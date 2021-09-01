package eu.wajja.web.fetcher;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.wajja.web.fetcher.content.ContentAnalyzer;
import eu.wajja.web.fetcher.controller.ProxyController;
import eu.wajja.web.fetcher.controller.URLController;
import eu.wajja.web.fetcher.elasticsearch.ElasticSearchService;
import eu.wajja.web.fetcher.enums.Command;
import eu.wajja.web.fetcher.enums.Status;
import eu.wajja.web.fetcher.enums.SubStatus;
import eu.wajja.web.fetcher.model.Result;
import eu.wajja.web.fetcher.services.ReindexService;
import eu.wajja.web.fetcher.services.RobotService;
import eu.wajja.web.fetcher.services.constants.MetadataConstant;

@DisallowConcurrentExecution
public class WebFetcherJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebFetcherJob.class);

    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";

    private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    private ProxyController proxyController;

    private String jobId;
    private Long maxPages;
    private List<String> excludedDataRegex;
    private List<String> excludedLinkRegex;
    private List<String> includedLinkRegex;
    private String crawlerUserAgent;
    private String rootUrl;

    private ElasticSearchService elasticSearchService;
    private URLController urlController;
    private RobotService robotService;
    private ReindexService reindexService;
    private ThreadPoolExecutor[] threadPoolExecutors;
    private int threadCounter = 0;
    private Long sleep;
    private boolean enableHashtag;
    private boolean readRobot;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        Consumer<Map<String, Object>> consumer = (Consumer<Map<String, Object>>) dataMap.get(WebFetcher.PROPERTY_CONSUMER);
        List<String> initialUrls = (List<String>) dataMap.get(WebFetcher.PROPERTY_URLS);
        List<String> chromeThreads = (List<String>) dataMap.get(WebFetcher.PROPERTY_CHROME_DRIVERS);

        jobId = UUID.randomUUID().toString();

        this.maxPages = dataMap.getLong(WebFetcher.PROPERTY_MAX_PAGES);
        this.rootUrl = dataMap.getString(WebFetcher.PROPERTY_ROOT_URL);
        this.excludedDataRegex = (List<String>) dataMap.get(WebFetcher.PROPERTY_EXCLUDE_DATA);
        this.excludedLinkRegex = (List<String>) dataMap.get(WebFetcher.PROPERTY_EXCLUDE_LINK);
        this.includedLinkRegex = (List<String>) dataMap.get(WebFetcher.PROPERTY_INCLUDE_LINK);
        this.crawlerUserAgent = dataMap.getString(WebFetcher.PROPERTY_CRAWLER_USER_AGENT);
        this.sleep = dataMap.getLong(WebFetcher.PROPERTY_SLEEP);
        this.enableHashtag = dataMap.getBoolean(WebFetcher.PROPERTY_ENABLE_HASHTAG);
        this.readRobot = dataMap.getBoolean(WebFetcher.PROPERTY_READ_ROBOT);

        String waitForCssSelector = dataMap.getString(WebFetcher.PROPERTY_WAIT_FOR_CSS_SELECTOR);
        Long maxWaitForCssSelector = dataMap.getLong(WebFetcher.PROPERTY_MAX_WAIT_FOR_CSS_SELECTOR);

        if (proxyController == null) {

            proxyController = new ProxyController(dataMap.getString(WebFetcher.PROPERTY_PROXY_USER),
                    dataMap.getString(WebFetcher.PROPERTY_PROXY_PASS),
                    dataMap.getString(WebFetcher.PROPERTY_PROXY_HOST),
                    dataMap.getLong(WebFetcher.PROPERTY_PROXY_PORT));
        }

        boolean enableDelete = dataMap.getBoolean(WebFetcher.PROPERTY_ENABLE_DELETE);
        boolean enableCrawl = dataMap.getBoolean(WebFetcher.PROPERTY_ENABLE_CRAWL);
        boolean reindex = dataMap.getBoolean(WebFetcher.PROPERTY_REINDEX);
        boolean enableRegex = dataMap.getBoolean(WebFetcher.PROPERTY_ENABLE_REGEX);
        boolean enableJsLinks = dataMap.getBoolean(WebFetcher.PROPERTY_ENABLE_JSLINKS);

        List<String> hostnames = (List<String>) dataMap.get(WebFetcher.PROPERTY_ELASTIC_HOSTNAMES);

        Long proxyPort = proxyController.getProxyPort();
        String proxyUsername = proxyController.getProxyUser();
        String proxyPassword = proxyController.getProxyPass();
        String username = dataMap.getString(WebFetcher.PROPERTY_ELASTIC_USERNAME);
        String password = dataMap.getString(WebFetcher.PROPERTY_ELASTIC_PASSWORD);
        String proxyScheme = proxyController.getProxyHost();
        String proxyHostname = proxyController.getProxyHost();

        elasticSearchService = new ElasticSearchService(hostnames, username, password, proxyScheme, proxyHostname, proxyPort, proxyUsername, proxyPassword);

        if (urlController == null) {

            urlController = new URLController(
                    elasticSearchService,
                    proxyController.getProxy(),
                    dataMap.getLong(WebFetcher.PROPERTY_TIMEOUT),
                    dataMap.getString(WebFetcher.PROPERTY_CRAWLER_USER_AGENT),
                    dataMap.getString(WebFetcher.PROPERTY_CRAWLER_REFERER),
                    waitForCssSelector,
                    maxWaitForCssSelector.intValue(),
                    enableJsLinks);

        }

        reindexService = new ReindexService(elasticSearchService, excludedDataRegex, excludedLinkRegex);
        robotService = new RobotService(urlController, elasticSearchService, readRobot);

        if (threadPoolExecutors == null) {

            if (chromeThreads.isEmpty()) {

                threadPoolExecutors = new ThreadPoolExecutor[1];
                threadPoolExecutors[0] = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);

            } else {

                threadPoolExecutors = new ThreadPoolExecutor[chromeThreads.size()];

                for (int x = 0; x < chromeThreads.size(); x++) {
                    threadPoolExecutors[x] = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
                }
            }
        }

        initialUrls.stream().map(i -> getUrlString(i, i, i)).forEach(initialUrl -> {

            String id = Base64.getEncoder().encodeToString(initialUrl.getBytes()).replace("/", "_");
            String index = "logstash_web_fetcher_" + id.toLowerCase();
            elasticSearchService.checkIndex(index);

            if (enableRegex) {

                // Here we rerun though all the completed pages, check the
                // regexes are correct
                reRunRegexExclusions(initialUrl, index);
            }

            if (reindex) {
                reindexService.reIndex(consumer, jobId, initialUrl, index);
                elasticSearchService.flushIndex(index);

            }

            if (enableCrawl) {

                // Read the robot.txt first
                robotService.checkRobot(chromeThreads, initialUrl, index, jobId);

                // Rerun previously queued items first
                fetchQueuedItems(consumer, chromeThreads, initialUrl, index);
                elasticSearchService.flushIndex(index);

                // Start the actual fetch
                fetchNewItems(consumer, chromeThreads, initialUrl, index);
                elasticSearchService.flushIndex(index);

            }

            if (enableDelete) {
                // Deleting all the old items
                deleteOldItems(consumer, initialUrl, index);
            }

        });

        LOGGER.info("Finished Thread {}", jobId);

    }

    private void fetchNewItems(Consumer<Map<String, Object>> consumer, List<String> chromeThreads, String initialUrl, String index) {

        LOGGER.info("Starting fetching items for thread : {}, url : {}", jobId, initialUrl);

        String chromeDriver = chromeThreads.stream().findFirst().orElse(null);
        extractUrl(consumer, initialUrl, initialUrl, chromeDriver, index, true);

        try {

            // TODO : The index doesn't have the time to flush. so nothing is
            // found in the
            // next step. fix this at some point
            Thread.sleep(1000);

            LinkedList<Result> results = new LinkedList<>();
            Future<Boolean> future = elasticSearchService.getAsyncUrls(index, results, Status.queue);

            while (!future.isDone()) {

                while (!results.isEmpty()) {
                    addNewThread(results.pop(), chromeThreads, consumer, index, true);
                }

                waitForThreads(initialUrl, 100);
            }

            while (!results.isEmpty()) {
                addNewThread(results.pop(), chromeThreads, consumer, index, true);
            }

            waitForThreads(initialUrl);

            if (maxPages == 0 || elasticSearchService.totalCountWithJobId(jobId, index) >= maxPages) {
                return;
            }

            if (elasticSearchService.hasMoreItemsInQueued(index)) {
                fetchNewItems(consumer, chromeThreads, initialUrl, index);
            }

        } catch (InterruptedException e) {
            LOGGER.info("InterruptedException", e);
            Thread.currentThread().interrupt();
        }

        LOGGER.info("Finished fetching items for thread : {}, url : {}", jobId, initialUrl);

    }

    private void fetchQueuedItems(Consumer<Map<String, Object>> consumer, List<String> chromeThreads, String initialUrl, String index) {

        LOGGER.info("Starting queued items for thread : {}, url : {}", jobId, initialUrl);

        try {

            // TODO : The index doesn't have the time to flush. so nothing is
            // found in the
            // next step. fix this at some point
            Thread.sleep(1000);

            LinkedList<Result> results = new LinkedList<>();
            Future<Boolean> future = elasticSearchService.getAsyncUrls(index, results, Status.queue);

            while (!future.isDone()) {

                while (!results.isEmpty()) {
                    addNewThread(results.pop(), chromeThreads, consumer, index, true);
                }

                waitForThreads(initialUrl, 100);
            }

            while (!results.isEmpty()) {
                addNewThread(results.pop(), chromeThreads, consumer, index, true);
            }

            waitForThreads(initialUrl);

            if (maxPages == 0 || elasticSearchService.totalCountWithJobId(jobId, index) >= maxPages) {
                return;
            }

            if (elasticSearchService.hasMoreItemsInQueued(index)) {
                fetchNewItems(consumer, chromeThreads, initialUrl, index);
            }

        } catch (InterruptedException e) {
            LOGGER.info("InterruptedException", e);
            Thread.currentThread().interrupt();
        }

        LOGGER.info("Finished queued items for thread : {}, url : {}", jobId, initialUrl);

    }

    private void addNewThread(Result result, List<String> chromeThreads, Consumer<Map<String, Object>> consumer, String index, boolean checkChildren) {

        String resultUrl = result.getUrl();
        String resultRootUrl = result.getRootUrl();

        if (chromeThreads.isEmpty()) {

            threadPoolExecutors[0].execute(() -> extractUrl(consumer, resultUrl, resultRootUrl, null, index, checkChildren));

        } else {

            String driver = chromeThreads.get(threadCounter);

            threadPoolExecutors[threadCounter].execute(() -> extractUrl(consumer, resultUrl, resultRootUrl, driver, index, checkChildren));

            if ((chromeThreads.size() - 1) == threadCounter) {
                threadCounter = 0;
            } else {
                threadCounter++;
            }
        }
    }

    private void waitForThreads(String initialUrl) {

        waitForThreads(initialUrl, 0);

    }

    private void waitForThreads(String initialUrl, Integer maxThreads) {

        for (int x = 0; x < threadPoolExecutors.length; x++) {

            int y = 0;

            while (threadPoolExecutors[x].getActiveCount() > maxThreads) {

                y++;

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    LOGGER.error("Failed to sleep in thread", e);
                    Thread.currentThread().interrupt();
                }

                if (y > 300) {
                    LOGGER.info("Waiting for site {} to be finished, active threads left {}", initialUrl, threadPoolExecutors[x].getActiveCount());
                }
            }
        }

    }

    private void extractUrl(Consumer<Map<String, Object>> consumer, String urlTmp, String rootUrlTmp, String chromeDriver, String index, boolean checkChildren) {

        String baseUrl = (this.rootUrl != null) ? this.rootUrl : rootUrlTmp;
        String url = getUrlString(urlTmp, baseUrl, baseUrl);

        LOGGER.info("Processing jobId {} url {}", jobId, url);

        try {

            if (maxPages == 0 || elasticSearchService.totalCountWithJobId(jobId, index) >= maxPages) {

                // check if we dont have too many pages
                LOGGER.info("Reached max pages for jobId {} = {}/{}", jobId, elasticSearchService.totalCountWithJobId(jobId, index), maxPages);

            } else if (!robotService.isAllowed(url, rootUrl, index, jobId, crawlerUserAgent)) {

                // Check if robot allows url
                elasticSearchService.addNewUrl(url, rootUrl, jobId, index, Status.processed, SubStatus.excluded, "robot dissallowed");

            } else if (excludedLinkRegex.stream().anyMatch(ex -> url.matches(ex))) {

                // Exclude if link is not allowed
                elasticSearchService.addNewUrl(url, rootUrl, jobId, index, Status.processed, SubStatus.excluded, "regex excludedLinkRegex");

            } else {

                // we have to fetch the data to continue here

                Result result = urlController.getURL(index, url, baseUrl, chromeDriver);
                Thread.sleep(sleep);
                ContentAnalyzer contentAnalyer = ContentAnalyzer.getInstance(result, readRobot);

                if (result == null || result.getContent() == null) {

                    // content is empty
                    elasticSearchService.addNewUrl(url, rootUrl, jobId, index, Status.failed, SubStatus.excluded, "content is empty");

                } else if (result.getCode() != 200) {

                    // Exclude if data is not allowed
                    elasticSearchService.addNewUrl(url, rootUrl, jobId, index, Status.failed, SubStatus.excluded, result.getCode().toString());

                } else if (excludedDataRegex.stream().anyMatch(ex -> result.getUrl().matches(ex))) {

                    // Exclude if data is not allowed

                    List<String> excluded = excludedDataRegex.stream().filter(ex -> result.getUrl().matches(ex)).collect(Collectors.toList());
                    LOGGER.info("Excluding url {} because it matched data regex {}", result.getUrl(), excluded);

                    elasticSearchService.addNewUrl(result, jobId, index, Status.processed, SubStatus.excluded, "excludedDataRegex");

                } else if (contentAnalyer != null && contentAnalyer.isExcluded()) {

                    // Exclude because of the document's content

                    LOGGER.info("Excluding url {} because {}", result.getUrl(), contentAnalyer.getExclusionReason());
                    elasticSearchService.addNewUrl(result, jobId, index, Status.processed, SubStatus.excluded, contentAnalyer.getExclusionReason());

                } else if (result.isCached()) {

                    LOGGER.info("Already sent url {}", result.getUrl());
                    elasticSearchService.addNewUrl(result, jobId, index, Status.processed, SubStatus.included, "Document already sent to filter");

                } else {

                    LOGGER.info("Sending url {}", result.getUrl());

                    Map<String, Object> metadata = new HashMap<>();
                    metadata.put(MetadataConstant.METADATA_URL, result.getUrl());
                    metadata.put(MetadataConstant.METADATA_INDEX, index);
                    metadata.put(MetadataConstant.METADATA_CONTENT_TYPE, result.getContentType());
                    metadata.put(MetadataConstant.METADATA_REFERENCE, Base64.getEncoder().encodeToString(result.getUrl().getBytes()));
                    metadata.put(MetadataConstant.METADATA_EPOCH, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
                    metadata.put(MetadataConstant.METADATA_UUID, UUID.randomUUID().toString());
                    metadata.put(MetadataConstant.METADATA_STATUS, 200);
                    metadata.put(MetadataConstant.METADATA_CONTEXT, baseUrl);
                    metadata.put(MetadataConstant.METADATA_COMMAND, Command.ADD.toString());
                    metadata.put(MetadataConstant.METADATA_CONTENT, Base64.getEncoder().encodeToString(result.getContent()));

                    consumer.accept(metadata);

                    elasticSearchService.addNewUrl(result, jobId, index, Status.processed, SubStatus.included, "Document sent to filter");
                }

                if (checkChildren && result != null && result.getContent() != null && baseUrl != null) {

                    Map<String, List<String>> headers = result.getHeaders();
                    Set<String> includedChildPages = new HashSet<>();

                    if (headers.containsKey("Content-Type") && headers.get("Content-Type").get(0).contains("html")) {

                        if (result.getChildUrls().isEmpty()) {

                            String bodyHtml = IOUtils.toString(result.getContent(), StandardCharsets.UTF_8.name());
                            org.jsoup.nodes.Document document = Jsoup.parse(bodyHtml);
                            Elements elements = document.getElementsByAttribute("href");

                            includedChildPages = elements.stream().map(e -> e.attr("href")).collect(Collectors.toSet());

                        } else {
                            includedChildPages = result.getChildUrls();

                        }

                    } else if (headers.containsKey("Content-Type") && headers.get("Content-Type").get(0).contains("xml")) {

                        try (InputStream inputStream = new ByteArrayInputStream(result.getContent())) {

                            DocumentBuilder builder = factory.newDocumentBuilder();
                            Document doc = builder.parse(inputStream);

                            NodeList nodeList = doc.getElementsByTagName("link");

                            for (int x = 0; nodeList.getLength() > x; x++) {

                                Node node = nodeList.item(x);
                                includedChildPages.add(node.getTextContent());
                            }

                        } catch (Exception e) {
                            LOGGER.error("Failed to parse xml", e);
                        }

                    }

                    String simpleUrlString = baseUrl.replace(HTTP, "").replace(HTTPS, "");

                    LOGGER.debug("Checking children {}", includedChildPages);

                    includedChildPages = includedChildPages.stream()
                            .filter(href -> !href.equals("/") && !href.startsWith("//"))
                            .map(urlStream -> getUrlString(urlStream, result.getUrl(), baseUrl))
                            .filter(href -> href.startsWith(HTTP) || href.startsWith(HTTPS))
                            .filter(href -> href.startsWith(HTTP + simpleUrlString) || href.startsWith(HTTPS + simpleUrlString) || includedLinkRegex.stream().anyMatch(ex -> href.matches(ex)))
                            .filter(href -> !excludedLinkRegex.stream().anyMatch(ex -> href.matches(ex)))
                            .sorted()
                            .collect(Collectors.toSet());

                    LOGGER.debug("Checked children {}", includedChildPages);

                    includedChildPages.parallelStream().forEach(href -> elasticSearchService.addNewChildUrl(href, baseUrl, jobId, index));
                }

                elasticSearchService.flushIndex(index);

            }

        } catch (Exception e) {
            LOGGER.error("Failed to retrieve URL from thread {}, url {}", jobId, url, e);
        }

    }

    private String getUrlString(String urlString, String parentUrl, String rootUrl) {

        urlString = urlString.trim();

        try {
            if (!urlString.startsWith("http") && urlString.startsWith("/")) {

                URL urlRoot = new URL(rootUrl);
                String path = urlRoot.getPath();

                if (StringUtils.isEmpty(path) || path.equals("/")) {

                    if (urlRoot.toString().endsWith("/") && urlString.startsWith("/")) {
                        urlString = urlRoot + urlString.substring(1);
                    } else {
                        urlString = urlRoot + urlString;
                    }

                } else {
                    urlString = rootUrl.replace(path, "") + urlString;
                }

            } else if (!urlString.startsWith("http") && !urlString.startsWith("/")) {

                URL urlRoot = new URL(rootUrl);

                if (urlString.startsWith("/")) {
                    urlString = urlRoot + "/" + urlString;
                } else {
                    urlString = parentUrl.substring(0, parentUrl.lastIndexOf('/') + 1) + urlString;
                }
            }

            if (!urlString.startsWith("http") && !urlString.startsWith("/")) {
                urlString = rootUrl + urlString;
            }

            if (!enableHashtag && urlString.contains("#")) {
                urlString = urlString.substring(0, urlString.indexOf("#"));
            }

        } catch (MalformedURLException e) {
            LOGGER.error("Failed to parse url {}", urlString, e);
        }

        try {
            return new URI(urlString).normalize().toString();
        } catch (URISyntaxException e) {
            LOGGER.warn("Failed to normalize url {}", urlString);
        }

        return urlString;
    }

    private void reRunRegexExclusions(String initialUrl, String index) {

        LOGGER.info("Starting regexes items for thread : {}, url : {}", jobId, initialUrl);

        LinkedList<Result> results = new LinkedList<>();
        Future<Boolean> future = elasticSearchService.getAsyncUrls(index, results, Status.processed);

        while (!future.isDone()) {

            while (!results.isEmpty()) {

                Result result = results.pop();
                checkStatus(index, result);

            }
        }

        while (!results.isEmpty()) {

            Result result = results.pop();
            checkStatus(index, result);
        }

        LOGGER.info("Finished regexes items for thread : {}, url : {}", jobId, initialUrl);

    }

    private void checkStatus(String index, Result result) {

        SubStatus subStatus = null;

        if (result.getSubStatus() != null) {
            subStatus = SubStatus.valueOf(result.getSubStatus());
        }

        String href = getUrlString(result.getUrl(), result.getRootUrl(), result.getRootUrl());

        List<String> linkExcluded = excludedLinkRegex.parallelStream().filter(ex -> href.matches(ex)).collect(Collectors.toList());
        List<String> dataExcluded = excludedDataRegex.parallelStream().filter(ex -> href.matches(ex)).collect(Collectors.toList());

        if (!linkExcluded.isEmpty()) {

            LOGGER.info("excluded link regex for url {}, regex {}", result.getUrl(), linkExcluded);

            // Exclude from processed
            if (subStatus == null || subStatus.equals(SubStatus.included)) {
                elasticSearchService.updateStatus(result.getUrl(), index, Status.processed, SubStatus.excluded, "regex excludedLinkRegex");
            }

        } else if (!dataExcluded.isEmpty()) {

            LOGGER.info("excluded data regex for url {}, regex {}", result.getUrl(), dataExcluded);

            // Exclude from processed
            if (subStatus == null || subStatus.equals(SubStatus.included)) {
                elasticSearchService.updateStatus(result.getUrl(), index, Status.processed, SubStatus.excluded, "regex excludedDataRegex");
            }

        } else {

            LOGGER.info("regex enabled for url {}", result.getUrl());

            // Include from processed
            if (subStatus == null || subStatus.equals(SubStatus.excluded)) {
                elasticSearchService.updateStatus(result.getUrl(), index, Status.processed, SubStatus.included, "Document sent to filter");
            }
        }
    }

    private void deleteOldItems(Consumer<Map<String, Object>> consumer, String initialUrl, String index) {

        LOGGER.info("Starting deleting items for thread : {}, url : {}", jobId, initialUrl);

        LinkedList<Result> results = new LinkedList<>();
        Future<Boolean> future = elasticSearchService.getAsyncUrls(index, results, Status.processed, SubStatus.excluded);

        while (!future.isDone()) {

            while (!results.isEmpty()) {

                Result result = results.pop();
                deleteResult(consumer, result);
            }
        }

        while (!results.isEmpty()) {

            Result result = results.pop();
            deleteResult(consumer, result);

        }

        LOGGER.info("Finished deleting items for thread : {}, url : {}", jobId, initialUrl);

    }

    private void deleteResult(Consumer<Map<String, Object>> consumer, Result result) {

        String reference = Base64.getEncoder().encodeToString(result.getUrl().getBytes());

        Map<String, Object> metadata = new HashMap<>();
        metadata.put(MetadataConstant.METADATA_REFERENCE, reference);
        metadata.put(MetadataConstant.METADATA_COMMAND, Command.DELETE.toString());

        consumer.accept(metadata);

        LOGGER.info("Deleting item for thread : {}, url : {}", jobId, result.getUrl());
    }
}

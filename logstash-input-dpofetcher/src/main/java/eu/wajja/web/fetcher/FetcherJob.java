package eu.wajja.web.fetcher;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.wajja.web.fetcher.controller.ProxyController;
import eu.wajja.web.fetcher.controller.URLController;
import eu.wajja.web.fetcher.elasticsearch.ElasticSearchService;
import eu.wajja.web.fetcher.enums.Command;
import eu.wajja.web.fetcher.enums.Status;
import eu.wajja.web.fetcher.enums.SubStatus;
import eu.wajja.web.fetcher.model.Result;
import eu.wajja.web.fetcher.model.ResultHits;
import eu.wajja.web.fetcher.services.ReindexService;
import eu.wajja.web.fetcher.services.RobotService;
import eu.wajja.web.fetcher.services.constants.MetadataConstant;

@DisallowConcurrentExecution
public class FetcherJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(FetcherJob.class);

    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";

    private ProxyController proxyController;

    private String jobId;
    private Long maxPages;
    private List<String> excludedDataRegex;
    private List<String> excludedLinkRegex;
    private String crawlerUserAgent;
    private String rootUrl;

    private ElasticSearchService elasticSearchService;
    private URLController urlController;
    private RobotService robotService;
    private ReindexService reindexService;
    private ThreadPoolExecutor[] threadPoolExecutors;
    private int threadCounter = 0;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        Consumer<Map<String, Object>> consumer = (Consumer<Map<String, Object>>) dataMap.get(DpoFetcher.PROPERTY_CONSUMER);
        List<String> initialUrls = (List<String>) dataMap.get(DpoFetcher.PROPERTY_URLS);
        List<String> chromeThreads = (List<String>) dataMap.get(DpoFetcher.PROPERTY_CHROME_DRIVERS);

        jobId = UUID.randomUUID().toString();

        this.maxPages = dataMap.getLong(DpoFetcher.PROPERTY_MAX_PAGES);
        this.rootUrl = dataMap.getString(DpoFetcher.PROPERTY_ROOT_URL);
        this.excludedDataRegex = (List<String>) dataMap.get(DpoFetcher.PROPERTY_EXCLUDE_DATA);
        this.excludedLinkRegex = (List<String>) dataMap.get(DpoFetcher.PROPERTY_EXCLUDE_LINK);
        this.crawlerUserAgent = dataMap.getString(DpoFetcher.PROPERTY_CRAWLER_USER_AGENT);

        String waitForCssSelector = dataMap.getString(DpoFetcher.PROPERTY_WAIT_FOR_CSS_SELECTOR);
        Long maxWaitForCssSelector = dataMap.getLong(DpoFetcher.PROPERTY_MAX_WAIT_FOR_CSS_SELECTOR);

        if (proxyController == null) {

            proxyController = new ProxyController(dataMap.getString(DpoFetcher.PROPERTY_PROXY_USER),
                    dataMap.getString(DpoFetcher.PROPERTY_PROXY_PASS),
                    dataMap.getString(DpoFetcher.PROPERTY_PROXY_HOST),
                    dataMap.getLong(DpoFetcher.PROPERTY_PROXY_PORT),
                    dataMap.getBoolean(DpoFetcher.PROPERTY_SSL_CHECK));
        }

        boolean enableDelete = dataMap.getBoolean(DpoFetcher.PROPERTY_ENABLE_DELETE);
        boolean enableCrawl = dataMap.getBoolean(DpoFetcher.PROPERTY_ENABLE_CRAWL);
        boolean readRobot = dataMap.getBoolean(DpoFetcher.PROPERTY_READ_ROBOT);
        boolean reindex = dataMap.getBoolean(DpoFetcher.PROPERTY_REINDEX);
        boolean enableRegex = dataMap.getBoolean(DpoFetcher.PROPERTY_ENABLE_REGEX);

        List<String> hostnames = (List<String>) dataMap.get(DpoFetcher.PROPERTY_ELASTIC_HOSTNAMES);

        Long proxyPort = proxyController.getProxyPort();
        String proxyUsername = proxyController.getProxyUser();
        String proxyPassword = proxyController.getProxyPass();
        String username = dataMap.getString(DpoFetcher.PROPERTY_ELASTIC_USERNAME);
        String password = dataMap.getString(DpoFetcher.PROPERTY_ELASTIC_PASSWORD);
        String proxyScheme = proxyController.getProxyHost();
        String proxyHostname = proxyController.getProxyHost();

        elasticSearchService = new ElasticSearchService(hostnames, username, password, proxyScheme, proxyHostname, proxyPort, proxyUsername, proxyPassword);

        if (urlController == null) {

            urlController = new URLController(
                    elasticSearchService,
                    proxyController.getProxy(),
                    dataMap.getLong(DpoFetcher.PROPERTY_TIMEOUT),
                    dataMap.getString(DpoFetcher.PROPERTY_CRAWLER_USER_AGENT),
                    dataMap.getString(DpoFetcher.PROPERTY_CRAWLER_REFERER),
                    waitForCssSelector,
                    maxWaitForCssSelector.intValue());

        }

        reindexService = new ReindexService(elasticSearchService, excludedDataRegex, excludedLinkRegex);
        robotService = new RobotService(urlController, elasticSearchService, readRobot);

        if (threadPoolExecutors == null) {

            threadPoolExecutors = new ThreadPoolExecutor[chromeThreads.size()];

            for (int x = 0; x < chromeThreads.size(); x++) {
                threadPoolExecutors[x] = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
            }
        }

        initialUrls.stream().map(i -> getUrlString(i, i)).forEach(initialUrl -> {

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

        String currentUrl = initialUrl + "&page=1&size=10000";
        Result resultParent = urlController.getURL(index, currentUrl, initialUrl, chromeThreads.get(0), true);

        String content = new String(resultParent.getContent());
        ObjectMapper objectMapper = new ObjectMapper();

        try {

            ResultHits resultHits = objectMapper.readValue(content, ResultHits.class);

            resultHits.getHits().getHits().stream().forEach(hit -> {

                String childUrl = rootUrl + "/detail/" + hit.get_id();
                Result result = urlController.getURL(index, childUrl, initialUrl, chromeThreads.get(0), false);

                LOGGER.info("Sending url {}", result.getUrl());

                Map<String, Object> metadata = new HashMap<>();
                metadata.put(MetadataConstant.METADATA_URL, result.getUrl());
                metadata.put(MetadataConstant.METADATA_INDEX, index);
                metadata.put(MetadataConstant.METADATA_CONTENT_TYPE, result.getContentType());
                metadata.put(MetadataConstant.METADATA_REFERENCE, Base64.getEncoder().encodeToString(result.getUrl().getBytes()));
                metadata.put(MetadataConstant.METADATA_EPOCH, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
                metadata.put(MetadataConstant.METADATA_UUID, UUID.randomUUID().toString());
                metadata.put(MetadataConstant.METADATA_STATUS, 200);
                metadata.put(MetadataConstant.METADATA_CONTEXT, rootUrl);
                metadata.put(MetadataConstant.METADATA_COMMAND, Command.ADD.toString());
                metadata.put(MetadataConstant.METADATA_CONTENT, Base64.getEncoder().encodeToString(result.getContent()));

                consumer.accept(metadata);

                elasticSearchService.addNewUrl(result, jobId, index, Status.processed, SubStatus.included, "Document sent to filter");

            });

        } catch (IOException e) {
            LOGGER.error("Failed to download main json", e);
        }

        LOGGER.info("Finished fetching items for thread : {}, url : {}", jobId, initialUrl);

    }

    private String getUrlString(String urlString, String rootUrl) {

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
                String path = urlRoot.getPath();

                if (StringUtils.isEmpty(path) || path.equals("/")) {

                    urlString = urlRoot + "/" + urlString;
                } else {
                    urlString = urlRoot.toString().substring(0, urlRoot.toString().lastIndexOf('/') + 1) + urlString;
                }
            }

            if (!urlString.startsWith("http") && !urlString.startsWith("/")) {
                urlString = rootUrl + urlString;
            }

            if (urlString.contains("#")) {
                urlString = urlString.substring(0, urlString.indexOf("#") - 1);
            }

        } catch (MalformedURLException e) {
            LOGGER.error("Failed to parse url {}", urlString, e);
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

        String href = getUrlString(result.getUrl(), result.getRootUrl());

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

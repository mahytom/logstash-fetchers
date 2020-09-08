package eu.wajja.web.fetcher;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.wajja.web.fetcher.controller.ProxyController;
import eu.wajja.web.fetcher.controller.URLController;
import eu.wajja.web.fetcher.elasticsearch.ElasticSearchService;
import eu.wajja.web.fetcher.enums.Command;
import eu.wajja.web.fetcher.model.Result;

@DisallowConcurrentExecution
public class FetcherJob implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(FetcherJob.class);

	private static final String METADATA_URL = "url";
	private static final String METADATA_INDEX = "index";
	private static final String METADATA_CONTENT_TYPE = "type";

	private static final String METADATA_EPOCH = "epochSecond";
	private static final String METADATA_REFERENCE = "reference";
	private static final String METADATA_CONTENT = "content";
	private static final String METADATA_CONTEXT = "context";
	private static final String METADATA_UUID = "uuid";
	private static final String METADATA_STATUS = "status";
	private static final String METADATA_COMMAND = "command";

	private static final String HTTP = "http://";
	private static final String HTTPS = "https://";
	private static final String ROBOTS = "robots.txt";

	private Random random = new Random();
	private ProxyController proxyController;

	private String jobId;
	private Long maxPages;
	private List<String> excludedDataRegex;
	private List<String> excludedLinkRegex;
	private String threadId;
	private String crawlerUserAgent;
	private String rootUrl;
	private ElasticSearchService elasticSearchService;

	protected Map<String, Set<String>> disallowedLocations = new HashMap<>();
	protected Map<String, Set<String>> allowedLocations = new HashMap<>();
	protected Map<String, Set<String>> sitemapLocations = new HashMap<>();

	private URLController urlController;
	private ThreadPoolExecutor[] threadPoolExecutors;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		Consumer<Map<String, Object>> consumer = (Consumer<Map<String, Object>>) dataMap.get(WebFetcher.PROPERTY_CONSUMER);
		List<String> initialUrls = (List<String>) dataMap.get(WebFetcher.PROPERTY_URLS);
		List<String> chromeThreads = (List<String>) dataMap.get(WebFetcher.PROPERTY_CHROME_DRIVERS);

		jobId = UUID.randomUUID().toString();

		boolean readRobot = dataMap.getBoolean(WebFetcher.PROPERTY_READ_ROBOT);

		this.maxPages = dataMap.getLong(WebFetcher.PROPERTY_MAX_PAGES);
		this.threadId = dataMap.getString(WebFetcher.PROPERTY_THREAD_ID);
		this.rootUrl = dataMap.getString(WebFetcher.PROPERTY_ROOT_URL);
		this.excludedDataRegex = (List<String>) dataMap.get(WebFetcher.PROPERTY_EXCLUDE_DATA);
		this.excludedLinkRegex = (List<String>) dataMap.get(WebFetcher.PROPERTY_EXCLUDE_LINK);
		this.crawlerUserAgent = dataMap.getString(WebFetcher.PROPERTY_CRAWLER_USER_AGENT);

		String waitForCssSelector = dataMap.getString(WebFetcher.PROPERTY_WAIT_FOR_CSS_SELECTOR);
		Long maxWaitForCssSelector = dataMap.getLong(WebFetcher.PROPERTY_MAX_WAIT_FOR_CSS_SELECTOR);

		if (proxyController == null) {

			proxyController = new ProxyController(dataMap.getString(WebFetcher.PROPERTY_PROXY_USER),
					dataMap.getString(WebFetcher.PROPERTY_PROXY_PASS),
					dataMap.getString(WebFetcher.PROPERTY_PROXY_HOST),
					dataMap.getLong(WebFetcher.PROPERTY_PROXY_PORT),
					dataMap.getBoolean(WebFetcher.PROPERTY_SSL_CHECK));
		}

		List<String> hostnames = (List<String>) dataMap.get(WebFetcher.PROPERTY_ELASTIC_HOSTNAMES);
		String username = dataMap.getString(WebFetcher.PROPERTY_ELASTIC_USERNAME);
		String password = dataMap.getString(WebFetcher.PROPERTY_ELASTIC_PASSWORD);
		String proxyScheme = proxyController.getProxyHost();
		String proxyHostname = proxyController.getProxyHost();
		Long proxyPort = proxyController.getProxyPort();
		String proxyUsername = proxyController.getProxyUser();
		String proxyPassword = proxyController.getProxyPass();

		elasticSearchService = new ElasticSearchService(hostnames, username, password, proxyScheme, proxyHostname, proxyPort, proxyUsername, proxyPassword);

		if (urlController == null) {

			urlController = new URLController(
					elasticSearchService,
					proxyController.getProxy(),
					dataMap.getLong(WebFetcher.PROPERTY_TIMEOUT),
					dataMap.getString(WebFetcher.PROPERTY_CRAWLER_USER_AGENT),
					dataMap.getString(WebFetcher.PROPERTY_CRAWLER_REFERER),
					waitForCssSelector,
					maxWaitForCssSelector.intValue());

		}

		if (threadPoolExecutors == null) {

			threadPoolExecutors = new ThreadPoolExecutor[chromeThreads.size()];

			for (int x = 0; x < chromeThreads.size(); x++) {
				threadPoolExecutors[x] = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
			}
		}

		initialUrls.stream().map(i -> getUrlString(i, i)).forEach(initialUrl -> {

			LOGGER.info("Starting fetch for thread : {}, url : {}", threadId, initialUrl);

			String id = Base64.getEncoder().encodeToString(initialUrl.getBytes());
			String index = "logstash_web_fetcher_" + id.toLowerCase();
			elasticSearchService.checkIndex(index);

			// Read the robot.txt first

			if (readRobot) {

				LOGGER.info("Reading Robot : {}, url : {}", threadId, initialUrl);

				Pattern p = Pattern.compile("(http).*(\\/\\/)[^\\/]{2,}(\\/)");
				Matcher m = p.matcher(initialUrl);

				if (m.find()) {
					String robotUrl = m.group(0) + ROBOTS;
					readRobot(index, initialUrl, robotUrl, chromeThreads.get(0));

				} else {
					LOGGER.warn("Failed to find robot.txt url {}", initialUrl);
				}

				LOGGER.info("Finished Reading Robot : {}, url : {}", threadId, initialUrl);

			}

			Integer randomValue = random.nextInt(chromeThreads.size());

			LOGGER.info("Adding url {}", initialUrl);
			extractUrl(consumer, initialUrl, initialUrl, chromeThreads.get(randomValue), index);

			List<Result> results = elasticSearchService.getUrlsToProcess(jobId, index);

			while (!results.isEmpty()) {

				int threadCounter = 0;

				for (int y = 0; results.size() > y; y++) {

					Result result = results.get(y);
					String driver = chromeThreads.get(threadCounter);
					threadPoolExecutors[threadCounter].execute(() -> extractUrl(consumer, result.getUrl(), result.getRootUrl(), driver, index));

					if ((chromeThreads.size() - 1) == threadCounter) {
						threadCounter = 0;
					} else {
						threadCounter++;
					}

				}

				for (int x = 0; x < threadPoolExecutors.length; x++) {

					int y = 0;

					while (threadPoolExecutors[x].getActiveCount() > 0) {

						y++;

						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							LOGGER.error("Failed to sleep in thread", e);
							Thread.currentThread().interrupt();
						}

						if (y > 300) {
							LOGGER.info("Waiting for site {} to be finished, active threads left {}", initialUrl, threadPoolExecutors[x].getActiveCount());
						}
					}
				}

				results = elasticSearchService.getUrlsToProcess(jobId, index);
			}

			deleteOldDocuments(consumer, index);
		});

		LOGGER.info("Finished Thread {}", threadId);

	}

	private void readRobot(String index, String initialUrl, String robotUrl, String chromeDriver) {

		Result result = urlController.getURL(index, robotUrl, initialUrl, chromeDriver);

		if (result.getContent() != null) {

			try (Scanner scanner = new Scanner(IOUtils.toString(result.getContent(), StandardCharsets.UTF_8.name()))) {

				elasticSearchService.addNewUrl(result, jobId, index, ElasticSearchService.STATUS_PROCESSED, "robot read");
				String userAgent = "*";

				while (scanner.hasNextLine()) {

					String line = scanner.nextLine().trim();

					if (!line.startsWith("#") && !line.isEmpty()) {

						if (line.startsWith("User-agent:")) {
							userAgent = line.replace("User-agent:", "").trim();

						} else if (line.startsWith("Disallow:")) {

							String perm = line.replace("Disallow:", "").trim();

							if (!disallowedLocations.containsKey(userAgent)) {
								disallowedLocations.put(userAgent, new HashSet<>());
							}

							disallowedLocations.get(userAgent).add(perm);

						} else if (line.startsWith("Allow:")) {

							String perm = line.replace("Allow:", "").trim();

							if (!allowedLocations.containsKey(userAgent)) {
								allowedLocations.put(userAgent, new HashSet<>());
							}

							allowedLocations.get(userAgent).add(perm);

						} else if (line.startsWith("Sitemap:")) {

							String perm = line.replace("Sitemap:", "").trim();

							if (!sitemapLocations.containsKey(userAgent)) {
								sitemapLocations.put(userAgent, new HashSet<>());
							}

							sitemapLocations.get(userAgent).add(perm);
						}
					}
				}

			} catch (Exception e) {
				LOGGER.error("Failed to parse robots.txt from url {}", robotUrl, e);
			}

		} else {
			LOGGER.warn("Failed to read robot.txt url, status {}, {}, {}", result.getCode(), initialUrl, result.getMessage());
		}
	}

	private void extractUrl(Consumer<Map<String, Object>> consumer, String urlStringTmp, String rootUrl, String chromeDriver, String index) {

		String rootUrlTmp = (this.rootUrl != null) ? this.rootUrl : rootUrl;
		String urlString = getUrlString(urlStringTmp, rootUrlTmp);

		LOGGER.info("Processing url {} in jobID {}", urlString, jobId);

		try {

			if (maxPages != 0 && elasticSearchService.totalCountWithJobId(urlString, jobId, index) <= maxPages) {

				Set<String> disallowedList = new HashSet<>();

				if (disallowedLocations.containsKey("*")) {
					disallowedList = disallowedLocations.get("*");
				}

				if (disallowedLocations.containsKey(crawlerUserAgent)) {
					disallowedList.addAll(disallowedLocations.get(crawlerUserAgent));
				}

				if (!disallowedList.isEmpty()) {

					String regex = (".*(" + String.join(")|(", disallowedList).replace("*", ".*").replace("/", "\\/") + ").*").trim();
					Pattern p = Pattern.compile(regex);
					Matcher m = p.matcher(urlString);

					if (m.find()) {
						LOGGER.info("URL {} is dissallowed", urlStringTmp);
						return;
					}
				}

				Result result = urlController.getURL(index, urlString, rootUrlTmp, chromeDriver);

				if (result != null && result.getContent() != null) {
					extractContent(consumer, result, index);

				} else {
					elasticSearchService.addNewUrl(result, jobId, index, ElasticSearchService.STATUS_FAILED, "content is empty");
					elasticSearchService.flushIndex(index);

					LOGGER.info("URL {} is does not have content", urlStringTmp);
				}
			}

		} catch (Exception e) {
			LOGGER.error("Failed to retrieve URL from thread {}, url {}", threadId, urlString, e);
		}

	}

	private void extractContent(Consumer<Map<String, Object>> consumer, Result result, String index) throws IOException {

		byte[] bytes = result.getContent();
		Map<String, List<String>> headers = result.getHeaders();

		Integer length = result.getLength();

		if (!elasticSearchService.existsInIndexWithSize(result.getUrl(), length, index)) {

			if (excludedDataRegex.stream().noneMatch(ex -> result.getUrl().matches(ex)) && result.getUrl().startsWith(result.getRootUrl())) {

				if (result.getCode() == 200 && result.getContent().length > 0) {

					LOGGER.info("Sending url {}", result.getUrl());

					Map<String, Object> metadata = new HashMap<>();
					metadata.put(METADATA_URL, result.getUrl());
					metadata.put(METADATA_INDEX, index);
					metadata.put(METADATA_CONTENT_TYPE, result.getContentType());
					metadata.put(METADATA_REFERENCE, Base64.getEncoder().encodeToString(result.getUrl().getBytes()));
					metadata.put(METADATA_EPOCH, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
					metadata.put(METADATA_UUID, UUID.randomUUID().toString());
					metadata.put(METADATA_STATUS, 200);
					metadata.put(METADATA_CONTEXT, result.getRootUrl());
					metadata.put(METADATA_COMMAND, Command.ADD.toString());
					metadata.put(METADATA_CONTENT, Base64.getEncoder().encodeToString(bytes));

					consumer.accept(metadata);
					elasticSearchService.addNewUrl(result, jobId, index, ElasticSearchService.STATUS_PROCESSED, "sent to API");

				} else {

					elasticSearchService.addNewUrl(result, jobId, index, ElasticSearchService.STATUS_FAILED, "Code is not 200 or content is empty");
					LOGGER.info("Skipping url {}, code : {}, size {}", result.getUrl(), result.getCode(), result.getLength());
				}

			} else {

				elasticSearchService.addNewUrl(result, jobId, index, ElasticSearchService.STATUS_FAILED, "Excluded by regex");
				LOGGER.info("Excluded url {}", result.getUrl());
			}

		} else {
			elasticSearchService.addNewUrl(result, jobId, index, ElasticSearchService.STATUS_PROCESSED, "Url is already in index");
			LOGGER.info("Url already in index {}", result.getUrl());
		}

		elasticSearchService.flushIndex(index);

		if (headers.containsKey("Content-Type") && headers.get("Content-Type").get(0).contains("html")) {

			String bodyHtml = IOUtils.toString(bytes, StandardCharsets.UTF_8.name());
			org.jsoup.nodes.Document document = Jsoup.parse(bodyHtml);
			Elements elements = document.getElementsByAttribute("href");

			String simpleUrlString = result.getRootUrl().replace(HTTP, "").replace(HTTPS, "");

			Set<String> includedChildPages = elements.stream().map(e -> e.attr("href"))
					.filter(href -> !href.equals("/") && !href.startsWith("//"))
					.map(url -> getUrlString(url, result.getRootUrl()))
					.filter(href -> href.startsWith(HTTP) || href.startsWith(HTTPS))
					.filter(href -> href.startsWith(HTTP + simpleUrlString) || href.startsWith(HTTPS + simpleUrlString))
					.filter(href -> !excludedLinkRegex.stream().anyMatch(ex -> href.matches(ex)))
					.sorted()
					.collect(Collectors.toSet());

			includedChildPages.parallelStream().forEach(href -> elasticSearchService.addNewChildUrl(href, result.getRootUrl(), jobId, index));
			elasticSearchService.flushIndex(index);
		}

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

	private void deleteOldDocuments(Consumer<Map<String, Object>> consumer, String index) {

		List<Result> results = elasticSearchService.getUrlsToDelete(jobId, index);

		while (!results.isEmpty()) {

			results.stream().forEach(result -> {

				String reference = Base64.getEncoder().encodeToString(result.getUrl().getBytes());
				LOGGER.info("Thread Sending Deletion {}, reference {}", threadId, reference);

				Map<String, Object> metadata = new HashMap<>();
				metadata.put(METADATA_REFERENCE, reference);
				metadata.put(METADATA_COMMAND, Command.DELETE.toString());

				consumer.accept(metadata);

				try {
					elasticSearchService.addNewUrl(result, jobId, index, ElasticSearchService.STATUS_DELETED, "document is sent for deletion");
					elasticSearchService.flushIndex(index);
				} catch (IOException e) {
					LOGGER.error("Failed to delete the reference {}", reference, e);
				}
			});

			results = elasticSearchService.getUrlsToDelete(jobId, index);
		}

		LOGGER.info("Finished deleting older documents/pages");
	}
}

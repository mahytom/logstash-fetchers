package eu.wajja.web.fetcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.ProxyConfig;
import com.machinepublishers.jbrowserdriver.ProxyConfig.Type;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.Settings.Builder;
import com.machinepublishers.jbrowserdriver.Timezone;
import com.machinepublishers.jbrowserdriver.UserAgent;

@DisallowConcurrentExecution
public class FetcherJob implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(FetcherJob.class);

	private static final String METADATA_EPOCH = "epochSecond";
	private static final String METADATA_REFERENCE = "reference";
	private static final String METADATA_CONTENT = "content";
	private static final String METADATA_URL = "url";
	private static final String METADATA_CONTEXT = "context";
	private static final String METADATA_UUID = "uuid";
	private static final String METADATA_STATUS = "status";
	private static final String METADATA_CHILD = "childPages";
	private static final String METADATA_EXTERNAL = "externalPages";
	private static final String METADATA_COMMAND = "command";

	private static final String HTTP = "http://";
	private static final String HTTPS = "https://";
	private static final String ROBOT = "robot.txt";

	private ObjectMapper objectMapper = new ObjectMapper();

	private Long maxDepth;
	private Long maxPages;
	private Long timeout;
	private Boolean waitJavascript;
	private List<String> excludedDataRegex;
	private List<String> excludedLinkRegex;
	private String chromeDriver;
	private Long maxPagesCount = 0l;
	private Proxy proxy = null;
	private WebDriver driver;
	private ThreadPoolExecutor executorService;
	private String threadId;

	private List<String> tmpList = new ArrayList<>();

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		Consumer<Map<String, Object>> consumer = (Consumer<Map<String, Object>>) dataMap.get(WebFetcher.PROPERTY_CONSUMER);
		String url = dataMap.getString(WebFetcher.PROPERTY_URL);

		String dataFolder = dataMap.getString(WebFetcher.PROPERTY_DATAFOLDER);

		this.maxDepth = dataMap.getLong(WebFetcher.PROPERTY_MAX_DEPTH);
		this.maxPages = dataMap.getLong(WebFetcher.PROPERTY_MAX_PAGES);
		this.timeout = dataMap.getLong(WebFetcher.PROPERTY_TIMEOUT);
		this.chromeDriver = dataMap.getString(WebFetcher.PROPERTY_CHROME_DRIVER);
		this.threadId = dataMap.getString(WebFetcher.PROPERTY_THREAD_ID);
		this.waitJavascript = dataMap.getBoolean(WebFetcher.PROPERTY_JAVASCRIPT);
		this.excludedDataRegex = (List<String>) dataMap.getOrDefault(WebFetcher.PROPERTY_EXCLUDE_DATA, new ArrayList<>());
		this.excludedLinkRegex = (List<String>) dataMap.getOrDefault(WebFetcher.PROPERTY_EXCLUDE_LINK, new ArrayList<>());

		initializeConnection(dataMap.getString(WebFetcher.PROPERTY_PROXY_USER),
				dataMap.getString(WebFetcher.PROPERTY_PROXY_PASS),
				dataMap.getString(WebFetcher.PROPERTY_PROXY_HOST),
				dataMap.getLong(WebFetcher.PROPERTY_PROXY_PORT),
				dataMap.getBoolean(WebFetcher.PROPERTY_SSL_CHECK));

		Long threads = dataMap.getLong(WebFetcher.PROPERTY_THREADS);
		executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads.intValue());

		LOGGER.info("Starting fetch for thread : {}, url : {}", threadId, url);

		String id = Base64.getEncoder().encodeToString(url.getBytes());

		try {

			URL robotUrl = new URL(url + "/" + ROBOT);
			HttpURLConnection urlConnection = getHttpConnection(robotUrl);
			int code = urlConnection.getResponseCode();
			String message = urlConnection.getResponseMessage();

			if (code == HttpURLConnection.HTTP_OK) {
				extractRobot(robotUrl.toString(), urlConnection, code, message);
			} else {
				LOGGER.warn("Failed to read robot.txt url, status {}, {}, {}", code, url, message);
			}

			extractUrl(consumer, url, url, 0l);
			deleteOldDocuments(consumer, dataFolder, id);

			while (executorService.getActiveCount() > 0) {
				LOGGER.debug("Thread count is : {}", executorService.getActiveCount());
				Thread.sleep(5000);
			}

		} catch (Exception e) {
			LOGGER.error("Failed to read robot", e);

		}

		if (this.waitJavascript) {
			LOGGER.info("Closing driver {}", url);
			this.driver.quit();
		}

		LOGGER.info("Finished Thread {}", threadId);

	}

	private void initializeConnection(String proxyUser, String proxyPass, String proxyHost, Long proxyPort, Boolean disableSSLcheck) {

		if (proxyUser != null && proxyPass != null) {

			LOGGER.info("Initializing Proxy Security {}:{}", proxyHost, proxyPort);
			Authenticator authenticator = new Authenticator() {

				@Override
				public PasswordAuthentication getPasswordAuthentication() {

					return new PasswordAuthentication(proxyUser, proxyPass.toCharArray());
				}
			};

			Authenticator.setDefault(authenticator);
		}

		if (proxyHost != null && proxyPort != null) {

			LOGGER.info("Initializing Proxy {}:{}", proxyHost, proxyPort);
			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort.intValue()));
		}

		if (!disableSSLcheck) {

			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			try {
				SSLContext sc = SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			} catch (NoSuchAlgorithmException | KeyManagementException e) {
				LOGGER.error("Failed to set authentication cert trust", e);
			}

			HostnameVerifier allHostsValid = new HostnameVerifier() {

				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		}

		if (this.waitJavascript) {

			if (StringUtils.isEmpty(chromeDriver)) {

				Builder settings = Settings.builder().timezone(Timezone.EUROPE_BRUSSELS)
						.connectTimeout(this.timeout.intValue())
						.maxConnections(50)
						.quickRender(true)
						.blockMedia(true)
						.userAgent(UserAgent.CHROME)
						.logger("ch.qos.logback.core.ConsoleAppender")
						.processes(2)
						.loggerLevel(java.util.logging.Level.INFO)
						.hostnameVerification(false);

				if (proxyUser != null && proxyPass != null && proxyPort != null) {

					ProxyConfig proxyConfig = new ProxyConfig(Type.HTTP, proxyHost, proxyPort.intValue(), proxyUser, proxyPass);
					settings.proxy(proxyConfig);
					settings.javaOptions("-Djdk.http.auth.tunneling.disabledSchemes=");
				}

				driver = new JBrowserDriver(settings.build());

			} else {

				Path chrome = Paths.get(chromeDriver);
				Boolean isExecutable = chrome.toFile().setExecutable(true);

				if (isExecutable) {
					LOGGER.info("set {} to be executable", chromeDriver);
				}

				System.setProperty("webdriver.chrome.driver", chrome.toAbsolutePath().toString());

				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.addArguments("--headless");
				chromeOptions.addArguments("--no-sandbox");
				chromeOptions.addArguments("--disable-dev-shm-usage");

				driver = new ChromeDriver(chromeOptions);

				// https://github.com/seleniumhq/selenium-google-code-issue-archive/issues/27
				((JavascriptExecutor) driver).executeScript("window.alert = function(msg) { }");
				((JavascriptExecutor) driver).executeScript("window.confirm = function(msg) { }");

			}

		}
	}

	private void deleteOldDocuments(Consumer<Map<String, Object>> consumer, String dataFolder, String id) {

		Path indexPath = Paths.get(new StringBuilder(dataFolder).append("/fetched-data/").append(id).toString());

		if (!indexPath.toFile().exists()) {
			indexPath.toFile().mkdirs();
		}

		Path pathFile = Paths.get(new StringBuilder(dataFolder).append("/fetched-data/").append(id).append("_tmp.txt").toString());

		if (pathFile.toFile().exists()) {

			try {
				List<String> legacyFile = Arrays.asList(objectMapper.readValue(pathFile.toFile(), String[].class));
				List<String> urlsForDeletion = legacyFile.stream().filter(l -> !tmpList.contains(l)).collect(Collectors.toList());

				LOGGER.info("Thread deleting {}, deleting {} urls", threadId, urlsForDeletion.size());

				urlsForDeletion.parallelStream().forEach(url -> {

					String reference = Base64.getEncoder().encodeToString(url.getBytes());
					LOGGER.info("Thread Sending Deletion {} , reference {}", threadId, reference);

					Map<String, Object> metadata = new HashMap<>();
					metadata.put(METADATA_REFERENCE, reference);
					metadata.put(METADATA_COMMAND, Command.DELETE.toString());

					consumer.accept(metadata);

				});

				Files.delete(pathFile);

			} catch (IOException e) {
				LOGGER.warn("Failed to read legacy file", e);
			}
		}

		try {
			String json = objectMapper.writeValueAsString(tmpList);
			Files.write(pathFile, json.getBytes());
		} catch (IOException e) {
			LOGGER.warn("Failed to write tmp file to disk {}", pathFile, e);
		}

	}

	private HttpURLConnection getHttpConnection(URL url) throws IOException {

		HttpURLConnection httpURLConnection;

		if (proxy == null) {
			httpURLConnection = (HttpURLConnection) url.openConnection();
		} else {
			httpURLConnection = (HttpURLConnection) url.openConnection(proxy);
		}

		httpURLConnection.setConnectTimeout(timeout.intValue());
		httpURLConnection.setReadTimeout(timeout.intValue());
		httpURLConnection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
		httpURLConnection.addRequestProperty("User-Agent", "Mozilla");
		httpURLConnection.addRequestProperty("Referer", "google.com");

		return httpURLConnection;
	}

	private void extractRobot(String url, HttpURLConnection urlConnection, int code, String message) {

		try (InputStream inputStream = urlConnection.getInputStream()) {

			LOGGER.info("Read url, status {}, {}, {}", code, url, message);

			byte[] content = IOUtils.toByteArray(inputStream);
			urlConnection.disconnect();

			Map<String, Object> metadata = new HashMap<>();
			metadata.put(METADATA_REFERENCE, ROBOT);
			metadata.put(METADATA_CONTENT, Base64.getEncoder().encodeToString(content));

		} catch (Exception e) {
			LOGGER.error("Failed to extract robot.txt from root context", e);
		}
	}

	private void extractUrl(Consumer<Map<String, Object>> consumer, String urlStringTmp, String rootUrl, Long depth) {

		String urlString = urlStringTmp;

		try {

			if (maxPages != 0 && maxPagesCount >= maxPages) {
				return;
			}

			urlString = getUrlString(urlStringTmp, rootUrl);
			String uuid = UUID.randomUUID().toString();

			if (tmpList.contains(urlString)) {
				return;
			} else {
				tmpList.add(urlString);
			}

			Map<String, Object> metadata = new HashMap<>();

			URL url = new URL(urlString);
			HttpURLConnection urlConnection = getHttpConnection(url);
			urlConnection.connect();

			int code = urlConnection.getResponseCode();
			String message = urlConnection.getResponseMessage();

			if (code == HttpURLConnection.HTTP_OK) {

				parseContent(consumer, rootUrl, depth, urlString, uuid, metadata, url, urlConnection, code, message);

			} else if (code == HttpURLConnection.HTTP_MOVED_TEMP || code == HttpURLConnection.HTTP_MOVED_PERM || code == HttpURLConnection.HTTP_SEE_OTHER) {

				String newUrl = urlConnection.getHeaderField("Location");

				if (!newUrl.startsWith(rootUrl)) {
					LOGGER.debug("Not redirecting to external url  {}", newUrl);
				} else {
					LOGGER.debug("Redirect needed to :  {}", newUrl);
					extractUrl(consumer, newUrl, rootUrl, depth);
				}

			} else {
				LOGGER.warn("Failed To Read Thread {}, status {}, pages {}, depth {}, url {}, message {}", threadId, code, maxPagesCount, depth, url, message);
			}

			urlConnection.disconnect();

		} catch (SocketTimeoutException e) {

			LOGGER.warn("Thread Timeout {}, url {}, sleeping and trying again", threadId, urlString);

			try {

				Thread.sleep(3000);
				extractUrl(consumer, urlString, rootUrl, depth);

			} catch (InterruptedException e1) {
				Thread.currentThread().interrupt();
			}

		} catch (Exception e) {
			LOGGER.error("Failed to retrieve URL from thread {}, url {}", threadId, urlString, e);
		}

	}

	private void parseContent(Consumer<Map<String, Object>> consumer, String rootUrl, Long depth, String urlString, String uuid, Map<String, Object> metadata, URL url, HttpURLConnection urlConnection, int code, String message) throws IOException {

		Map<String, List<String>> headers = urlConnection.getHeaderFields();
		List<String> childPages = new ArrayList<>();

		try (InputStream inputStream = urlConnection.getInputStream()) {

			byte[] bytes = IOUtils.toByteArray(inputStream);
			childPages = extractContent(consumer, rootUrl, urlString, uuid, url, code, message, headers, depth, bytes);

		} catch (Exception e1) {

			LOGGER.warn("Failed Thread {}, status {}, pages {}, depth {}, url {}, message {}", threadId, code, maxPagesCount, depth, url, message, e1);

		}

		Optional.ofNullable(childPages).orElse(new ArrayList<>()).stream().forEach(childUrl -> executorService.execute(() -> extractUrl(consumer, childUrl, rootUrl, depth + 1)));

	}

	@SuppressWarnings("unchecked")
	private List<String> extractContent(Consumer<Map<String, Object>> consumer, String rootUrl, String urlString, String uuid, URL url, int code, String message, Map<String, List<String>> headers, Long depth, byte[] bytes) throws IOException {

		Map<String, Object> metadata = parseHtml(urlString, rootUrl, headers, bytes, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC), uuid);

		if (excludedDataRegex.stream().noneMatch(ex -> urlString.matches(ex))) {

			maxPagesCount++;
			consumer.accept(metadata);
			LOGGER.info("Thread {}, status {}, pages {}, depth {}, url {}, message {}", threadId, code, maxPagesCount, depth, url, message);

		} else {
			LOGGER.info("Excluded Thread {}, status {}, pages {}, depth {}, url {}, message {}", threadId, code, maxPagesCount, depth, url, message);
		}

		List<String> childPages = (List<String>) metadata.get(METADATA_CHILD);
		Long newDepth = depth + 1;

		if (maxDepth == 0 || newDepth <= maxDepth) {
			return childPages;
		}

		return new ArrayList<>();

	}

	private String getUrlString(String urlString, String rootUrl) throws MalformedURLException {

		urlString = urlString.trim();

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

		if (urlString.endsWith("/") && !urlString.equals(rootUrl)) {
			urlString = urlString.substring(0, urlString.lastIndexOf('/'));
		}

		return urlString;
	}

	private Map<String, Object> parseHtml(String urlString, String rootUrl, Map<String, List<String>> headers, byte[] bytes, Long epochSecond, String uuid) throws IOException {

		Map<String, Object> metadata = new HashMap<>();
		metadata.put(METADATA_REFERENCE, Base64.getEncoder().encodeToString(urlString.getBytes()));
		metadata.put(METADATA_CONTENT, Base64.getEncoder().encodeToString(bytes));
		metadata.put(METADATA_EPOCH, epochSecond);
		metadata.put(METADATA_URL, urlString);
		metadata.put(METADATA_UUID, uuid);
		metadata.put(METADATA_STATUS, 200);
		metadata.put(METADATA_CONTEXT, rootUrl);
		metadata.put(METADATA_COMMAND, Command.ADD.toString());

		headers.entrySet().stream().filter(entry -> entry.getKey() != null).forEach(entry -> metadata.put(entry.getKey(), entry.getValue()));

		if (headers.containsKey("Content-Type") && headers.get("Content-Type").get(0).contains("html")) {

			String bodyHtml = null;

			if (waitJavascript) {

				driver.get(urlString);
				bodyHtml = driver.getPageSource();

				metadata.put(METADATA_CONTENT, Base64.getEncoder().encodeToString(bodyHtml.getBytes()));

			} else {
				bodyHtml = IOUtils.toString(bytes, StandardCharsets.UTF_8.name());
			}

			org.jsoup.nodes.Document document = Jsoup.parse(bodyHtml);
			Elements elements = document.getElementsByAttribute("href");

			String simpleUrlString = getSimpleUrl(rootUrl);

			List<String> childPages = elements.stream().map(e -> e.attr("href"))
					.filter(href -> (!href.startsWith(HTTP) && !href.startsWith(HTTPS) && !href.startsWith("mailto") && !href.startsWith("javascript") && !href.endsWith(".css") && !href.endsWith(".js")) || href.startsWith(HTTP + simpleUrlString) || href.startsWith(HTTPS + simpleUrlString))
					.filter(href -> !href.equals("/") && !href.startsWith("//"))
					.filter(href -> excludedLinkRegex.stream().noneMatch(ex -> href.matches(ex)))
					.sorted()
					.collect(Collectors.toList());

			List<String> externalPages = elements.stream().map(e -> e.attr("href")).filter(href -> (href.startsWith(HTTP) || href.startsWith(HTTPS)) && !href.startsWith(HTTP + simpleUrlString) && !href.startsWith(HTTPS + simpleUrlString)).collect(Collectors.toList());

			metadata.put(METADATA_CHILD, new ArrayList<>(new HashSet<>(childPages)));
			metadata.put(METADATA_EXTERNAL, new ArrayList<>(new HashSet<>(externalPages)));

		}

		return metadata;
	}

	private String getSimpleUrl(String urlString) {

		try {

			URL url = new URL(urlString);

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(url.getHost());

			if (url.getPort() > 0) {
				stringBuilder.append(url.getPort());
			}

			String path = url.getPath();
			if (path.startsWith("/") && path.length() > 2) {
				path = path.substring(1, path.length());
			}

			if (path.split("/").length > 1) {
				stringBuilder.append("/").append(path.substring(0, path.indexOf('/')));
			}

			return stringBuilder.toString();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return urlString;
	}

}

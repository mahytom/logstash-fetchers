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

import eu.wajja.web.fetcher.model.Result;

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
	private String crawlerUserAgent;
	private String crawlerReferer;
	private Long maxPagesCount = 0l;
	private Long excludedLinkPagesCount = 0l;
	private Long excludedDataPagesCount = 0l;
	private Long threads;
	private Proxy proxy = null;
	private WebDriver driver;
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
		this.crawlerUserAgent = dataMap.getString(WebFetcher.PROPERTY_CRAWLER_USER_AGENT);
		this.crawlerReferer = dataMap.getString(WebFetcher.PROPERTY_CRAWLER_REFERER);

		initializeConnection(dataMap.getString(WebFetcher.PROPERTY_PROXY_USER),
				dataMap.getString(WebFetcher.PROPERTY_PROXY_PASS),
				dataMap.getString(WebFetcher.PROPERTY_PROXY_HOST),
				dataMap.getLong(WebFetcher.PROPERTY_PROXY_PORT),
				dataMap.getBoolean(WebFetcher.PROPERTY_SSL_CHECK));

		threads = dataMap.getLong(WebFetcher.PROPERTY_THREADS);

		LOGGER.info("############Starting fetch for thread : {}, url : {}##################################", threadId, url);
        LOGGER.info();

		String id = Base64.getEncoder().encodeToString(url.getBytes());

		try {

			URL robotUrl = new URL(url + "/" + ROBOT);
			Result result = getURL(robotUrl, url, proxy, timeout, maxPagesCount, 1l, crawlerUserAgent, crawlerReferer);

			if (result.getContent() != null) {

				Map<String, Object> metadata = new HashMap<>();
				metadata.put(METADATA_REFERENCE, ROBOT);
				metadata.put(METADATA_CONTENT, Base64.getEncoder().encodeToString(result.getContent()));

			} else {
				LOGGER.warn("Failed to read robot.txt url, status {}, {}, {}", result.getCode(), url, result.getMessage());
			}

			extractUrl(consumer, url, url, 0l);
			deleteOldDocuments(consumer, dataFolder, id);

		} catch (Exception e) {
			LOGGER.error("Failed to read robot", e);

		}

		if (this.waitJavascript) {
			LOGGER.info("Closing driver {}", url);
			this.driver.quit();
		}

		LOGGER.info("##############################Finished Thread {}, for url: {}##########################", threadId, url);
        LOGGER.info("Downloaded {} documents for the {} job", maxPagesCount,url)

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

	private void extractUrl(Consumer<Map<String, Object>> consumer, String urlStringTmp, String rootUrl, Long depth) {

		String urlString = null;

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

			Result result = getURL(new URL(urlString), rootUrl, proxy, timeout, maxPagesCount, depth, this.crawlerUserAgent, this.crawlerReferer);

			if (result.getContent() != null) {

				List<String> childPages = extractContent(consumer, result, uuid, depth);
				Optional.ofNullable(childPages).orElse(new ArrayList<>()).stream().forEach(childUrl -> extractUrl(consumer, childUrl, result.getRootUrl(), depth + 1));

			}

		} catch (Exception e) {
			LOGGER.error("Failed to retrieve URL from thread {}, url {}", threadId, urlString, e);
		}

	}

	public static Result getURL(URL url, String rootUrl, Proxy proxy, Long timeout, Long maxPagesCount, Long depth, String userAgent, String referer) {

		HttpURLConnection httpURLConnection = null;
		Result result = new Result();

		try {

			if (proxy == null) {
				httpURLConnection = (HttpURLConnection) url.openConnection();
			} else {
				httpURLConnection = (HttpURLConnection) url.openConnection(proxy);
			}

			httpURLConnection.setConnectTimeout(timeout.intValue());
			httpURLConnection.setReadTimeout(timeout.intValue());
			httpURLConnection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
			httpURLConnection.addRequestProperty("User-Agent", userAgent);
			httpURLConnection.addRequestProperty("Referer", referer);

			httpURLConnection.connect();

			int code = httpURLConnection.getResponseCode();
			String message = httpURLConnection.getResponseMessage();
			result.setCode(code);
			result.setMessage(message);
			result.setUrl(url.toString());
			result.setRootUrl(rootUrl);

			if (code == HttpURLConnection.HTTP_OK) {

				InputStream inputStream = httpURLConnection.getInputStream();
				byte[] content = IOUtils.toByteArray(inputStream);

				result.setContent(content);
				result.setHeaders(httpURLConnection.getHeaderFields());

			} else if (code == HttpURLConnection.HTTP_MOVED_TEMP || code == HttpURLConnection.HTTP_MOVED_PERM || code == HttpURLConnection.HTTP_SEE_OTHER) {

				String newUrl = httpURLConnection.getHeaderField("Location");

				if (!newUrl.startsWith(rootUrl)) {
					LOGGER.debug("Not redirecting to external url  {}", newUrl);
				} else {
					LOGGER.debug("Redirect needed to :  {}", newUrl);
					return getURL(new URL(newUrl), rootUrl, proxy, timeout, maxPagesCount, depth, userAgent, referer);
				}

			} else {
				LOGGER.warn("Failed To Read status {}, pages {}, depth {}, url {}, message {}", code, maxPagesCount, depth, url, message);
			}

		} catch (SocketTimeoutException e) {

			LOGGER.warn("Thread url {}, sleeping and trying again", url);

			try {

				Thread.sleep(3000);
				return getURL(url, rootUrl, proxy, timeout, maxPagesCount, depth, userAgent, referer);

			} catch (InterruptedException e1) {
				Thread.currentThread().interrupt();
			}

		} catch (Exception e) {
			LOGGER.error("Failed to retrieve URL url {}", url.toString(), e);
		} finally {

			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	private List<String> extractContent(Consumer<Map<String, Object>> consumer, Result result, String uuid, Long depth) throws IOException {

		byte[] bytes = result.getContent();
		Map<String, List<String>> headers = result.getHeaders();

		Map<String, Object> metadata = new HashMap<>();
		metadata.put(METADATA_REFERENCE, Base64.getEncoder().encodeToString(result.getUrl().getBytes()));
		metadata.put(METADATA_CONTENT, Base64.getEncoder().encodeToString(bytes));
		metadata.put(METADATA_EPOCH, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
		metadata.put(METADATA_URL, result.getUrl());
		metadata.put(METADATA_UUID, uuid);
		metadata.put(METADATA_STATUS, 200);
		metadata.put(METADATA_CONTEXT, result.getRootUrl());
		metadata.put(METADATA_COMMAND, Command.ADD.toString());

		headers.entrySet().stream().filter(entry -> entry.getKey() != null).forEach(entry -> metadata.put(entry.getKey(), entry.getValue()));

		if (headers.containsKey("Content-Type") && headers.get("Content-Type").get(0).contains("html")) {

			String bodyHtml = null;

			if (waitJavascript) {

				driver.get(result.getUrl());
				bodyHtml = driver.getPageSource();

				metadata.put(METADATA_CONTENT, Base64.getEncoder().encodeToString(bodyHtml.getBytes()));

			} else {
				bodyHtml = IOUtils.toString(bytes, StandardCharsets.UTF_8.name());
			}

			org.jsoup.nodes.Document document = Jsoup.parse(bodyHtml);
			Elements elements = document.getElementsByAttribute("href");

			String simpleUrlString = getSimpleUrl(result.getRootUrl());

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

		if (excludedDataRegex.stream().noneMatch(ex -> result.getUrl().matches(ex))) {

			maxPagesCount++;
			consumer.accept(metadata);
			LOGGER.info("Accepting URL for further processing : {} - Thread {}, status {}, pages {}, depth {},  message {}, rootUrl {}, size {}, tmpList {}", result.getUrl(), threadId, result.getCode(), maxPagesCount, depth,  result.getMessage(), result.getRootUrl(), metadata.get(METADATA_CONTENT).toString().length(), tmpList.size());

		} else {
            excludedDataPagesCount++;
			LOGGER.info("This url is excluded by the excludedDataRegex : url {}, Thread {}, status {}, pages {}, depth {},  message {}, rootUrl {}, size {}, tmpList {}", result.getUrl(),threadId, result.getCode(), maxPagesCount, depth,  result.getMessage(), result.getRootUrl(), result.getContent().length, tmpList.size());
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
			LOGGER.error("MalformedURLException", e);
		}

		return urlString;
	}

}

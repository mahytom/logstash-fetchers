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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
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
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.ProxyConfig;
import com.machinepublishers.jbrowserdriver.ProxyConfig.Type;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.Settings.Builder;
import com.machinepublishers.jbrowserdriver.Timezone;
import com.machinepublishers.jbrowserdriver.UserAgent;

import co.elastic.logstash.api.Configuration;
import co.elastic.logstash.api.Context;
import co.elastic.logstash.api.Input;
import co.elastic.logstash.api.LogstashPlugin;
import co.elastic.logstash.api.PluginConfigSpec;

/**
 * Simple tool to fetch http content and send it to logstash
 * 
 * @author mahytom
 *
 */
@LogstashPlugin(name = "webfetcher")
public class WebFetcher implements Input {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebFetcher.class);

	public static final String PROPERTY_URLS = "urls";
	public static final String PROPERTY_EXCLUDE = "exclude";
	public static final String PROPERTY_DATAFOLDER = "dataFolder";
	public static final String PROPERTY_THREADS = "threads";
	public static final String PROPERTY_TIMEOUT = "timeout";
	public static final String PROPERTY_MAX_DEPTH = "maxdepth";
	public static final String PROPERTY_MAX_PAGES = "maxpages";
	public static final String PROPERTY_JAVASCRIPT = "waitJavascript";
	public static final String PROPERTY_SSL_CHECK = "sslcheck";
	public static final String PROPERTY_REFRESH_INTERVAL = "refreshInterval";
	public static final String PROPERTY_PROXY_HOST = "proxyHost";
	public static final String PROPERTY_PROXY_PORT = "proxyPort";
	public static final String PROPERTY_PROXY_USER = "proxyUser";
	public static final String PROPERTY_PROXY_PASS = "proxyPass";

	public static final PluginConfigSpec<List<Object>> CONFIG_URLS = PluginConfigSpec.arraySetting(PROPERTY_URLS);
	public static final PluginConfigSpec<List<Object>> CONFIG_EXCLUDE = PluginConfigSpec.arraySetting(PROPERTY_EXCLUDE, Arrays.asList(".css", ".js"), false, false);
	public static final PluginConfigSpec<String> CONFIG_DATA_FOLDER = PluginConfigSpec.stringSetting(PROPERTY_DATAFOLDER);
	public static final PluginConfigSpec<Long> CONFIG_THREAD_POOL_SIZE = PluginConfigSpec.numSetting(PROPERTY_THREADS, 10);
	public static final PluginConfigSpec<Long> CONFIG_TIMEOUT = PluginConfigSpec.numSetting(PROPERTY_TIMEOUT, 8000);
	public static final PluginConfigSpec<Long> CONFIG_MAX_DEPTH = PluginConfigSpec.numSetting(PROPERTY_MAX_DEPTH, 0);
	public static final PluginConfigSpec<Long> CONFIG_MAX_PAGES = PluginConfigSpec.numSetting(PROPERTY_MAX_PAGES, 0);
	public static final PluginConfigSpec<Boolean> CONFIG_WAIT_JAVASCRIPT = PluginConfigSpec.booleanSetting(PROPERTY_JAVASCRIPT, false);
	public static final PluginConfigSpec<Boolean> CONFIG_DISABLE_SSL_CHECK = PluginConfigSpec.booleanSetting(PROPERTY_SSL_CHECK, true);
	public static final PluginConfigSpec<Long> CONFIG_REFRESH_INTERVAL = PluginConfigSpec.numSetting(PROPERTY_REFRESH_INTERVAL, 86400l);
	public static final PluginConfigSpec<String> PROXY_HOST = PluginConfigSpec.stringSetting(PROPERTY_PROXY_HOST);
	public static final PluginConfigSpec<Long> PROXY_PORT = PluginConfigSpec.numSetting(PROPERTY_PROXY_PORT, 80);
	public static final PluginConfigSpec<String> PROXY_USER = PluginConfigSpec.stringSetting(PROPERTY_PROXY_USER);
	public static final PluginConfigSpec<String> PROXY_PASS = PluginConfigSpec.stringSetting(PROPERTY_PROXY_PASS);

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

	private ThreadPoolExecutor executorService = null;
	private StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
	private final CountDownLatch done = new CountDownLatch(1);
	protected volatile boolean stopped;

	private String threadId;
	private String dataFolder;
	private Long refreshSeconds;
	private Long maxDepth;
	private Long maxPages;
	private Long timeout;
	private Boolean waitJavascript;
	private List<String> urls;
	private List<String> excludedUrls;
	private Map<String, Long> maxPagesCount = new HashMap<>();
	private Proxy proxy = null;

	private String proxyHost;
	private Long proxyPort;
	private String proxyUser;
	private String proxyPass;
	private WebDriver driver;

	/**
	 * Mandatory constructor
	 * 
	 * @param id
	 * @param config
	 * @param context
	 */
	public WebFetcher(String id, Configuration config, Context context) {

		if (context != null && LOGGER.isDebugEnabled()) {
			LOGGER.debug(context.toString());
		}

		this.threadId = id;
		this.dataFolder = config.get(CONFIG_DATA_FOLDER);
		this.urls = config.get(CONFIG_URLS).stream().map(url -> (String) url).collect(Collectors.toList());
		this.excludedUrls = config.get(CONFIG_EXCLUDE).stream().map(url -> (String) url).collect(Collectors.toList());
		this.refreshSeconds = config.get(CONFIG_REFRESH_INTERVAL);
		this.maxDepth = config.get(CONFIG_MAX_DEPTH);
		this.maxPages = config.get(CONFIG_MAX_PAGES);
		this.timeout = config.get(CONFIG_TIMEOUT);
		this.waitJavascript = config.get(CONFIG_WAIT_JAVASCRIPT);
		this.proxyHost = config.get(PROXY_HOST);
		this.proxyPort = config.get(PROXY_PORT);
		this.proxyUser = config.get(PROXY_USER);
		this.proxyPass = config.get(PROXY_PASS);

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

		Boolean disableSSLcheck = config.get(CONFIG_DISABLE_SSL_CHECK);

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

		executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(config.get(CONFIG_THREAD_POOL_SIZE).intValue());

		Builder settings = Settings.builder().timezone(Timezone.EUROPE_BRUSSELS).connectTimeout(this.timeout.intValue()).maxConnections(50).quickRender(true).blockMedia(true).userAgent(UserAgent.CHROME).logger("ch.qos.logback.core.ConsoleAppender").processes(2)
				.loggerLevel(java.util.logging.Level.INFO).hostnameVerification(false);

		if (proxyUser != null && proxyPass != null && proxyPort != null) {

			ProxyConfig proxyConfig = new ProxyConfig(Type.HTTP, proxyHost, proxyPort.intValue(), proxyUser, proxyPass);
			settings.proxy(proxyConfig);
			settings.javaOptions("-Djdk.http.auth.tunneling.disabledSchemes=");
		}

		driver = new JBrowserDriver(settings.build());

	}

	@Override
	public void start(Consumer<Map<String, Object>> consumer) {

		try {

			while (!stopped) {

				Long startTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

				urls.parallelStream().forEach(url -> {

					LOGGER.info("Starting fetch for URL: {}", url);

					String id = Base64.getEncoder().encodeToString(url.getBytes());
					Path indexPath = Paths.get(new StringBuilder(dataFolder).append("/fetched-data/").append(id).toString());

					try {

						Directory directory = FSDirectory.open(indexPath);
						URL robotUrl = new URL(url + "/" + ROBOT);
						HttpURLConnection urlConnection = getHttpConnection(robotUrl);

						int code = urlConnection.getResponseCode();
						String message = urlConnection.getResponseMessage();

						if (code == HttpURLConnection.HTTP_OK) {
							extractRobot(robotUrl.toString(), directory, urlConnection, code, message);

						} else {

							LOGGER.warn("Failed to read robot.txt url, status {}, {}, {}", code, url, message);

							Map<String, Object> metadata = new HashMap<>();
							metadata.put(METADATA_REFERENCE, ROBOT);
							metadata.put(METADATA_CONTENT, Base64.getEncoder().encodeToString(ROBOT.getBytes()));

							writeDocumentToIndex(directory, metadata);
						}

						maxPagesCount.put(url, 0l);
						extractUrl(directory, consumer, url, url, 0l, startTime);

						while (executorService.getActiveCount() > 0) {
							Thread.sleep(1000);
						}

						List<String> documentsToDelete = getDocumentsToDelete(startTime, directory);

						documentsToDelete.stream().forEach(reference -> {

							LOGGER.info("Sending {} for deletion", reference);

							Map<String, Object> metadata = new HashMap<>();
							metadata.put(METADATA_REFERENCE, reference);
							metadata.put(METADATA_COMMAND, Command.DELETE);

							consumer.accept(metadata);

						});

						if (!documentsToDelete.isEmpty()) {
							deleteDocumentToIndex(directory, documentsToDelete);
						}

						directory.close();

					} catch (IOException | InterruptedException e1) {
						LOGGER.error("Failed to create data directory", e1);
						Thread.currentThread().interrupt();

					}

					LOGGER.info("Finished processing all url : {}", url);

				});

				Thread.sleep(refreshSeconds * 1000);
			}

		} catch (Exception e) {
			LOGGER.error("Failed", e);
		}

		maxPagesCount = new HashMap<>();
	}

	private List<String> getDocumentsToDelete(Long startTime, Directory directory) throws IOException {

		try (IndexReader indexReader = DirectoryReader.open(directory)) {

			IndexSearcher isearcher = new IndexSearcher(indexReader);
			Query query = LongPoint.newRangeQuery(METADATA_EPOCH + "_RANGE_QUERY", Long.MIN_VALUE, startTime);
			TopDocs topDocs = isearcher.search(query, 1000);

			return Arrays.asList(topDocs.scoreDocs).stream().map(tdoc -> {

				try {

					Document document = isearcher.doc(tdoc.doc);
					IndexableField indexableField = document.getFields().stream().filter(e -> e.name().equals(METADATA_REFERENCE)).findFirst().orElse(null);

					if (indexableField != null) {
						return indexableField.stringValue();
					}

				} catch (IOException e) {
					LOGGER.error("Failed to send document for deletion", e);
				}

				return tdoc.doc + "";

			}).collect(Collectors.toList());

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

	private void extractRobot(String url, Directory directory, HttpURLConnection urlConnection, int code, String message) {

		try (InputStream inputStream = urlConnection.getInputStream()) {

			LOGGER.info("Read url, status {}, {}, {}", code, url, message);

			byte[] content = IOUtils.toByteArray(inputStream);
			urlConnection.disconnect();

			Map<String, Object> metadata = new HashMap<>();
			metadata.put(METADATA_REFERENCE, ROBOT);
			metadata.put(METADATA_CONTENT, Base64.getEncoder().encodeToString(content));

			writeDocumentToIndex(directory, metadata);

		} catch (Exception e) {
			LOGGER.error("Failed to extract robot.txt from root context", e);
		}
	}

	private void extractUrl(Directory directory, Consumer<Map<String, Object>> consumer, String urlStringTmp, String rootUrl, Long depth, Long startTime) {

		String urlString = urlStringTmp;

		try {

			if (maxPages != 0 && maxPagesCount.get(rootUrl) >= maxPages) {
				return;
			}

			urlString = getUrlString(urlStringTmp, rootUrl);
			String uuid = UUID.randomUUID().toString();

			Document document = getDocument(directory, urlString);

			if (document != null) {

				Long epochValue = Long.decode(document.getField(METADATA_EPOCH).stringValue());
				if (epochValue != null && epochValue > startTime) {
					return;
				}

				uuid = document.get("uuid");
			}

			Map<String, Object> metadata = new HashMap<>();

			URL url = new URL(urlString);
			HttpURLConnection urlConnection = getHttpConnection(url);
			urlConnection.connect();

			int code = urlConnection.getResponseCode();
			String message = urlConnection.getResponseMessage();

			if (code == HttpURLConnection.HTTP_OK) {

				parseContent(directory, consumer, rootUrl, depth, startTime, urlString, uuid, metadata, url, urlConnection, code, message);

			} else if (code == HttpURLConnection.HTTP_MOVED_TEMP || code == HttpURLConnection.HTTP_MOVED_PERM || code == HttpURLConnection.HTTP_SEE_OTHER) {

				String newUrl = urlConnection.getHeaderField("Location");

				if (!newUrl.startsWith(rootUrl)) {
					LOGGER.warn("Not redirecting to external url  {}", newUrl);
				} else {
					LOGGER.info("Redirect needed to :  {}", newUrl);
					extractUrl(directory, consumer, newUrl, rootUrl, depth, startTime);
				}

			} else {

				LOGGER.error("Failed to read url, status {}, {}, {}", code, url, message);

				metadata.put(METADATA_REFERENCE, Base64.getEncoder().encodeToString(urlString.getBytes()));
				metadata.put(METADATA_EPOCH, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
				metadata.put(METADATA_URL, urlString);
				metadata.put(METADATA_UUID, uuid);
				metadata.put(METADATA_STATUS, code);
				metadata.put(METADATA_CONTEXT, urlString);

				writeDocumentToIndex(directory, metadata);
			}

			urlConnection.disconnect();

		} catch (SocketTimeoutException e) {

			LOGGER.warn("Url {} timeout, sleeping and trying again", urlString);

			try {

				Thread.sleep(3000);
				extractUrl(directory, consumer, urlString, rootUrl, depth, startTime);

			} catch (InterruptedException e1) {
				Thread.currentThread().interrupt();
			}

		} catch (Exception e) {
			LOGGER.error("Failed to retrieve URL: {}", urlString, e);
		}

	}

	private void parseContent(Directory directory, Consumer<Map<String, Object>> consumer, String rootUrl, Long depth, Long startTime, String urlString, String uuid, Map<String, Object> metadata, URL url, HttpURLConnection urlConnection, int code, String message) throws IOException {

		Map<String, List<String>> headers = urlConnection.getHeaderFields();

		try (InputStream inputStream = urlConnection.getInputStream()) {

			byte[] bytes = IOUtils.toByteArray(inputStream);
			inputStream.close();

			extractContent(directory, consumer, rootUrl, urlString, uuid, url, code, message, headers, depth, startTime, bytes);

		} catch (Exception e1) {

			LOGGER.error("Failed to read url, status {}, {}, {}", code, url, message, e1);

			metadata.put(METADATA_REFERENCE, Base64.getEncoder().encodeToString(urlString.getBytes()));
			metadata.put(METADATA_EPOCH, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
			metadata.put(METADATA_URL, urlString);
			metadata.put(METADATA_UUID, uuid);
			metadata.put(METADATA_STATUS, code);
			metadata.put(METADATA_CONTEXT, urlString);

			writeDocumentToIndex(directory, metadata);
		}
	}

	@SuppressWarnings("unchecked")
	private void extractContent(Directory directory, Consumer<Map<String, Object>> consumer, String rootUrl, String urlString, String uuid, URL url, int code, String message, Map<String, List<String>> headers, Long depth, Long startTime, byte[] bytes) throws IOException {

		maxPagesCount.put(rootUrl, maxPagesCount.get(rootUrl) + 1);
		Map<String, Object> metadata = parseHtml(urlString, rootUrl, headers, bytes, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC), uuid);
		consumer.accept(metadata);

		LOGGER.info("Read url, status {}, {}, {}, depth {}, pages {}", code, url, message, depth, maxPagesCount.get(rootUrl));
		writeDocumentToIndex(directory, metadata);

		List<String> childPages = (List<String>) metadata.get(METADATA_CHILD);
		Long newDepth = depth + 1;

		if (maxDepth == 0 || newDepth <= maxDepth) {
			Optional.ofNullable(childPages).orElse(new ArrayList<>()).parallelStream().filter(href -> excludedUrls.stream().noneMatch(ex -> href.matches(ex))).forEach(childUrl -> executorService.submit(() -> extractUrl(directory, consumer, childUrl, rootUrl, newDepth, startTime)));
		}

	}

	private synchronized Document getDocument(Directory directory, String url) throws IOException {

		if (url != null) {

			try (IndexReader indexReader = DirectoryReader.open(directory)) {

				IndexSearcher isearcher = new IndexSearcher(indexReader);
				Query query = new TermQuery(new Term(METADATA_URL, url));
				TopDocs topDocs = isearcher.search(query, 1);

				if (topDocs.totalHits.value > 0) {

					ScoreDoc doc = topDocs.scoreDocs[0];
					return isearcher.doc(doc.doc);
				}

			}

		}

		return null;

	}

	private synchronized void writeDocumentToIndex(Directory directory, Map<String, Object> metadata) throws IOException {

		String url = (String) metadata.get(METADATA_URL);
		Document oldDocument = getDocument(directory, url);

		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(standardAnalyzer);
		Document document = new Document();

		try (IndexWriter iwriter = new IndexWriter(directory, indexWriterConfig)) {

			metadata.entrySet().stream().filter(e -> !e.getKey().equals(METADATA_CONTENT)).forEach(e -> {

				if (e.getValue() instanceof String) {
					document.add(new StringField(e.getKey(), (String) e.getValue(), Store.YES));

				} else if (e.getValue() instanceof Long) {
					document.add(new StringField(e.getKey(), ((Long) e.getValue()).toString(), Store.YES));
					document.add(new LongPoint(e.getKey() + "_RANGE_QUERY", (Long) e.getValue()));
				}
			});

			if (oldDocument == null) {
				iwriter.addDocument(document);
			} else {
				iwriter.updateDocument(new Term(METADATA_URL, url), document.getFields());
			}

			iwriter.flush();
			iwriter.commit();

		} catch (StackOverflowError | Exception e1) {
			LOGGER.error("Failed to write document to index", e1);
		}

	}

	private synchronized void deleteDocumentToIndex(Directory directory, List<String> references) throws IOException {

		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(standardAnalyzer);

		try (IndexWriter iwriter = new IndexWriter(directory, indexWriterConfig)) {

			for (String reference : references) {
				Query query = new TermQuery(new Term(METADATA_REFERENCE, reference));
				iwriter.deleteDocuments(query);
			}

		} catch (StackOverflowError | Exception e1) {
			LOGGER.error("Failed to write document to index", e1);
		}

	}

	private String getUrlString(String urlString, String rootUrl) throws MalformedURLException {

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
					.filter(href -> !href.equals("/") && !href.startsWith("//")).collect(Collectors.toList());

			List<String> externalPages = elements.stream().map(e -> e.attr("href")).filter(href -> (href.startsWith(HTTP) || href.startsWith(HTTPS)) && !href.startsWith(HTTP + simpleUrlString) && !href.startsWith(HTTPS + simpleUrlString)).collect(Collectors.toList());

			metadata.put(METADATA_CHILD, new ArrayList<>(new HashSet<>(childPages)));
			metadata.put(METADATA_EXTERNAL, new ArrayList<>(new HashSet<>(externalPages)));

		}

		return metadata;
	}

	private String getSimpleUrl(String urlString) {

		String simpleUrlString = urlString.replace(HTTP, "").replace(HTTPS, "");

		if (simpleUrlString.contains("?")) {
			return simpleUrlString.substring(0, simpleUrlString.indexOf('?'));
		}

		return simpleUrlString;
	}

	@Override
	public void stop() {
		driver.quit();
		stopped = true;
	}

	@Override
	public void awaitStop() throws InterruptedException {
		driver.quit();
		done.await();
	}

	/**
	 * Returns a list of all configuration
	 */
	@Override
	public Collection<PluginConfigSpec<?>> configSchema() {
		return Arrays.asList(CONFIG_URLS, CONFIG_DATA_FOLDER, CONFIG_DISABLE_SSL_CHECK, CONFIG_EXCLUDE, CONFIG_THREAD_POOL_SIZE, CONFIG_REFRESH_INTERVAL, PROXY_HOST, PROXY_PASS, PROXY_PORT, PROXY_USER, CONFIG_MAX_DEPTH, CONFIG_TIMEOUT, CONFIG_MAX_PAGES, CONFIG_WAIT_JAVASCRIPT);
	}

	@Override
	public String getId() {
		return this.threadId;
	}
}

package eu.wajja.web.fetcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.Timezone;

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

	public static final PluginConfigSpec<List<Object>> CONFIG_URLS = PluginConfigSpec.arraySetting("urls");
	public static final PluginConfigSpec<List<Object>> CONFIG_EXCLUDE = PluginConfigSpec.arraySetting("exclude", Arrays.asList(".css", ".png"), false, false);
	public static final PluginConfigSpec<String> CONFIG_DATA_FOLDER = PluginConfigSpec.stringSetting("dataFolder");
	public static final PluginConfigSpec<Long> CONFIG_THREAD_POOL_SIZE = PluginConfigSpec.numSetting("threads", 10);
	public static final PluginConfigSpec<Long> CONFIG_TIMEOUT = PluginConfigSpec.numSetting("timeout", 8000);
	public static final PluginConfigSpec<Long> CONFIG_MAX_DEPTH = PluginConfigSpec.numSetting("maxdepth", 0);
	public static final PluginConfigSpec<Long> CONFIG_MAX_PAGES = PluginConfigSpec.numSetting("maxpages", 0);
	public static final PluginConfigSpec<Boolean> CONFIG_WAIT_JAVASCRIPT = PluginConfigSpec.booleanSetting("waitJavascript", false);
	public static final PluginConfigSpec<Long> CONFIG_REFRESH_INTERVAL = PluginConfigSpec.numSetting("refreshInterval", 86400l);

	public static final PluginConfigSpec<String> PROXY_HOST = PluginConfigSpec.stringSetting("proxyHost");
	public static final PluginConfigSpec<Long> PROXY_PORT = PluginConfigSpec.numSetting("proxyPort", 80);
	public static final PluginConfigSpec<String> PROXY_USER = PluginConfigSpec.stringSetting("proxyUser");
	public static final PluginConfigSpec<String> PROXY_PASS = PluginConfigSpec.stringSetting("proxyPass");

	private static final String METADATA_EPOCH = "epochSecond";
	private static final String METADATA_REFERENCE = "reference";
	private static final String METADATA_CONTENT = "content";
	private static final String METADATA_URL = "url";
	private static final String METADATA_UUID = "uuid";
	private static final String METADATA_STATUS = "status";
	private static final String METADATA_CHILD = "childPages";
	private static final String METADATA_EXTERNAL = "externalPages";

	private static final String HTTP = "http://";
	private static final String HTTPS = "https://";
	private static final String ROBOT = "robot.txt";

	private ThreadPoolExecutor executorService = null;
	private StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
	private final CountDownLatch done = new CountDownLatch(1);
	private volatile boolean stopped;

	private String threadId;
	private Context context;
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

	/**
	 * Mandatory constructor
	 * 
	 * @param id
	 * @param config
	 * @param context
	 */
	public WebFetcher(String id, Configuration config, Context context) {

		this.threadId = id;
		this.context = context;
		this.dataFolder = config.get(CONFIG_DATA_FOLDER);
		this.urls = config.get(CONFIG_URLS).stream().map(url -> (String) url).collect(Collectors.toList());
		this.excludedUrls = config.get(CONFIG_EXCLUDE).stream().map(url -> (String) url).collect(Collectors.toList());
		this.refreshSeconds = config.get(CONFIG_REFRESH_INTERVAL);
		this.maxDepth = config.get(CONFIG_MAX_DEPTH);
		this.maxPages = config.get(CONFIG_MAX_PAGES);
		this.timeout = config.get(CONFIG_TIMEOUT);
		this.waitJavascript = config.get(CONFIG_WAIT_JAVASCRIPT);

		String proxyHost = config.get(PROXY_HOST);
		Long proxyPort = config.get(PROXY_PORT);
		String proxyUser = config.get(PROXY_USER);
		String proxyPass = config.get(PROXY_PASS);

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

		executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(config.get(CONFIG_THREAD_POOL_SIZE).intValue());
	}

	@Override
	public void start(Consumer<Map<String, Object>> consumer) {

		LocalDateTime refreshDateTime = LocalDateTime.now().minusSeconds(refreshSeconds);

		try {

			while (!stopped) {

				urls.parallelStream().forEach(url -> {

					LOGGER.info("Starting fetch for URL: {}", url);

					String id = Base64.getEncoder().encodeToString(url.getBytes());
					Path indexPath = Paths.get(new StringBuilder(dataFolder).append("/").append(id).append("_index").toString());

					try {

						Directory directory = FSDirectory.open(indexPath);
						URL robotUrl = new URL(url + "/robot.txt");
						HttpURLConnection urlConnection = getHttpConnection(robotUrl);

						int code = urlConnection.getResponseCode();
						String message = urlConnection.getResponseMessage();

						if (code == HttpURLConnection.HTTP_OK) {
							extractRobot(robotUrl.toString(), directory, urlConnection, code, message);

						} else {

							LOGGER.error("Failed to read robot.txt url, status {}, {}, {}", code, url, message);

							Map<String, Object> metadata = new HashMap<>();
							metadata.put(METADATA_REFERENCE, ROBOT);
							metadata.put(METADATA_CONTENT, Base64.getEncoder().encodeToString(ROBOT.getBytes()));

							writeDocumentToIndex(directory, metadata);
						}

						maxPagesCount.put(url, 0l);
						extractUrl(directory, consumer, url, url, refreshDateTime, 0l);

						while (executorService.getActiveCount() > 0) {
							Thread.sleep(1000);
						}

						directory.close();

					} catch (IOException | InterruptedException e1) {
						LOGGER.error("Failed to create data directory", e1);
					}

				});

				LOGGER.info("finished processing all urls");
				Thread.sleep(30000);
			}

		} catch (Exception e) {
			LOGGER.error("Failed", e);
		}

		maxPagesCount = new HashMap<>();
	}

	private HttpURLConnection getHttpConnection(URL url) throws IOException {

		HttpURLConnection httpURLConnection;

		if (proxy == null) {
			httpURLConnection = (HttpURLConnection) url.openConnection();
		} else {
			httpURLConnection = (HttpURLConnection) url.openConnection(proxy);
		}

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

	private void extractUrl(Directory directory, Consumer<Map<String, Object>> consumer, String urlString, String rootUrl, LocalDateTime refreshDateTime, Long depth) {

		try {

			if (maxPages != 0 && maxPagesCount.get(rootUrl) >= maxPages) {
				return;
			} else {
				maxPagesCount.put(rootUrl, maxPagesCount.get(rootUrl) + 1);
			}

			urlString = getUrlString(urlString, rootUrl);
			String uuid = UUID.randomUUID().toString();

			Document document = getDocument(directory, urlString);

			if (document != null) {

				IndexableField indexableField = document.getFields().stream().filter(e -> e.name().equals(METADATA_EPOCH)).findFirst().orElse(null);

				if (indexableField != null) {

					Long epochSecond = Long.valueOf(indexableField.stringValue());
					LocalDateTime indexTime = LocalDateTime.ofEpochSecond(epochSecond, 0, ZoneOffset.UTC);

					if (indexTime.isAfter(refreshDateTime)) {
						return;
					}

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

				Map<String, List<String>> headers = urlConnection.getHeaderFields();

				try (InputStream inputStream = urlConnection.getInputStream()) {

					extractContent(directory, consumer, rootUrl, refreshDateTime, urlString, uuid, url, code, message, headers, inputStream, depth);

				} catch (Exception e1) {

					LOGGER.error("Failed to read url, status {}, {}, {}", code, url, message, e1);

					metadata.put(METADATA_REFERENCE, Base64.getEncoder().encodeToString(urlString.getBytes()));
					metadata.put(METADATA_EPOCH, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
					metadata.put(METADATA_URL, urlString);
					metadata.put(METADATA_UUID, uuid);
					metadata.put(METADATA_STATUS, code);

					writeDocumentToIndex(directory, metadata);
				}

			} else if (code == HttpURLConnection.HTTP_MOVED_TEMP || code == HttpURLConnection.HTTP_MOVED_PERM || code == HttpURLConnection.HTTP_SEE_OTHER) {

				String newUrl = urlConnection.getHeaderField("Location");
				LOGGER.info("Redirect needed to :  {}", newUrl);
				extractUrl(directory, consumer, newUrl, rootUrl, refreshDateTime, depth);

			} else {

				LOGGER.error("Failed to read url, status {}, {}, {}", code, url, message);

				metadata.put(METADATA_REFERENCE, Base64.getEncoder().encodeToString(urlString.getBytes()));
				metadata.put(METADATA_EPOCH, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
				metadata.put(METADATA_URL, urlString);
				metadata.put(METADATA_UUID, uuid);
				metadata.put(METADATA_STATUS, code);

				writeDocumentToIndex(directory, metadata);
			}

			urlConnection.disconnect();

		} catch (Exception e) {
			LOGGER.error("Failed to retrieve URL: {}", urlString, e);
		}

	}

	private void extractContent(Directory directory, Consumer<Map<String, Object>> consumer, String rootUrl, LocalDateTime refreshDateTime, String urlString, String uuid, URL url, int code, String message, Map<String, List<String>> headers, InputStream inputStream, Long depth) throws IOException {

		Map<String, Object> metadata;
		byte[] bytes = IOUtils.toByteArray(inputStream);
		inputStream.close();

		metadata = parseHtml(urlString, rootUrl, headers, bytes, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC), uuid);
		consumer.accept(metadata);

		LOGGER.info("Read url, status {}, {}, {}, depth {}, pages {}", code, url, message, depth, maxPagesCount.get(rootUrl));
		writeDocumentToIndex(directory, metadata);

		List<String> childPages = (List<String>) metadata.get(METADATA_CHILD);
		Long newDepth = depth + 1;

		if (maxDepth == 0 || newDepth <= maxDepth) {

			Optional.ofNullable(childPages).orElse(new ArrayList<>()).parallelStream().filter(href -> {

				return excludedUrls.stream().noneMatch(ex -> href.matches(ex));

			}).forEach(childUrl -> executorService.submit(() -> extractUrl(directory, consumer, childUrl, rootUrl, refreshDateTime, newDepth)));
		}

	}

	private synchronized Document getDocument(Directory directory, String url) throws IOException {

		try (IndexReader indexReader = DirectoryReader.open(directory)) {

			IndexSearcher isearcher = new IndexSearcher(indexReader);
			Query query = new TermQuery(new Term(METADATA_URL, url));
			TopDocs topDocs = isearcher.search(query, 1);

			if (topDocs.totalHits.value > 0) {

				ScoreDoc doc = topDocs.scoreDocs[0];
				return isearcher.doc(doc.doc);
			}

		}

		return null;

	}

	private synchronized void writeDocumentToIndex(Directory directory, Map<String, Object> metadata) throws IOException {

		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(standardAnalyzer);
		Document document = new Document();

		try (IndexWriter iwriter = new IndexWriter(directory, indexWriterConfig)) {

			metadata.entrySet().stream().filter(e -> !e.getKey().equals(METADATA_CONTENT)).forEach(e -> {

				if (e.getValue() instanceof String) {
					document.add(new StringField(e.getKey(), (String) e.getValue(), Store.YES));

				} else if (e.getValue() instanceof Long) {
					document.add(new StringField(e.getKey(), ((Long) e.getValue()).toString(), Store.YES));
				}
			});

			iwriter.addDocument(document);

		} catch (StackOverflowError | Exception e1) {
			LOGGER.error("Failed to write document to index", e1);
		}

	}

	private String getUrlString(String urlString, String rootUrl) throws MalformedURLException {

		if (!urlString.startsWith("http") && urlString.startsWith("/")) {

			URL urlRoot = new URL(rootUrl);
			String path = urlRoot.getPath();

			if (StringUtils.isEmpty(path) || path.equals("/")) {
				urlString = urlRoot + urlString;
			} else {
				urlString = rootUrl.replace(path, "") + urlString;
			}

		}

		if (!urlString.startsWith("http") && !urlString.startsWith("/")) {
			urlString = rootUrl + urlString;
		}

		if (urlString.contains("#")) {
			urlString = urlString.substring(0, urlString.indexOf('#'));
		}

		if (urlString.endsWith("/")) {
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

		headers.entrySet().stream().filter(entry -> entry.getKey() != null).forEach(entry -> metadata.put(entry.getKey(), entry.getValue()));

		if (headers.containsKey("Content-Type") && headers.get("Content-Type").get(0).contains("html")) {

			String bodyHtml = null;

			if (waitJavascript) {

				JBrowserDriver driver = new JBrowserDriver(Settings.builder().timezone(Timezone.EUROPE_BRUSSELS).build());
				driver.get(urlString);
				bodyHtml = driver.getPageSource();
				driver.quit();

				metadata.put(METADATA_CONTENT, Base64.getEncoder().encodeToString(bodyHtml.getBytes()));

			} else {
				bodyHtml = IOUtils.toString(bytes, "UTF-8");
			}

			org.jsoup.nodes.Document document = Jsoup.parse(bodyHtml);

			Elements elements = document.getElementsByAttribute("href");
			String simpleUrlString = getSimpleUrl(rootUrl);

			List<String> childPages = elements.stream().map(e -> e.attr("href")).filter(href -> href.startsWith("/") || href.startsWith(HTTP + simpleUrlString) || href.startsWith(HTTPS + simpleUrlString)).filter(href -> !href.equals("/") && !href.startsWith("//")).collect(Collectors.toList());

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
		stopped = true;
	}

	@Override
	public void awaitStop() throws InterruptedException {
		done.await();
	}

	/**
	 * Returns a list of all configuration
	 */
	@Override
	public Collection<PluginConfigSpec<?>> configSchema() {
		return Arrays.asList(CONFIG_URLS, CONFIG_DATA_FOLDER, CONFIG_EXCLUDE, CONFIG_THREAD_POOL_SIZE, CONFIG_REFRESH_INTERVAL, PROXY_HOST, PROXY_PASS, PROXY_PORT, PROXY_USER, CONFIG_MAX_DEPTH, CONFIG_TIMEOUT, CONFIG_MAX_PAGES, CONFIG_WAIT_JAVASCRIPT);
	}

	@Override
	public String getId() {
		return this.threadId;
	}
}

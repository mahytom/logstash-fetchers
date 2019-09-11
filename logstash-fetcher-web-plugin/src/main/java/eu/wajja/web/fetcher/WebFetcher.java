package eu.wajja.web.fetcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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
import java.util.Set;
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
	public static final PluginConfigSpec<Long> CONFIG_THRAD_POOL_SIZE = PluginConfigSpec.numSetting("threads", 10);
	public static final PluginConfigSpec<Long> CONFIG_REFRESH_INTERVAL = PluginConfigSpec.numSetting("refreshInterval", 30000000l);

	private ThreadPoolExecutor executorService = null;
	private StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
	private final CountDownLatch done = new CountDownLatch(1);
	private volatile boolean stopped;

	private String threadId;
	private Context context;
	private String dataFolder;
	private Long refreshSeconds;
	private List<String> urls;
	private List<String> excludedUrls;

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

		executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(config.get(CONFIG_THRAD_POOL_SIZE).intValue());
	}

	@Override
	public void start(Consumer<Map<String, Object>> consumer) {

		LocalDateTime refreshDateTime = LocalDateTime.now().minusSeconds(refreshSeconds);

		try {

			while (!stopped) {

				urls.stream().forEach(url -> {

					String id = Base64.getEncoder().encodeToString(url.getBytes());
					Path indexPath = Paths.get(dataFolder + "/" + id + "_index");

					try {

						Directory directory = FSDirectory.open(indexPath);
						HttpURLConnection urlConnection = (HttpURLConnection) new URL(url + "/robot.txt").openConnection();

						int code = urlConnection.getResponseCode();
						String message = urlConnection.getResponseMessage();

						if (code == 200) {
							extractRobot(url, directory, urlConnection, code, message);

						} else {

							LOGGER.error("Failed to read robot.txt url, status {}, {}, {}", code, url.toString(), message);

							Map<String, Object> metadata = new HashMap<>();
							metadata.put("reference", "robot.txt");
							metadata.put("content", StringUtils.EMPTY);

							writeDocumentToIndex(directory, metadata);
						}

						extractUrl(directory, consumer, url, url, refreshDateTime);

						while (executorService.getActiveCount() > 0) {
							Thread.sleep(1000);
							LOGGER.info("Waiting for threads to be processed");
						}

						directory.close();

					} catch (IOException | InterruptedException e1) {
						LOGGER.error("Failed to create data directory", e1);
					}

				});

				LOGGER.info("finished processing all urls");
				stopped = true;
			}

		} catch (Exception e) {
			LOGGER.error("Failed", e);
		} finally {
			stopped = true;
			done.countDown();
		}
	}

	private void extractRobot(String url, Directory directory, HttpURLConnection urlConnection, int code, String message) {
		try (InputStream inputStream = urlConnection.getInputStream()) {

			LOGGER.info("Read url, status {}, {}, {}", code, url, message);

			byte[] bytes = IOUtils.toByteArray(inputStream);
			String content = IOUtils.toString(bytes, "UTF-8");
			urlConnection.disconnect();

			Map<String, Object> metadata = new HashMap<>();
			metadata.put("reference", "robot.txt");
			metadata.put("content", content);

			writeDocumentToIndex(directory, metadata);

		} catch (Exception e) {
			LOGGER.error("Failed to extract robot.txt from root context", e);
		}
	}

	@SuppressWarnings("unchecked")
	private void extractUrl(Directory directory, Consumer<Map<String, Object>> consumer, String urlString_, String rootUrl, LocalDateTime refreshDateTime) {

		try {

			String urlString = getUrlString(urlString_);
			String uuid = UUID.randomUUID().toString();

			Document document = getDocument(directory, urlString);

			if (document != null) {

				IndexableField indexableField = document.getFields().stream().filter(e -> e.name().equals("epochSecond")).findFirst().orElse(null);
				Long epochSecond = Long.valueOf(indexableField.stringValue());
				LocalDateTime indexTime = LocalDateTime.ofEpochSecond(epochSecond, 0, ZoneOffset.UTC);

				if (indexTime.isAfter(refreshDateTime)) {
					LOGGER.info("Skiping url {}", urlString);
					return;
				}

				uuid = document.get("uuid");

			}

			Map<String, Object> metadata = new HashMap<>();

			URL url = new URL(urlString);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.connect();

			int code = urlConnection.getResponseCode();
			String message = urlConnection.getResponseMessage();

			if (code == 200) {

				Map<String, List<String>> headers = urlConnection.getHeaderFields();

				try (InputStream inputStream = urlConnection.getInputStream()) {

					byte[] bytes = IOUtils.toByteArray(inputStream);
					inputStream.close();

					metadata = parseHtml(urlString, headers, bytes, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC), uuid);
					consumer.accept(metadata);

					LOGGER.info("Read url, status {}, {}, {}", code, url, message);
					writeDocumentToIndex(directory, metadata);

					List<String> childPages = (List<String>) metadata.get("childPages");

					Optional.ofNullable(childPages).orElse(new ArrayList<>()).parallelStream().forEach(childUrl -> {

						executorService.submit(() -> {

							if (!childUrl.startsWith(urlString)) {
								extractUrl(directory, consumer, rootUrl + childUrl, rootUrl, refreshDateTime);
							} else {
								extractUrl(directory, consumer, childUrl, rootUrl, refreshDateTime);
							}

						});

					});

				} catch (Exception e1) {

					LOGGER.error("Failed to read url, status {}, {}, {}", code, url, message);

					metadata.put("reference", Base64.getEncoder().encodeToString(urlString.getBytes()));
					metadata.put("epochSecond", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
					metadata.put("url", urlString);
					metadata.put("uuid", uuid);
					metadata.put("status", code);

					writeDocumentToIndex(directory, metadata);
				}

			} else {

				LOGGER.error("Failed to read url, status {}, {}, {}", code, url, message);

				metadata.put("reference", Base64.getEncoder().encodeToString(urlString.getBytes()));
				metadata.put("epochSecond", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
				metadata.put("url", urlString);
				metadata.put("uuid", uuid);
				metadata.put("status", code);

				writeDocumentToIndex(directory, metadata);
			}

			urlConnection.disconnect();

		} catch (Exception e) {
			LOGGER.error("Not a valid URL", e);
		}

	}

	private synchronized Document getDocument(Directory directory, String url) throws IOException {

		try (IndexReader indexReader = DirectoryReader.open(directory)) {

			IndexSearcher isearcher = new IndexSearcher(indexReader);
			Query query = new TermQuery(new Term("url", url));
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

			metadata.entrySet().stream().filter(e -> !e.getKey().equals("content")).forEach(e -> {

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

	private String getUrlString(String urlString) {

		if (urlString.contains("?")) {
			urlString = urlString.substring(0, urlString.indexOf('?'));
		}

		if (urlString.contains("#")) {
			urlString = urlString.substring(0, urlString.indexOf('#'));
		}

		if (urlString.endsWith("/")) {
			urlString = urlString.substring(0, urlString.lastIndexOf('/'));
		}

		return urlString;
	}

	private Map<String, Object> parseHtml(String urlString, Map<String, List<String>> headers, byte[] bytes, Long epochSecond, String uuid) throws IOException {

		String bodyHtml = IOUtils.toString(bytes, "UTF-8");

		Map<String, Object> metadata = new HashMap<>();
		metadata.put("reference", Base64.getEncoder().encodeToString(urlString.getBytes()));
		metadata.put("content", bodyHtml);
		metadata.put("epochSecond", epochSecond);
		metadata.put("url", urlString);
		metadata.put("uuid", uuid);
		metadata.put("status", 200);

		headers.entrySet().stream().filter(entry -> entry.getKey() != null).forEach(entry -> {
			metadata.put(entry.getKey(), entry.getValue());
		});

		if (headers.containsKey("Content-Type") && headers.get("Content-Type").get(0).contains("html")) {

			org.jsoup.nodes.Document document = Jsoup.parse(bodyHtml);

			Elements elements = document.getElementsByAttribute("href");
			
			List<String> childPages = elements.stream().map(e -> e.attr("href"))
					.filter(href -> href.startsWith("/") || href.startsWith(urlString))
					.filter(href -> excludedUrls.stream()
							.noneMatch(ex -> href.contains(ex)))
					.collect(Collectors.toList());
			
			List<String> externalPages = elements.stream().map(e -> e.attr("href"))
					.filter(href -> href.startsWith("http") && !href.startsWith(urlString))
					.filter(href -> excludedUrls.stream()
							.noneMatch(ex -> href.contains(ex)))
					.collect(Collectors.toList());

			metadata.put("childPages", cleanUpList(childPages));
			metadata.put("externalPages", cleanUpList(externalPages));

		}

		return metadata;
	}

	private Object cleanUpList(List<String> pages) {

		Set<String> childPages = new HashSet<>(pages.stream().map(x -> getUrlString(x)).collect(Collectors.toList()));
		return new ArrayList<>(childPages);
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
		return Arrays.asList(CONFIG_URLS, CONFIG_DATA_FOLDER, CONFIG_EXCLUDE, CONFIG_THRAD_POOL_SIZE, CONFIG_THRAD_POOL_SIZE);
	}

	@Override
	public String getId() {
		return this.threadId;
	}
}

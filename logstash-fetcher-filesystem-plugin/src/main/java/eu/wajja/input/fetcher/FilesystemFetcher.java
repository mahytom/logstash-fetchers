package eu.wajja.input.fetcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.elastic.logstash.api.Configuration;
import co.elastic.logstash.api.Context;
import co.elastic.logstash.api.Input;
import co.elastic.logstash.api.LogstashPlugin;
import co.elastic.logstash.api.PluginConfigSpec;

@LogstashPlugin(name = "filesystemfetcher")
public class FilesystemFetcher implements Input {

	private static final Logger LOGGER = LoggerFactory.getLogger(FilesystemFetcher.class);

	public static final PluginConfigSpec<List<Object>> CONFIG_PATHS = PluginConfigSpec.arraySetting("paths");
	public static final PluginConfigSpec<List<Object>> CONFIG_EXCLUDE = PluginConfigSpec.arraySetting("exclude", new ArrayList<>(), false, false);
	public static final PluginConfigSpec<String> CONFIG_DATA_FOLDER = PluginConfigSpec.stringSetting("dataFolder");
	public static final PluginConfigSpec<Long> CONFIG_THREAD_POOL_SIZE = PluginConfigSpec.numSetting("threads", 10);

	private ThreadPoolExecutor executorService = null;
	private StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
	private final CountDownLatch done = new CountDownLatch(1);
	private volatile boolean stopped;

	private String threadId;
	private Context context;
	private String dataFolder;
	private List<String> paths;
	private List<String> excludedUrls;

	/**
	 * Mandatory constructor
	 * 
	 * @param id
	 * @param config
	 * @param context
	 */
	public FilesystemFetcher(String id, Configuration config, Context context) {

		this.threadId = id;
		this.context = context;
		this.paths = config.get(CONFIG_PATHS).stream().map(url -> (String) url).collect(Collectors.toList());
		this.dataFolder = config.get(CONFIG_DATA_FOLDER);
		this.excludedUrls = config.get(CONFIG_EXCLUDE).stream().map(url -> (String) url).collect(Collectors.toList());

		executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(config.get(CONFIG_THREAD_POOL_SIZE).intValue());
	}

	@Override
	public void start(Consumer<Map<String, Object>> consumer) {

		try {

			while (!stopped) {

				paths.parallelStream().forEach(path -> {

					try {

						Path dataPath = Paths.get(path);
						String id = Base64.getEncoder().encodeToString(dataPath.toString().getBytes());
						Path indexPath = Paths.get(dataFolder + "/" + id + "_index");
						Directory directory = FSDirectory.open(indexPath);

						writeDocumentToIndex(directory, new HashMap<>());

						File file = dataPath.toFile();
						Arrays.asList(file.listFiles()).parallelStream().forEach(f -> parseFile(directory, f, consumer));

					} catch (IOException e1) {
						LOGGER.error("Failed to create data directory", e1);
					}
				});

				stopped = true;
			}

		} catch (Exception e) {
			LOGGER.error("Failed", e);
		} finally {
			stopped = true;
			done.countDown();
		}

	}

	private void parseFile(Directory directory, File file, Consumer<Map<String, Object>> consumer) {

		String path = file.getAbsolutePath().toString();

		if (file.isDirectory()) {

			Arrays.asList(file.listFiles()).stream().forEach(f -> {

				executorService.submit(() -> {
					parseFile(directory, f, consumer);
				});
			});

		} else {

			if (excludedUrls.stream().noneMatch(ex -> file.getName().endsWith(ex))) {

				Document document = getDocument(directory, path);

				if (document == null || file.lastModified() > Long.parseLong(document.getValues("epochSecond")[0])) {

					LOGGER.info("Sending : " + path);

					Map<String, Object> metadata = new HashMap<>();

					try (FileInputStream fileInputStream = new FileInputStream(file)) {

						byte[] bytes = IOUtils.toByteArray(fileInputStream);

						metadata.put("reference", Base64.getEncoder().encodeToString(path.getBytes()));
						metadata.put("epochSecond", file.lastModified());
						metadata.put("path", path);
						metadata.put("content", bytes);

						consumer.accept(metadata);

						writeDocumentToIndex(directory, metadata);

					} catch (Exception e) {
						LOGGER.error("Failed", e);
					}

				} else {
					LOGGER.info("Already sent : " + path + " : " + file.lastModified());
				}

			} else {
				LOGGER.info("Excluded path : " + path);
			}

		}

	}

	private synchronized Document getDocument(Directory directory, String path) {

		try (IndexReader indexReader = DirectoryReader.open(directory)) {

			IndexSearcher isearcher = new IndexSearcher(indexReader);
			Query query = new TermQuery(new Term("reference", Base64.getEncoder().encodeToString(path.getBytes())));
			TopDocs topDocs = isearcher.search(query, 1);

			if (topDocs.totalHits.value > 0) {

				ScoreDoc doc = topDocs.scoreDocs[0];
				return isearcher.doc(doc.doc);
			}

		} catch (IOException e) {
			LOGGER.error("Failed", e);
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

	@Override
	public void stop() {
		stopped = true;
	}

	@Override
	public void awaitStop() throws InterruptedException {
		done.await();
	}

	/**
	 * Returs a list of all configuration
	 */
	@Override
	public Collection<PluginConfigSpec<?>> configSchema() {
		return Arrays.asList(CONFIG_PATHS, CONFIG_DATA_FOLDER, CONFIG_EXCLUDE, CONFIG_THREAD_POOL_SIZE);
	}

	@Override
	public String getId() {
		return this.threadId;
	}
}

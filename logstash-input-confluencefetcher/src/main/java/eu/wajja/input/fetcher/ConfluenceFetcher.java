package eu.wajja.input.fetcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import co.elastic.logstash.api.Configuration;
import co.elastic.logstash.api.Context;
import co.elastic.logstash.api.Input;
import co.elastic.logstash.api.LogstashPlugin;
import co.elastic.logstash.api.PluginConfigSpec;

/**
 * Fetches pages from a confluence page
 * 
 * @author mahytom
 *
 */
@LogstashPlugin(name = "confluencefetcher")
public class ConfluenceFetcher implements Input {

	private ThreadPoolExecutor threadPoolExecutor;

	public static final PluginConfigSpec<List<Object>> CONFIG_URLS = PluginConfigSpec.arraySetting("urls");
	public static final PluginConfigSpec<String> CONFIG_USERNAME = PluginConfigSpec.stringSetting("username");
	public static final PluginConfigSpec<String> CONFIG_PASSWORD = PluginConfigSpec.stringSetting("password");
	public static final PluginConfigSpec<String> CONFIG_DATA_FOLDER = PluginConfigSpec.stringSetting("dataFolder");
	public static final PluginConfigSpec<List<Object>> CONFIG_SPACES = PluginConfigSpec.arraySetting("spaces", new ArrayList<>(), false, false);
	public static final PluginConfigSpec<Long> CONFIG_PAGE_LIMIT = PluginConfigSpec.numSetting("pageLimit", 25);
	public static final PluginConfigSpec<Long> CONFIG_THREADS = PluginConfigSpec.numSetting("threads", 3);

	private static final HttpClient httpClient = HttpClientBuilder.create().build();
	
	private String id;
	private String username;
	private String password;
	private String dataFolder;
	private Integer pageLimit;
	private List<String> urls;
	private List<String> spaces;

	/**
	 * Mandatory constructor
	 * 
	 * @param id
	 * @param config
	 * @param context
	 */
	public ConfluenceFetcher(String id, Configuration config, Context context) {

		/**
		 * Initializing the available threads
		 */
		Integer threads = (config.get(CONFIG_THREADS)).intValue();
		this.threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads);

		/**
		 * Initializing all the properties
		 */
		this.id = id;
		this.username = config.get(CONFIG_USERNAME);
		this.password = config.get(CONFIG_PASSWORD);
		this.dataFolder = config.get(CONFIG_DATA_FOLDER);
		this.pageLimit = (config.get(CONFIG_PAGE_LIMIT)).intValue();
		this.spaces = config.get(CONFIG_SPACES).stream().map(spc -> (String) spc).collect(Collectors.toList());
		this.urls = config.get(CONFIG_URLS).stream().map(url -> (String) url).collect(Collectors.toList());
	}

	@Override
	public void start(Consumer<Map<String, Object>> consumer) {

		urls.stream().forEach(confluenceRestUrl -> {
			threadPoolExecutor.execute(new ConfluenceFetcherThread(consumer, confluenceRestUrl, username, password, dataFolder, pageLimit, spaces, httpClient));
		});

	}

	@Override
	public void stop() {
		threadPoolExecutor.shutdown();
	}

	@Override
	public void awaitStop() throws InterruptedException {
		threadPoolExecutor.shutdown();
	}

	/**
	 * Returs a list of all configuration
	 */
	@Override
	public Collection<PluginConfigSpec<?>> configSchema() {
		return Arrays.asList(CONFIG_URLS, CONFIG_USERNAME, CONFIG_DATA_FOLDER, CONFIG_PASSWORD, CONFIG_SPACES, CONFIG_PAGE_LIMIT, CONFIG_THREADS);
	}

	@Override
	public String getId() {
		return this.id;
	}
}

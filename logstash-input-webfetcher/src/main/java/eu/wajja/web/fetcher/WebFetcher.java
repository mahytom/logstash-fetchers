package eu.wajja.web.fetcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.elastic.logstash.api.Configuration;
import co.elastic.logstash.api.Context;
import co.elastic.logstash.api.Input;
import co.elastic.logstash.api.LogstashPlugin;
import co.elastic.logstash.api.PluginConfigSpec;
import eu.wajja.web.fetcher.config.SchedulerBuilder;

/**
 * Simple tool to fetch http content and send it to logstash
 * 
 * @author mahytom
 *
 */
@LogstashPlugin(name = "webfetcher")
public class WebFetcher implements Input {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebFetcher.class);

	protected static final String PROPERTY_URLS = "urls";
	protected static final String PROPERTY_EXCLUDE_DATA = "excludeData";
	protected static final String PROPERTY_EXCLUDE_LINK = "excludeLink";
	protected static final String PROPERTY_DATAFOLDER = "dataFolder";
	protected static final String PROPERTY_TIMEOUT = "timeout";
	protected static final String PROPERTY_MAX_DEPTH = "maxdepth";
	protected static final String PROPERTY_MAX_PAGES = "maxpages";
	protected static final String PROPERTY_SSL_CHECK = "sslcheck";
	protected static final String PROPERTY_REFRESH_INTERVAL = "refreshInterval";
	protected static final String PROPERTY_PROXY_HOST = "proxyHost";
	protected static final String PROPERTY_PROXY_PORT = "proxyPort";
	protected static final String PROPERTY_PROXY_USER = "proxyUser";
	protected static final String PROPERTY_PROXY_PASS = "proxyPass";
	protected static final String PROPERTY_CRON = "cron";
	protected static final String PROPERTY_CONSUMER = "consumer";
	protected static final String PROPERTY_THREAD_ID = "threadId";
	protected static final String PROPERTY_CHROME_DRIVERS = "chromeDrivers";
	protected static final String PROPERTY_CRAWLER_USER_AGENT = "crawlerUserAgent";
	protected static final String PROPERTY_CRAWLER_REFERER = "crawlerReferer";
	protected static final String PROPERTY_READ_ROBOT = "readRobot";

	public static final String GROUP_NAME = "group001";

	public static final PluginConfigSpec<List<Object>> CONFIG_URLS = PluginConfigSpec.arraySetting(PROPERTY_URLS);
	public static final PluginConfigSpec<List<Object>> CONFIG_EXCLUDE_DATA = PluginConfigSpec.arraySetting(PROPERTY_EXCLUDE_DATA, new ArrayList<>(), false, false);
	public static final PluginConfigSpec<List<Object>> CONFIG_EXCLUDE_LINK = PluginConfigSpec.arraySetting(PROPERTY_EXCLUDE_LINK, new ArrayList<>(), false, false);
	public static final PluginConfigSpec<String> CONFIG_DATA_FOLDER = PluginConfigSpec.stringSetting(PROPERTY_DATAFOLDER);
	public static final PluginConfigSpec<Long> CONFIG_TIMEOUT = PluginConfigSpec.numSetting(PROPERTY_TIMEOUT, 8000);
	public static final PluginConfigSpec<Long> CONFIG_MAX_DEPTH = PluginConfigSpec.numSetting(PROPERTY_MAX_DEPTH, 0);
	public static final PluginConfigSpec<Long> CONFIG_MAX_PAGES = PluginConfigSpec.numSetting(PROPERTY_MAX_PAGES, 0);
	public static final PluginConfigSpec<Boolean> CONFIG_DISABLE_SSL_CHECK = PluginConfigSpec.booleanSetting(PROPERTY_SSL_CHECK, true);
	public static final PluginConfigSpec<Long> CONFIG_REFRESH_INTERVAL = PluginConfigSpec.numSetting(PROPERTY_REFRESH_INTERVAL, 86400l);
	public static final PluginConfigSpec<String> CONFIG_PROXY_HOST = PluginConfigSpec.stringSetting(PROPERTY_PROXY_HOST);
	public static final PluginConfigSpec<Long> CONFIG_PROXY_PORT = PluginConfigSpec.numSetting(PROPERTY_PROXY_PORT, 80);
	public static final PluginConfigSpec<String> CONFIG_PROXY_USER = PluginConfigSpec.stringSetting(PROPERTY_PROXY_USER);
	public static final PluginConfigSpec<String> CONFIG_PROXY_PASS = PluginConfigSpec.stringSetting(PROPERTY_PROXY_PASS);
	public static final PluginConfigSpec<String> CONFIG_CRON = PluginConfigSpec.stringSetting(PROPERTY_CRON);
	public static final PluginConfigSpec<List<Object>> CONFIG_CHROME_DRIVERS = PluginConfigSpec.arraySetting(PROPERTY_CHROME_DRIVERS, new ArrayList<>(), false, false);
	public static final PluginConfigSpec<String> CONFIG_CRAWLER_USER_AGENT = PluginConfigSpec.stringSetting(PROPERTY_CRAWLER_USER_AGENT, "Wajja Crawler");
	public static final PluginConfigSpec<String> CONFIG_CRAWLER_REFERER = PluginConfigSpec.stringSetting(PROPERTY_CRAWLER_REFERER, "http://wajja.eu/");
	public static final PluginConfigSpec<Boolean> CONFIG_READ_ROBOT = PluginConfigSpec.booleanSetting(PROPERTY_READ_ROBOT, true);

	private final CountDownLatch done = new CountDownLatch(1);
	protected volatile boolean stopped;

	private String threadId;
	private List<String> urls;
	private String cron;
	private JobDataMap jobDataMap = new JobDataMap();

	/**
	 * Mandatory constructor
	 * 
	 * @param id
	 * @param config
	 * @param context
	 * @throws SchedulerException
	 */
	public WebFetcher(String id, Configuration config, Context context) {

		if (context != null && LOGGER.isDebugEnabled()) {
			LOGGER.debug(context.toString());
		}

		jobDataMap.put(PROPERTY_DATAFOLDER, config.get(CONFIG_DATA_FOLDER));
		jobDataMap.put(PROPERTY_EXCLUDE_DATA, config.get(CONFIG_EXCLUDE_DATA).stream().map(url -> (String) url).collect(Collectors.toList()));
		jobDataMap.put(PROPERTY_EXCLUDE_LINK, config.get(CONFIG_EXCLUDE_LINK).stream().map(url -> (String) url).collect(Collectors.toList()));
		jobDataMap.put(PROPERTY_MAX_DEPTH, config.get(CONFIG_MAX_DEPTH));
		jobDataMap.put(PROPERTY_MAX_PAGES, config.get(CONFIG_MAX_PAGES));
		jobDataMap.put(PROPERTY_TIMEOUT, config.get(CONFIG_TIMEOUT));
		jobDataMap.put(PROPERTY_CHROME_DRIVERS, config.get(CONFIG_CHROME_DRIVERS).stream().map(url -> (String) url).collect(Collectors.toList()));
		jobDataMap.put(PROPERTY_CRAWLER_REFERER, config.get(CONFIG_CRAWLER_REFERER));
		jobDataMap.put(PROPERTY_CRAWLER_USER_AGENT, config.get(CONFIG_CRAWLER_USER_AGENT));
		jobDataMap.put(PROPERTY_READ_ROBOT, config.get(CONFIG_READ_ROBOT));

		jobDataMap.put(PROPERTY_PROXY_HOST, config.get(CONFIG_PROXY_HOST));
		jobDataMap.put(PROPERTY_PROXY_PORT, config.get(CONFIG_PROXY_PORT));
		jobDataMap.put(PROPERTY_PROXY_USER, config.get(CONFIG_PROXY_USER));
		jobDataMap.put(PROPERTY_PROXY_PASS, config.get(CONFIG_PROXY_PASS));

		this.threadId = id;
		this.urls = config.get(CONFIG_URLS).stream().map(url -> (String) url).collect(Collectors.toList());
		this.cron = config.get(CONFIG_CRON);

		jobDataMap.put(PROPERTY_PROXY_PASS, config.get(CONFIG_PROXY_PASS));
	}

	@Override
	public void start(Consumer<Map<String, Object>> consumer) {

		try {

			JobDataMap newJobDataMap = new JobDataMap(this.jobDataMap);
			newJobDataMap.put(PROPERTY_URLS, urls);
			newJobDataMap.put(PROPERTY_CONSUMER, consumer);
			newJobDataMap.put(PROPERTY_THREAD_ID, threadId);

			String uuid = UUID.randomUUID().toString();

			JobDetail job = JobBuilder.newJob(FetcherJob.class)
					.withIdentity(uuid, GROUP_NAME)
					.setJobData(newJobDataMap)
					.build();

			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity(uuid, GROUP_NAME)
					.startNow()
					.withSchedule(CronScheduleBuilder.cronSchedule(this.cron))
					.build();

			SchedulerBuilder.getScheduler().scheduleJob(job, trigger);

			while (!stopped) {
				Thread.sleep(1000);
			}

		} catch (Exception e) {
			LOGGER.error("Failed", e);
		}
	}

	@Override
	public void stop() {

		try {
			SchedulerBuilder.getScheduler().shutdown();
		} catch (SchedulerException e) {
			LOGGER.error("Failed to stop scheduler", e);
		}

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
		return Arrays.asList(CONFIG_URLS,
				CONFIG_DATA_FOLDER,
				CONFIG_DISABLE_SSL_CHECK,
				CONFIG_EXCLUDE_DATA,
				CONFIG_EXCLUDE_LINK,
				CONFIG_REFRESH_INTERVAL,
				CONFIG_PROXY_HOST,
				CONFIG_PROXY_PASS,
				CONFIG_PROXY_PORT,
				CONFIG_PROXY_USER,
				CONFIG_MAX_DEPTH,
				CONFIG_TIMEOUT,
				CONFIG_MAX_PAGES,
				CONFIG_CRON,
				CONFIG_READ_ROBOT,
				CONFIG_CRAWLER_REFERER,
				CONFIG_CRAWLER_USER_AGENT,
				CONFIG_CHROME_DRIVERS);
	}

	@Override
	public String getId() {
		return this.threadId;
	}
}

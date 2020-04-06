package eu.wajja.web.fetcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

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
	protected static final String PROPERTY_CONSUMER = "consumer";

	private static final String PROPERTY_EXCLUDE_DATA = "excludeData";
	private static final String PROPERTY_EXCLUDE_LINK = "excludeLink";
	private static final String PROPERTY_DATAFOLDER = "dataFolder";
	private static final String PROPERTY_THREADS = "threads";
	private static final String PROPERTY_TIMEOUT = "timeout";
	private static final String PROPERTY_MAX_DEPTH = "maxdepth";
	private static final String PROPERTY_MAX_PAGES = "maxpages";
	private static final String PROPERTY_JAVASCRIPT = "waitJavascript";
	private static final String PROPERTY_SSL_CHECK = "sslcheck";
	private static final String PROPERTY_REFRESH_INTERVAL = "refreshInterval";
	private static final String PROPERTY_PROXY_HOST = "proxyHost";
	private static final String PROPERTY_PROXY_PORT = "proxyPort";
	private static final String PROPERTY_PROXY_USER = "proxyUser";
	private static final String PROPERTY_PROXY_PASS = "proxyPass";
	private static final String PROPERTY_CRON = "cron";
	private static final String PROPERTY_CHROME_DRIVER = "chromeDriver";
	private static final String PROPERTY_CRAWLER_USER_AGENT = "crawlerUserAgent";
	private static final String PROPERTY_CRAWLER_REFERER = "crawlerReferer";
	private static final String PROPERTY_READ_ROBOT = "readRobot";
	private static final String PROPERTY_START_TIME = "startTime";

	public static final String GROUP_NAME = "group001";

	List<PluginConfigSpec<?>> propertiesList = Arrays.asList(
			PluginConfigSpec.arraySetting(PROPERTY_URLS),
			PluginConfigSpec.arraySetting(PROPERTY_EXCLUDE_DATA, new ArrayList<>(), false, false),
			PluginConfigSpec.arraySetting(PROPERTY_EXCLUDE_LINK, new ArrayList<>(), false, false),
			PluginConfigSpec.stringSetting(PROPERTY_DATAFOLDER),
			PluginConfigSpec.numSetting(PROPERTY_TIMEOUT, 8000),
			PluginConfigSpec.numSetting(PROPERTY_MAX_DEPTH, 0),
			PluginConfigSpec.numSetting(PROPERTY_MAX_PAGES, 0),
			PluginConfigSpec.booleanSetting(PROPERTY_JAVASCRIPT, false),
			PluginConfigSpec.booleanSetting(PROPERTY_SSL_CHECK, true),
			PluginConfigSpec.numSetting(PROPERTY_REFRESH_INTERVAL, 86400l),
			PluginConfigSpec.stringSetting(PROPERTY_PROXY_HOST),
			PluginConfigSpec.numSetting(PROPERTY_PROXY_PORT, 80),
			PluginConfigSpec.stringSetting(PROPERTY_PROXY_USER),
			PluginConfigSpec.stringSetting(PROPERTY_PROXY_PASS),
			PluginConfigSpec.stringSetting(PROPERTY_CRON),
			PluginConfigSpec.numSetting(PROPERTY_THREADS, 1l),
			PluginConfigSpec.stringSetting(PROPERTY_CHROME_DRIVER, null, false, false),
			PluginConfigSpec.stringSetting(PROPERTY_CRAWLER_USER_AGENT, "Wajja Crawler"),
			PluginConfigSpec.stringSetting(PROPERTY_CRAWLER_REFERER, "http://wajja.eu/"),
			PluginConfigSpec.booleanSetting(PROPERTY_READ_ROBOT, true));

	private final CountDownLatch done = new CountDownLatch(1);
	private volatile boolean stopped;

	private String cron;
	private String threadId;
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

		propertiesList.stream().forEach(l -> this.jobDataMap.put(l.name(), config.get(l)));
		this.jobDataMap.put(PROPERTY_START_TIME, new Date().getTime());

		this.cron = (String) config.get(propertiesList.stream().filter(x -> x.name().equals(PROPERTY_CRON)).findFirst().orElse(null));
		this.threadId = id;

	}

	@Override
	public void start(Consumer<Map<String, Object>> consumer) {

		try {

			jobDataMap.put(PROPERTY_CONSUMER, consumer);
			String uuid = UUID.randomUUID().toString();

			JobDetail job = JobBuilder.newJob(FetcherJob.class)
					.withIdentity(uuid, GROUP_NAME)
					.setJobData(jobDataMap)
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
		return propertiesList;
	}

	@Override
	public String getId() {
		return this.threadId;
	}
}

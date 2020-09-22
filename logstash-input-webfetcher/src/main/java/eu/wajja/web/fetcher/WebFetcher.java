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
    protected static final String PROPERTY_CHROME_DRIVERS = "chromeDrivers";
    protected static final String PROPERTY_CRAWLER_USER_AGENT = "crawlerUserAgent";
    protected static final String PROPERTY_CRAWLER_REFERER = "crawlerReferer";
    protected static final String PROPERTY_WAIT_FOR_CSS_SELECTOR = "waitForCssSelector";
    protected static final String PROPERTY_MAX_WAIT_FOR_CSS_SELECTOR = "maxWaitForCssSelector";
    protected static final String PROPERTY_READ_ROBOT = "readRobot";
    protected static final String PROPERTY_ROOT_URL = "rootUrl";
    protected static final String PROPERTY_REINDEX = "reindex";
    protected static final String PROPERTY_ENABLE_CRAWL = "enableCrawl";
    protected static final String PROPERTY_ENABLE_DELETE = "enableDelete";
    protected static final String PROPERTY_ENABLE_REGEX = "enableRegex";

    protected static final String PROPERTY_ELASTIC_HOSTNAMES = "elasticsearchHostnames";
    protected static final String PROPERTY_ELASTIC_USERNAME = "elasticsearchUsername";
    protected static final String PROPERTY_ELASTIC_PASSWORD = "elasticsearchPassword";

    public static final String GROUP_NAME = "group001";

    public static final PluginConfigSpec<List<Object>> CONFIG_URLS = PluginConfigSpec.arraySetting(PROPERTY_URLS);
    public static final PluginConfigSpec<List<Object>> CONFIG_EXCLUDE_DATA = PluginConfigSpec.arraySetting(PROPERTY_EXCLUDE_DATA, new ArrayList<>(), false, false);
    public static final PluginConfigSpec<List<Object>> CONFIG_EXCLUDE_LINK = PluginConfigSpec.arraySetting(PROPERTY_EXCLUDE_LINK, new ArrayList<>(), false, false);
    public static final PluginConfigSpec<Long> CONFIG_TIMEOUT = PluginConfigSpec.numSetting(PROPERTY_TIMEOUT, 8000);
    public static final PluginConfigSpec<Long> CONFIG_MAX_DEPTH = PluginConfigSpec.numSetting(PROPERTY_MAX_DEPTH, 0);
    public static final PluginConfigSpec<Long> CONFIG_MAX_PAGES = PluginConfigSpec.numSetting(PROPERTY_MAX_PAGES, 1000);
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
    public static final PluginConfigSpec<Boolean> CONFIG_REINDEX = PluginConfigSpec.booleanSetting(PROPERTY_REINDEX, false);
    public static final PluginConfigSpec<String> CONFIG_ROOT_URL = PluginConfigSpec.stringSetting(PROPERTY_ROOT_URL, null, false, false);
    public static final PluginConfigSpec<Boolean> CONFIG_ENABLE_REGEX = PluginConfigSpec.booleanSetting(PROPERTY_ENABLE_REGEX, false);

    public static final PluginConfigSpec<List<Object>> CONFIG_ELASTIC_HOSTNAMES = PluginConfigSpec.arraySetting(PROPERTY_ELASTIC_HOSTNAMES, new ArrayList<>(), false, false);
    public static final PluginConfigSpec<String> CONFIG_ELASTIC_USERNAME = PluginConfigSpec.stringSetting(PROPERTY_ELASTIC_USERNAME, null, false, false);
    public static final PluginConfigSpec<String> CONFIG_ELASTIC_PASSWORD = PluginConfigSpec.stringSetting(PROPERTY_ELASTIC_PASSWORD, null, false, false);
    public static final PluginConfigSpec<Boolean> CONFIG_ENABLE_CRAWL = PluginConfigSpec.booleanSetting(PROPERTY_ENABLE_CRAWL, true, false, false);
    public static final PluginConfigSpec<Boolean> CONFIG_ENABLE_DELETE = PluginConfigSpec.booleanSetting(PROPERTY_ENABLE_DELETE, false, false, false);

    public static final PluginConfigSpec<String> CONFIG_WAIT_FOR_CSS_SELECTOR = PluginConfigSpec.stringSetting(PROPERTY_WAIT_FOR_CSS_SELECTOR);
    public static final PluginConfigSpec<Long> CONFIG_MAX_WAIT_FOR_CSS_SELECTOR = PluginConfigSpec.numSetting(PROPERTY_MAX_WAIT_FOR_CSS_SELECTOR, 30);

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

        jobDataMap.put(PROPERTY_EXCLUDE_DATA, config.get(CONFIG_EXCLUDE_DATA).stream().map(url -> (String) url).collect(Collectors.toList()));
        jobDataMap.put(PROPERTY_EXCLUDE_LINK, config.get(CONFIG_EXCLUDE_LINK).stream().map(url -> (String) url).collect(Collectors.toList()));
        jobDataMap.put(PROPERTY_MAX_DEPTH, config.get(CONFIG_MAX_DEPTH));
        jobDataMap.put(PROPERTY_MAX_PAGES, config.get(CONFIG_MAX_PAGES));
        jobDataMap.put(PROPERTY_TIMEOUT, config.get(CONFIG_TIMEOUT));
        jobDataMap.put(PROPERTY_CHROME_DRIVERS, config.get(CONFIG_CHROME_DRIVERS).stream().map(url -> (String) url).collect(Collectors.toList()));
        jobDataMap.put(PROPERTY_CRAWLER_REFERER, config.get(CONFIG_CRAWLER_REFERER));
        jobDataMap.put(PROPERTY_CRAWLER_USER_AGENT, config.get(CONFIG_CRAWLER_USER_AGENT));
        jobDataMap.put(PROPERTY_READ_ROBOT, config.get(CONFIG_READ_ROBOT));
        jobDataMap.put(PROPERTY_ROOT_URL, config.get(CONFIG_ROOT_URL));

        jobDataMap.put(PROPERTY_PROXY_HOST, config.get(CONFIG_PROXY_HOST));
        jobDataMap.put(PROPERTY_PROXY_PORT, config.get(CONFIG_PROXY_PORT));
        jobDataMap.put(PROPERTY_PROXY_USER, config.get(CONFIG_PROXY_USER));
        jobDataMap.put(PROPERTY_PROXY_PASS, config.get(CONFIG_PROXY_PASS));

        jobDataMap.put(PROPERTY_ELASTIC_HOSTNAMES, config.get(CONFIG_ELASTIC_HOSTNAMES).stream().map(url -> (String) url).collect(Collectors.toList()));
        jobDataMap.put(PROPERTY_ELASTIC_USERNAME, config.get(CONFIG_ELASTIC_USERNAME));
        jobDataMap.put(PROPERTY_ELASTIC_PASSWORD, config.get(CONFIG_ELASTIC_PASSWORD));

        jobDataMap.put(PROPERTY_WAIT_FOR_CSS_SELECTOR, config.get(CONFIG_WAIT_FOR_CSS_SELECTOR));
        jobDataMap.put(PROPERTY_MAX_WAIT_FOR_CSS_SELECTOR, config.get(CONFIG_MAX_WAIT_FOR_CSS_SELECTOR));
        jobDataMap.put(PROPERTY_REINDEX, config.get(CONFIG_REINDEX));
        jobDataMap.put(PROPERTY_ENABLE_CRAWL, config.get(CONFIG_ENABLE_CRAWL));
        jobDataMap.put(PROPERTY_ENABLE_DELETE, config.get(CONFIG_ENABLE_DELETE));
        jobDataMap.put(PROPERTY_ENABLE_REGEX, config.get(CONFIG_ENABLE_REGEX));

        this.threadId = id;
        this.urls = config.get(CONFIG_URLS).stream().map(url -> (String) url).collect(Collectors.toList());
        this.cron = config.get(CONFIG_CRON);

        jobDataMap.put(PROPERTY_PROXY_PASS, config.get(CONFIG_PROXY_PASS));
    }

    @Override
    public void start(Consumer<Map<String, Object>> consumer) {

        LOGGER.info("Starting a new Thread");

        try {

            JobDataMap newJobDataMap = new JobDataMap(this.jobDataMap);
            newJobDataMap.put(PROPERTY_URLS, urls);
            newJobDataMap.put(PROPERTY_CONSUMER, consumer);

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

        LOGGER.info("Closing all scheduled jobs");

        try {
            SchedulerBuilder.getScheduler().clear();
        } catch (SchedulerException e) {
            LOGGER.error("Failed to stop scheduler", e);
        }

        stopped = true;
    }

    @Override
    public void awaitStop() throws InterruptedException {

        LOGGER.info("Awaiting full stop");

        done.await();
    }

    /**
     * Returns a list of all configuration
     */
    @Override
    public Collection<PluginConfigSpec<?>> configSchema() {

        return Arrays.asList(CONFIG_URLS,
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
                CONFIG_CHROME_DRIVERS,
                CONFIG_WAIT_FOR_CSS_SELECTOR,
                CONFIG_ROOT_URL,
                CONFIG_REINDEX,
                CONFIG_ELASTIC_HOSTNAMES,
                CONFIG_ELASTIC_USERNAME,
                CONFIG_ELASTIC_PASSWORD,
                CONFIG_ENABLE_CRAWL,
                CONFIG_ENABLE_DELETE,
                CONFIG_ENABLE_REGEX,
                CONFIG_MAX_WAIT_FOR_CSS_SELECTOR);
    }

    @Override
    public String getId() {

        return this.threadId;
    }
}

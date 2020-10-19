package eu.wajja.input.fetcher;

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
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.elastic.logstash.api.Configuration;
import co.elastic.logstash.api.Context;
import co.elastic.logstash.api.Input;
import co.elastic.logstash.api.LogstashPlugin;
import co.elastic.logstash.api.PluginConfigSpec;
import eu.wajja.input.fetcher.config.SchedulerBuilder;

@LogstashPlugin(name = "filesystemfetcher")
public class FilesystemFetcher implements Input {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilesystemFetcher.class);

    protected static final String PROPERTY_CRON = "cron";
    protected static final String PROPERTY_PATHS = "paths";
    protected static final String PROPERTY_EXCLUDE = "exclude";
    protected static final String PROPERTY_THREADS = "threads";
    protected static final String PROPERTY_CONSUMER = "consumer";
    protected static final String PROPERTY_ELASTIC_HOSTNAMES = "elasticsearchHostnames";
    protected static final String PROPERTY_ELASTIC_USERNAME = "elasticsearchUsername";
    protected static final String PROPERTY_ELASTIC_PASSWORD = "elasticsearchPassword";
    protected static final String PROPERTY_PROXY_HOST = "proxyHost";
    protected static final String PROPERTY_PROXY_PORT = "proxyPort";
    protected static final String PROPERTY_PROXY_USER = "proxyUser";
    protected static final String PROPERTY_PROXY_PASS = "proxyPass";
    protected static final String PROPERTY_SSL_CHECK = "sslcheck";

    public static final PluginConfigSpec<List<Object>> CONFIG_PATHS = PluginConfigSpec.arraySetting(PROPERTY_PATHS);
    public static final PluginConfigSpec<List<Object>> CONFIG_EXCLUDE = PluginConfigSpec.arraySetting(PROPERTY_EXCLUDE, new ArrayList<>(), false, false);
    public static final PluginConfigSpec<Long> CONFIG_THREADS = PluginConfigSpec.numSetting(PROPERTY_THREADS, 10);
    public static final PluginConfigSpec<String> CONFIG_CRON = PluginConfigSpec.stringSetting(PROPERTY_CRON);
    public static final PluginConfigSpec<List<Object>> CONFIG_ELASTIC_HOSTNAMES = PluginConfigSpec.arraySetting(PROPERTY_ELASTIC_HOSTNAMES, new ArrayList<>(), false, false);
    public static final PluginConfigSpec<String> CONFIG_ELASTIC_USERNAME = PluginConfigSpec.stringSetting(PROPERTY_ELASTIC_USERNAME, null, false, false);
    public static final PluginConfigSpec<String> CONFIG_ELASTIC_PASSWORD = PluginConfigSpec.stringSetting(PROPERTY_ELASTIC_PASSWORD, null, false, false);
    public static final PluginConfigSpec<String> CONFIG_PROXY_HOST = PluginConfigSpec.stringSetting(PROPERTY_PROXY_HOST);
    public static final PluginConfigSpec<Long> CONFIG_PROXY_PORT = PluginConfigSpec.numSetting(PROPERTY_PROXY_PORT, 80);
    public static final PluginConfigSpec<String> CONFIG_PROXY_USER = PluginConfigSpec.stringSetting(PROPERTY_PROXY_USER);
    public static final PluginConfigSpec<String> CONFIG_PROXY_PASS = PluginConfigSpec.stringSetting(PROPERTY_PROXY_PASS);
    public static final PluginConfigSpec<Boolean> CONFIG_DISABLE_SSL_CHECK = PluginConfigSpec.booleanSetting(PROPERTY_SSL_CHECK, true);

    public static final String GROUP_NAME = "FilesystemFetcherGroup";

    private final CountDownLatch done = new CountDownLatch(1);
    private volatile boolean stopped;
    private String threadId;
    private String cron;

    private JobDataMap jobDataMap = new JobDataMap();

    /**
     * Mandatory constructor
     * 
     * @param id
     * @param config
     * @param context
     */
    public FilesystemFetcher(String id, Configuration config, Context context) {

        if (context != null && LOGGER.isDebugEnabled()) {
            LOGGER.debug(context.toString());
        }

        jobDataMap.put(PROPERTY_PATHS, config.get(CONFIG_PATHS).stream().map(url -> (String) url).collect(Collectors.toList()));
        jobDataMap.put(PROPERTY_EXCLUDE, config.get(CONFIG_EXCLUDE).stream().map(url -> (String) url).collect(Collectors.toList()));
        jobDataMap.put(PROPERTY_THREADS, config.get(CONFIG_THREADS));

        this.cron = config.get(CONFIG_CRON);

    }

    @Override
    public void start(Consumer<Map<String, Object>> consumer) {

        LOGGER.info("Starting a new Thread");

        try {

            JobDataMap newJobDataMap = new JobDataMap(this.jobDataMap);
            newJobDataMap.put(PROPERTY_CONSUMER, consumer);

            String uuid = UUID.randomUUID().toString();

            JobDetail job = JobBuilder.newJob(FilesystemFetcherJob.class)
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
            LOGGER.error("Failedto initialize the cron jobs", e);
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

    @Override
    public Collection<PluginConfigSpec<?>> configSchema() {

        return Arrays.asList(CONFIG_PATHS,
                CONFIG_EXCLUDE,
                CONFIG_THREADS,
                CONFIG_ELASTIC_HOSTNAMES,
                CONFIG_ELASTIC_USERNAME,
                CONFIG_ELASTIC_PASSWORD,
                CONFIG_PROXY_HOST,
                CONFIG_PROXY_PORT,
                CONFIG_PROXY_USER,
                CONFIG_PROXY_PASS,
                CONFIG_DISABLE_SSL_CHECK,
                CONFIG_CRON);
    }

    @Override
    public String getId() {

        return this.threadId;
    }

}

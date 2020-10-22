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

/**
 * Fetches pages from a confluence page
 * 
 * @author mahytom
 *
 */
@LogstashPlugin(name = "sambafetcher")
public class SambaFetcher implements Input {

    private static final Logger LOGGER = LoggerFactory.getLogger(SambaFetcher.class);

    protected static final String PROPERTY_SMB_HOST = "smbHost";
    protected static final String PROPERTY_SMB_USERNAME = "smbUsername";
    protected static final String PROPERTY_SMB_PASSWORD = "smbPassword";
    protected static final String PROPERTY_SMB_DOMAIN = "smbDomain";
    protected static final String PROPERTY_SMB_FOLDER = "smbFolder";

    protected static final String PROPERTY_CRON = "cron";
    protected static final String PROPERTY_EXCLUDE = "exclude";
    protected static final String PROPERTY_CONSUMER = "consumer";
    protected static final String PROPERTY_ELASTIC_HOSTNAMES = "elasticsearchHostnames";
    protected static final String PROPERTY_ELASTIC_USERNAME = "elasticsearchUsername";
    protected static final String PROPERTY_ELASTIC_PASSWORD = "elasticsearchPassword";

    public static final PluginConfigSpec<String> CONFIG_CRON = PluginConfigSpec.stringSetting(PROPERTY_CRON, null, false, false);

    public static final PluginConfigSpec<String> CONFIG_SMB_HOST = PluginConfigSpec.stringSetting(PROPERTY_SMB_HOST, null, false, false);
    public static final PluginConfigSpec<String> CONFIG_SMB_USERNAME = PluginConfigSpec.stringSetting(PROPERTY_SMB_USERNAME, null, false, false);
    public static final PluginConfigSpec<String> CONFIG_SMB_PASSWORD = PluginConfigSpec.stringSetting(PROPERTY_SMB_PASSWORD, null, false, false);
    public static final PluginConfigSpec<String> CONFIG_SMB_DOMAIN = PluginConfigSpec.stringSetting(PROPERTY_SMB_DOMAIN, null, false, false);
    public static final PluginConfigSpec<String> CONFIG_SMB_FOLDER = PluginConfigSpec.stringSetting(PROPERTY_SMB_FOLDER, null, false, false);

    public static final PluginConfigSpec<List<Object>> CONFIG_EXCLUDE = PluginConfigSpec.arraySetting(PROPERTY_EXCLUDE, new ArrayList<>(), false, false);
    public static final PluginConfigSpec<List<Object>> CONFIG_ELASTIC_HOSTNAMES = PluginConfigSpec.arraySetting(PROPERTY_ELASTIC_HOSTNAMES, new ArrayList<>(), false, false);
    public static final PluginConfigSpec<String> CONFIG_ELASTIC_USERNAME = PluginConfigSpec.stringSetting(PROPERTY_ELASTIC_USERNAME, null, false, false);
    public static final PluginConfigSpec<String> CONFIG_ELASTIC_PASSWORD = PluginConfigSpec.stringSetting(PROPERTY_ELASTIC_PASSWORD, null, false, false);

    public static final String GROUP_NAME = "SmbFetcherGroup";

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
    public SambaFetcher(String id, Configuration config, Context context) {

        this.threadId = id;

        if (context != null && LOGGER.isDebugEnabled()) {
            LOGGER.debug(context.toString());
        }

        jobDataMap.put(PROPERTY_SMB_HOST, config.get(CONFIG_SMB_HOST));
        jobDataMap.put(PROPERTY_SMB_USERNAME, config.get(CONFIG_SMB_USERNAME));
        jobDataMap.put(PROPERTY_SMB_PASSWORD, config.get(CONFIG_SMB_PASSWORD));
        jobDataMap.put(PROPERTY_SMB_DOMAIN, config.get(CONFIG_SMB_DOMAIN));
        jobDataMap.put(PROPERTY_SMB_FOLDER, config.get(CONFIG_SMB_FOLDER));
        
        jobDataMap.put(PROPERTY_EXCLUDE, config.get(CONFIG_EXCLUDE).stream().map(url -> (String) url).collect(Collectors.toList()));

        jobDataMap.put(PROPERTY_ELASTIC_HOSTNAMES, config.get(CONFIG_ELASTIC_HOSTNAMES).stream().map(url -> (String) url).collect(Collectors.toList()));
        jobDataMap.put(PROPERTY_ELASTIC_USERNAME, config.get(CONFIG_ELASTIC_USERNAME));
        jobDataMap.put(PROPERTY_ELASTIC_PASSWORD, config.get(CONFIG_ELASTIC_PASSWORD));
        
        this.cron = config.get(CONFIG_CRON);

    }

    @Override
    public void start(Consumer<Map<String, Object>> consumer) {

        LOGGER.info("Starting a new Thread");

        try {

            JobDataMap newJobDataMap = new JobDataMap(this.jobDataMap);
            newJobDataMap.put(PROPERTY_CONSUMER, consumer);

            String uuid = UUID.randomUUID().toString();

            JobDetail job = JobBuilder.newJob(SambaFetcherJob.class)
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

        this.stopped = true;
    }

    @Override
    public void awaitStop() throws InterruptedException {

        this.stopped = true;
    }

    /**
     * Returns a list of all configuration
     */
    @Override
    public Collection<PluginConfigSpec<?>> configSchema() {

        return Arrays.asList(
                CONFIG_SMB_HOST,
                CONFIG_SMB_USERNAME,
                CONFIG_SMB_PASSWORD,
                CONFIG_SMB_DOMAIN,
                CONFIG_SMB_FOLDER,
                CONFIG_EXCLUDE,
                CONFIG_ELASTIC_HOSTNAMES,
                CONFIG_ELASTIC_USERNAME,
                CONFIG_ELASTIC_PASSWORD,
                CONFIG_CRON);
    }

    @Override
    public String getId() {

        return this.threadId;
    }
}

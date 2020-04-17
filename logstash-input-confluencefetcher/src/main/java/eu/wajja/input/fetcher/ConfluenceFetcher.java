package eu.wajja.input.fetcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
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

import com.atlassian.confluence.rest.client.RemoteAttachmentServiceImpl;
import com.atlassian.confluence.rest.client.RemoteContentRestrictionServiceImpl;
import com.atlassian.confluence.rest.client.RemoteContentServiceImpl;
import com.atlassian.confluence.rest.client.RemotePersonServiceImpl;
import com.atlassian.confluence.rest.client.RemoteSpaceServiceImpl;
import com.atlassian.confluence.rest.client.RestClientFactory;
import com.atlassian.confluence.rest.client.authentication.AuthenticatedWebResourceProvider;
import com.atlassian.confluence.rest.client.remoteservice.people.RemoteGroupServiceImpl;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.sun.jersey.api.client.Client;

import co.elastic.logstash.api.Configuration;
import co.elastic.logstash.api.Context;
import co.elastic.logstash.api.Input;
import co.elastic.logstash.api.LogstashPlugin;
import co.elastic.logstash.api.PluginConfigSpec;
import eu.wajja.input.fetcher.config.SchedulerBuilder;
import eu.wajja.input.fetcher.controller.ConfluenceDataFetcher;
import eu.wajja.input.fetcher.controller.ConfluenceGroupFetcher;

/**
 * Fetches pages from a confluence page
 * 
 * @author mahytom
 *
 */
@LogstashPlugin(name = "confluencefetcher")
public class ConfluenceFetcher implements Input {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfluenceFetcher.class);

	/** Security Configuration **/
	public static final PluginConfigSpec<String> CONFIG_USERNAME = PluginConfigSpec.stringSetting("username");
	public static final PluginConfigSpec<String> CONFIG_PASSWORD = PluginConfigSpec.stringSetting("password");

	/** Generic Configuration **/
	public static final PluginConfigSpec<String> CONFIG_URL = PluginConfigSpec.stringSetting("url");
	public static final PluginConfigSpec<String> CONFIG_DATA_FOLDER = PluginConfigSpec.stringSetting("dataFolder");

	/** User Sync Configuration **/
	public static final PluginConfigSpec<Boolean> CONFIG_USER_SYNC = PluginConfigSpec.booleanSetting("enableUserSync", true);
	public static final PluginConfigSpec<Long> CONFIG_USER_BATCH = PluginConfigSpec.numSetting("userSyncBatchSize", 1000);
	public static final PluginConfigSpec<Long> CONFIG_USER_THREADS = PluginConfigSpec.numSetting("userSyncThreadSize", 3);
	public static final PluginConfigSpec<String> CONFIG_USER_CRON = PluginConfigSpec.stringSetting("userSyncCron");

	/** Data Sync Configuration **/
	public static final PluginConfigSpec<Boolean> CONFIG_DATA_SYNC = PluginConfigSpec.booleanSetting("enableDataSync", true);
	public static final PluginConfigSpec<Long> CONFIG_DATA_BATCH = PluginConfigSpec.numSetting("dataSyncBatchSize", 100);
	public static final PluginConfigSpec<Long> CONFIG_DATA_THREADS = PluginConfigSpec.numSetting("dataSyncThreadSize", 3);
	public static final PluginConfigSpec<List<Object>> CONFIG_SPACES = PluginConfigSpec.arraySetting("spaces", new ArrayList<>(), false, false);
	public static final PluginConfigSpec<Long> CONFIG_PAGE_LIMIT = PluginConfigSpec.numSetting("pageLimit", 25);
	public static final PluginConfigSpec<String> CONFIG_DATA_CRON = PluginConfigSpec.stringSetting("dataSyncCron");
	public static final PluginConfigSpec<List<Object>> CONFIG_ATTACHMENTS_INCLUDE = PluginConfigSpec.arraySetting("dataAttachmentsInclude", new ArrayList<>(), false, false);
	public static final PluginConfigSpec<List<Object>> CONFIG_ATTACHMENTS_EXCLUDE = PluginConfigSpec.arraySetting("dataAttachmentsExclude", new ArrayList<>(), false, false);
	public static final PluginConfigSpec<Long> CONFIG_ATTACHMENTS_MAX_FILE_SIZE = PluginConfigSpec.numSetting("dataAttachmentsMaxSize");
	public static final PluginConfigSpec<List<Object>> CONFIG_PAGE_EXCLUDE = PluginConfigSpec.arraySetting("dataPageExclude", new ArrayList<>(), false, false);
	public static final PluginConfigSpec<List<Object>> CONFIG_SPACE_EXCLUDE = PluginConfigSpec.arraySetting("dataSpaceExclude", new ArrayList<>(), false, false);

	public static final String GROUP_NAME = "confluencefetcherGroup";

	private String id;
	private String username;
	private String password;
	private String baseUrl;
	private List<String> spaces;
	private ListeningExecutorService executorUser;
	private ListeningExecutorService executorData;
	private boolean enableUserSync;
	private boolean enableDataSync;
	private Long userBatchSize;
	private Long dataBatchSize;
	private boolean stopped = false;
	private String cronUser;
	private String cronData;
	private String dataFolder;
	private Long dataSyncThreadSize;

	private List<String> dataAttachmentsInclude;
	private List<String> dataAttachmentsExclude;
	private Long dataAttachmentsMaxSize;
	private List<String> dataPageExclude;
	private List<String> dataSpaceExclude;

	/**
	 * Mandatory constructor
	 * 
	 * @param id
	 * @param config
	 * @param context
	 */
	public ConfluenceFetcher(String id, Configuration config, Context context) {

		this.id = id;

		/** Security Configuration **/
		this.username = config.get(CONFIG_USERNAME);
		this.password = config.get(CONFIG_PASSWORD);

		/** Generic Configuration **/
		this.baseUrl = config.get(CONFIG_URL);
		this.dataFolder = config.get(CONFIG_DATA_FOLDER);

		/** User Sync Configuration **/
		this.enableUserSync = config.get(CONFIG_USER_SYNC);
		this.executorUser = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool((config.get(CONFIG_USER_THREADS)).intValue()));
		this.userBatchSize = config.get(CONFIG_USER_BATCH);
		this.cronUser = config.get(CONFIG_USER_CRON);

		/** Data Sync Configuration **/
		this.enableDataSync = config.get(CONFIG_DATA_SYNC);
		this.executorData = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool((config.get(CONFIG_DATA_THREADS)).intValue()));
		this.dataBatchSize = config.get(CONFIG_DATA_BATCH);
		this.spaces = config.get(CONFIG_SPACES).stream().map(spc -> (String) spc).collect(Collectors.toList());
		this.cronData = config.get(CONFIG_DATA_CRON);
		this.dataSyncThreadSize = config.get(CONFIG_DATA_THREADS);

		this.dataAttachmentsInclude = config.get(CONFIG_ATTACHMENTS_INCLUDE).stream().map(spc -> (String) spc).collect(Collectors.toList());
		this.dataAttachmentsExclude = config.get(CONFIG_ATTACHMENTS_EXCLUDE).stream().map(spc -> (String) spc).collect(Collectors.toList());
		this.dataAttachmentsMaxSize = config.get(CONFIG_ATTACHMENTS_MAX_FILE_SIZE);
		this.dataPageExclude = config.get(CONFIG_PAGE_EXCLUDE).stream().map(spc -> (String) spc).collect(Collectors.toList());
		this.dataSpaceExclude = config.get(CONFIG_SPACE_EXCLUDE).stream().map(spc -> (String) spc).collect(Collectors.toList());
	}

	@Override
	public void start(Consumer<Map<String, Object>> consumer) {

		Client client = RestClientFactory.newClient();

		try (AuthenticatedWebResourceProvider provider = new AuthenticatedWebResourceProvider(client, baseUrl, "/")) {

			provider.setAuthContext(username, password.toCharArray());

			if (enableUserSync) {

				try {

					JobDataMap newJobDataMap = new JobDataMap();
					newJobDataMap.put("remotePersonServiceImpl", new RemotePersonServiceImpl(provider, this.executorUser));
					newJobDataMap.put("remoteGroupServiceImpl", new RemoteGroupServiceImpl(provider, this.executorUser));
					newJobDataMap.put("consumer", consumer);
					newJobDataMap.put("batchSize", this.userBatchSize);

					String uuid = UUID.randomUUID().toString();

					JobDetail job = JobBuilder.newJob(ConfluenceGroupFetcher.class)
							.withIdentity(uuid, GROUP_NAME)
							.setJobData(newJobDataMap)
							.build();

					Trigger trigger = TriggerBuilder.newTrigger()
							.withIdentity(uuid, GROUP_NAME)
							.startNow()
							.withSchedule(CronScheduleBuilder.cronSchedule(this.cronUser))
							.build();

					SchedulerBuilder.getScheduler().scheduleJob(job, trigger);

				} catch (Exception e) {
					LOGGER.error("Failed fetch user/groups", e);
				}

			}

			if (enableDataSync) {

				try {

					JobDataMap newJobDataMap = new JobDataMap();
					newJobDataMap.put("remoteSpaceServiceImpl", new RemoteSpaceServiceImpl(provider, this.executorData));
					newJobDataMap.put("remoteContentServiceImpl", new RemoteContentServiceImpl(provider, this.executorData));
					newJobDataMap.put("remoteAttachmentServiceImpl", new RemoteAttachmentServiceImpl(provider, this.executorData));
					newJobDataMap.put("remoteContentRestrictionServiceImpl", new RemoteContentRestrictionServiceImpl(provider, this.executorData));
					newJobDataMap.put("consumer", consumer);
					newJobDataMap.put("batchSize", this.dataBatchSize);
					newJobDataMap.put("sites", this.spaces);
					newJobDataMap.put("url", this.baseUrl);
					newJobDataMap.put("username", this.username);
					newJobDataMap.put("password", this.password);
					newJobDataMap.put("dataAttachmentsInclude", this.dataAttachmentsInclude);
					newJobDataMap.put("dataAttachmentsExclude", this.dataAttachmentsExclude);
					newJobDataMap.put("dataAttachmentsMaxSize", this.dataAttachmentsMaxSize);
					newJobDataMap.put("dataPageExclude", this.dataPageExclude);
					newJobDataMap.put("dataSpaceExclude", this.dataSpaceExclude);
					newJobDataMap.put("dataFolder", this.dataFolder);
					newJobDataMap.put("dataSyncThreadSize", this.dataSyncThreadSize);

					String uuid = UUID.randomUUID().toString();

					JobDetail job = JobBuilder.newJob(ConfluenceDataFetcher.class)
							.withIdentity(uuid, GROUP_NAME)
							.setJobData(newJobDataMap)
							.build();

					Trigger trigger = TriggerBuilder.newTrigger()
							.withIdentity(uuid, GROUP_NAME)
							.startNow()
							.withSchedule(CronScheduleBuilder.cronSchedule(this.cronData))
							.build();

					SchedulerBuilder.getScheduler().scheduleJob(job, trigger);

				} catch (Exception e) {
					LOGGER.error("Failed fetch data", e);
				}
			}

			while (!stopped) {
				Thread.sleep(1000);
			}

		} catch (Exception e) {
			LOGGER.error("Failed to crawl confluence", e);
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

				/** Security Configuration **/
				CONFIG_USERNAME,
				CONFIG_PASSWORD,

				/** Generic Configuration **/
				CONFIG_URL,
				CONFIG_DATA_FOLDER,

				/** User Sync Configuration **/
				CONFIG_USER_SYNC,
				CONFIG_USER_BATCH,
				CONFIG_USER_THREADS,
				CONFIG_USER_CRON,

				/** Data Sync Configuration **/
				CONFIG_DATA_SYNC,
				CONFIG_DATA_BATCH,
				CONFIG_DATA_THREADS,
				CONFIG_SPACES,
				CONFIG_PAGE_LIMIT,
				CONFIG_DATA_CRON,
				CONFIG_ATTACHMENTS_INCLUDE,
				CONFIG_ATTACHMENTS_EXCLUDE,
				CONFIG_ATTACHMENTS_MAX_FILE_SIZE,
				CONFIG_PAGE_EXCLUDE,
				CONFIG_SPACE_EXCLUDE);
	}

	@Override
	public String getId() {
		return this.id;
	}
}

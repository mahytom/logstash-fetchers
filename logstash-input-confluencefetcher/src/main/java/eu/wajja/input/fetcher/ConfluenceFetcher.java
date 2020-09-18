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

import javax.xml.namespace.QName;

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
import com.atlassian.confluence.rest.client.authentication.AuthenticatedWebResourceProvider;
import com.atlassian.confluence.rest.client.remoteservice.people.RemoteGroupServiceImpl;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import co.elastic.logstash.api.Configuration;
import co.elastic.logstash.api.Context;
import co.elastic.logstash.api.Input;
import co.elastic.logstash.api.LogstashPlugin;
import co.elastic.logstash.api.PluginConfigSpec;
import eu.wajja.input.fetcher.config.SchedulerBuilder;
import eu.wajja.input.fetcher.controller.ConfluenceDataFetcher;
import eu.wajja.input.fetcher.controller.ConfluenceGroupFetcher;
import eu.wajja.input.fetcher.soap.confluence.ConfluenceSoapService;
import eu.wajja.input.fetcher.soap.confluence.ConfluenceSoapServiceServiceLocator;

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
	public static final PluginConfigSpec<Long> CONFIG_SLEEP = PluginConfigSpec.numSetting("sleep", 1);
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
	private boolean enableUserSync;
	private boolean enableDataSync;
	private Long userBatchSize;
	private boolean stopped = false;
	private String cronUser;
	private String cronData;
	private String dataFolder;
	private Long dataSyncThreadSize;
	private Long sleep;

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
		this.sleep = config.get(CONFIG_SLEEP);

		/** User Sync Configuration **/
		this.enableUserSync = config.get(CONFIG_USER_SYNC);
		this.userBatchSize = config.get(CONFIG_USER_BATCH);
		this.cronUser = config.get(CONFIG_USER_CRON);

		/** Data Sync Configuration **/
		this.enableDataSync = config.get(CONFIG_DATA_SYNC);
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

		try (AuthenticatedWebResourceProvider provider = AuthenticatedWebResourceProvider.createWithNewClient(this.baseUrl)) {

			provider.setAuthContext(username, password.toCharArray());
			ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(this.dataSyncThreadSize.intValue()));

			if (enableUserSync) {

				try {

					JobDataMap newJobDataMap = new JobDataMap();
					newJobDataMap.put("consumer", consumer);
					newJobDataMap.put("batchSize", this.userBatchSize);
					newJobDataMap.put("sleep", this.sleep);
					newJobDataMap.put("remotePersonServiceImpl", new RemotePersonServiceImpl(provider, executor));
					newJobDataMap.put("remoteGroupServiceImpl", new RemoteGroupServiceImpl(provider, executor));

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

					// SOAP
					try {

						String confluenceLocation = this.baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2?wsdl";
						String confluenceService = "ConfluenceSoapServiceService";
						String confluenceServicepoint = this.baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2";

						QName confluenceQname = new QName(confluenceServicepoint, confluenceService);
						ConfluenceSoapServiceServiceLocator serviceLocator = new ConfluenceSoapServiceServiceLocator(confluenceLocation, confluenceQname, this.baseUrl);
						serviceLocator.setConfluenceserviceV2EndpointAddress(confluenceServicepoint);

						ConfluenceSoapService soapService = serviceLocator.getConfluenceserviceV2();
						String soapToken = soapService.login(username, password);

						newJobDataMap.put("soapService", soapService);
						newJobDataMap.put("soapToken", soapToken);

					} catch (Exception e) {
						LOGGER.info("Failed to connect to SOAP endpoint", e);
					}

					// REST

					newJobDataMap.put("remoteSpaceServiceImpl", new RemoteSpaceServiceImpl(provider, executor));
					newJobDataMap.put("remoteContentServiceImpl", new RemoteContentServiceImpl(provider, executor));
					newJobDataMap.put("remoteAttachmentServiceImpl", new RemoteAttachmentServiceImpl(provider, executor));
					newJobDataMap.put("remoteContentRestrictionServiceImpl", new RemoteContentRestrictionServiceImpl(provider, executor));

					// Generic
					newJobDataMap.put("consumer", consumer);
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
					newJobDataMap.put("sleep", this.sleep);

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
				CONFIG_SLEEP,

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

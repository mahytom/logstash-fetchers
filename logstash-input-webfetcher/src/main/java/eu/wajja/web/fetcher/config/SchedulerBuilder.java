package eu.wajja.web.fetcher.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.DirectSchedulerFactory;
import org.quartz.simpl.RAMJobStore;
import org.quartz.simpl.SimpleThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedulerBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerBuilder.class);

	private static final String DEFAULT_INSTANCE_ID = "LOGSTASH_WEB_FETCHER";
	private static Scheduler scheduler;

	public static Scheduler getScheduler() {
		return scheduler;
	}

	private SchedulerBuilder() {

	}

	static {

		try {
			DirectSchedulerFactory factory = DirectSchedulerFactory.getInstance();

			Integer quartzThreads = Integer.valueOf(System.getProperty("quartzThreads", "100"));		
			SimpleThreadPool threadPool = new SimpleThreadPool(quartzThreads, Thread.NORM_PRIORITY);
			threadPool.setThreadNamePrefix(DEFAULT_INSTANCE_ID);
			threadPool.initialize();

			factory.createScheduler(DEFAULT_INSTANCE_ID, DEFAULT_INSTANCE_ID, threadPool, new RAMJobStore());
			scheduler = factory.getScheduler(DEFAULT_INSTANCE_ID);

			scheduler.start();

		} catch (SchedulerException e) {
			LOGGER.error("Failed to initialize quartz scheduler", e);
		}

	}
}

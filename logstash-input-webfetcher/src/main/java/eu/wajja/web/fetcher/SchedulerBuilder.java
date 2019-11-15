package eu.wajja.web.fetcher;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedulerBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerBuilder.class);

	private static Scheduler scheduler;

	public static Scheduler getScheduler() {
		return scheduler;
	}

	private SchedulerBuilder() {

	}

	static {

		try {
			SchedulerFactory schedulerFactory = new StdSchedulerFactory();
			scheduler = schedulerFactory.getScheduler();
			scheduler.start();

		} catch (SchedulerException e) {
			LOGGER.error("Failed to initialize quartz scheduler", e);
		}

	}
}

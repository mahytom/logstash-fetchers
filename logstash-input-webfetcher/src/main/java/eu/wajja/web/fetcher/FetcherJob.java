package eu.wajja.web.fetcher;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisallowConcurrentExecution
public class FetcherJob implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(FetcherJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		// Test Sending the data to html plugin

		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		Consumer<Map<String, Object>> consumer = (Consumer<Map<String, Object>>) dataMap.get(WebFetcher.PROPERTY_CONSUMER);

		Map<String, Object> metadata = new HashMap<>();
		dataMap.entrySet().stream().filter(
				k -> k.getValue() instanceof String ||
						k.getValue() instanceof List ||
						k.getValue() instanceof Long ||
						k.getValue() instanceof Boolean)
				.forEach(k -> metadata.put(k.getKey(), k.getValue()));

		List<String> urls = (List<String>) metadata.get(WebFetcher.PROPERTY_URLS);

		urls.stream().forEach(url -> {

			LOGGER.info("Starting job : {}", url);
			metadata.put(WebFetcher.PROPERTY_URLS, Arrays.asList(url));
			consumer.accept(metadata);

		});

	}
}

package eu.wajja.web.fetcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.logstash.plugins.ConfigurationImpl;

import co.elastic.logstash.api.Configuration;
import eu.wajja.web.fetcher.WebFetcher;

public class WebFetcherTest {

	@Test
	public void testWebFetcher() {

		Map<String, Object> configValues = new HashMap<>();
		configValues.put(WebFetcher.CONFIG_URLS.name(), Arrays.asList("https://euraxess.ec.europa.eu"));
		configValues.put(WebFetcher.CONFIG_DATA_FOLDER.name(), "C:/data/");
		Configuration config = new ConfigurationImpl(configValues);
		WebFetcher input = new WebFetcher("test-id", config, null);
		TestConsumer testConsumer = new TestConsumer();
		input.start(testConsumer);

		List<Map<String, Object>> events = testConsumer.getEvents();
//		Assert.assertEquals(eventCount, events.size());
//		for (int k = 1; k <= events.size(); k++) {
//			Assert.assertEquals(prefix + " " + StringUtils.center(k + " of " + eventCount, 20),
//					events.get(k - 1).get("message"));
//		}
	}

	private static class TestConsumer implements Consumer<Map<String, Object>> {

		private List<Map<String, Object>> events = new ArrayList<>();

		@Override
		public void accept(Map<String, Object> event) {
			synchronized (this) {
				events.add(event);
			}
		}

		public List<Map<String, Object>> getEvents() {
			return events;
		}
	}
}

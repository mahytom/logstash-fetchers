package eu.wajja.web.fetcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.logstash.plugins.ConfigurationImpl;

import co.elastic.logstash.api.Configuration;

public class WebFetcherTest {

	private Properties properties;

	@Before
	public void intialize() throws IOException {

		properties = new Properties();
		properties.load(this.getClass().getClassLoader().getResourceAsStream("test.properties"));
	}

	@Test
	public void testWebFetcher() {

		Map<String, Object> configValues = new HashMap<>();
		configValues.put(WebFetcher.CONFIG_URLS.name(), Arrays.asList(((String) properties.get(WebFetcher.PROPERTY_URLS))));
		configValues.put(WebFetcher.CONFIG_DATA_FOLDER.name(), properties.get(WebFetcher.PROPERTY_DATAFOLDER));
		configValues.put(WebFetcher.CONFIG_EXCLUDE.name(), Arrays.asList(((String) properties.get(WebFetcher.PROPERTY_EXCLUDE)).split(",")));

		Configuration config = new ConfigurationImpl(configValues);
		WebFetcher webFetcher = new WebFetcher("test-id", config, null);
		webFetcher.stopped = true;

		TestConsumer testConsumer = new TestConsumer();
		webFetcher.start(testConsumer);

		List<Map<String, Object>> events = testConsumer.getEvents();

		Assert.assertEquals(0, events.size());
		webFetcher.stop();
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

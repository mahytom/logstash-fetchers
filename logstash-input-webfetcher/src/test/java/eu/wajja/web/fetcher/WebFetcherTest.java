package eu.wajja.web.fetcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
	public void testWebFetcher() throws InterruptedException {
		System.out.println("Testging now : ");

		Map<String, Object> configValues = new HashMap<>();

		Map<String, Object> secondConfigValues = new HashMap<>();
		configValues.put(WebFetcher.CONFIG_URLS.name(),
				Arrays.asList(((String) properties.get(WebFetcher.PROPERTY_URLS))));
		configValues.put(WebFetcher.CONFIG_DATA_FOLDER.name(), properties.get(WebFetcher.PROPERTY_DATAFOLDER));
		configValues.put(WebFetcher.CONFIG_EXCLUDE_DATA.name(),
				Arrays.asList(((String) properties.getOrDefault(WebFetcher.PROPERTY_EXCLUDE_DATA, ".css")).split(","))
						.stream().map(tt -> tt.substring(1, tt.length() - 1)).collect(Collectors.toList()));
		configValues.put(WebFetcher.CONFIG_EXCLUDE_LINK.name(),
				Arrays.asList(((String) properties.getOrDefault(WebFetcher.PROPERTY_EXCLUDE_LINK, ".css")).split(","))
						.stream().map(tt -> tt.substring(1, tt.length() - 1)).collect(Collectors.toList()));
		configValues.put(WebFetcher.CONFIG_CRON.name(), properties.get(WebFetcher.PROPERTY_CRON));
		configValues.put(WebFetcher.CONFIG_TIMEOUT.name(),
				new Long((String) properties.get(WebFetcher.PROPERTY_TIMEOUT)));
		configValues.put(WebFetcher.CONFIG_THREADS.name(),
				new Long((String) properties.get(WebFetcher.PROPERTY_THREADS)));
		configValues.put(WebFetcher.CONFIG_MAX_PAGES.name(),
				new Long((String) properties.get(WebFetcher.PROPERTY_MAX_PAGES)));

		Configuration config = new ConfigurationImpl(configValues);

		secondConfigValues.put(WebFetcher.CONFIG_URLS.name(),
				Arrays.asList(((String) properties.get("secondJobUrls"))));
		secondConfigValues.put(WebFetcher.CONFIG_DATA_FOLDER.name(), properties.get(WebFetcher.PROPERTY_DATAFOLDER));
		secondConfigValues.put(WebFetcher.CONFIG_MAX_PAGES.name(),
				new Long((String) properties.get(WebFetcher.PROPERTY_MAX_PAGES)));

		Configuration secondConf = new ConfigurationImpl(secondConfigValues);

		WebFetcher secondWebFetcher = new WebFetcher("secondTest-id", secondConf, null);
		WebFetcher webFetcher = new WebFetcher("test-id", config, null);
		webFetcher.stopped = true;

		TestConsumer testConsumer = new TestConsumer();
		TestConsumer secondTestConsumer = new TestConsumer();

		System.out.println("---------------calling webfetcher start");
		webFetcher.start(testConsumer);
		System.out.println("---------------calling second webfetcher start");
		secondWebFetcher.start(secondTestConsumer);

		List<Map<String, Object>> events = testConsumer.getEvents();

		Assert.assertEquals(0, events.size());
		Thread.sleep(60000);
		webFetcher.stop();
		secondWebFetcher.stop();

		System.out.println("Testging should end : ");
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

package eu.wajja.input.fetcher;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.logstash.plugins.ConfigurationImpl;

import co.elastic.logstash.api.Configuration;

public class FilesystemFetcherTest {

	private Properties properties;

	@Before
	public void intialize() throws IOException {

		properties = new Properties();
		properties.load(this.getClass().getClassLoader().getResourceAsStream("test.properties"));
	}

	@Ignore
	@Test
	public void filesystemFetcherTest() throws IOException {

		Map<String, Object> configValues = new HashMap<>();
		configValues.put(FilesystemFetcher.CONFIG_PATHS.name(), Arrays.asList("/upload/folder"));
		configValues.put(FilesystemFetcher.CONFIG_DATA_FOLDER.name(), Files.createTempDirectory("tmp").toFile().getAbsolutePath());
		configValues.put(FilesystemFetcher.CONFIG_EXCLUDE.name(), Arrays.asList(".jar"));
		configValues.put(FilesystemFetcher.CONFIG_THREAD_POOL_SIZE.name(), 1l);

		Configuration config = new ConfigurationImpl(configValues);
		FilesystemFetcher fetcher = new FilesystemFetcher("test-id", config, null);

		TestConsumer testConsumer = new TestConsumer();
		fetcher.start(testConsumer);

		List<Map<String, Object>> events = testConsumer.getEvents();

		Assert.assertEquals(1, events.size());

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

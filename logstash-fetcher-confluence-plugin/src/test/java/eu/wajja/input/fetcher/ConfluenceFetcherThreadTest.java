package eu.wajja.input.fetcher;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.http.client.HttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class ConfluenceFetcherThreadTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfluenceFetcherThreadTest.class);

	@Mock
	private Consumer<Map<String, Object>> consumer;
	
	@Test
	public void testWebFetcher() {

		ClassLoader classLoader = getClass().getClassLoader();
		URL path = this.getClass().getResource("../../../../../");
		
		
		Consumer<Map<String, Object>> consumer = Mockito.mock(Consumer.class);
		String url = "http://#";
		String username = "username";
		String password = "password";
		String dataFolder = null;
		Integer pageLimit = 10;
		List<String> spaces = Arrays.asList("TEST");
		HttpClient httpClient = Mockito.mock(HttpClient.class);

		ConfluenceFetcherThread confluenceFetcherThread = new ConfluenceFetcherThread(consumer, url, username, password, dataFolder, pageLimit, spaces, httpClient);
		confluenceFetcherThread.run();
		
//		Map<String, Object> configValues = new HashMap<>();
//		configValues.put(ConfluenceFetcher.CONFIG_THREADS.name(), 1l);
//		configValues.put(ConfluenceFetcher.CONFIG_USERNAME.name(), "mahytom");
//		configValues.put(ConfluenceFetcher.CONFIG_PASSWORD.name(), "Internet2014");
//		configValues.put(ConfluenceFetcher.CONFIG_DATA_FOLDER.name(), "C:/Users/mail/confluence-data/");
//		configValues.put(ConfluenceFetcher.CONFIG_URLS.name(), Arrays.asList("https://webgate.ec.europa.eu/CITnet/confluence/"));
//		configValues.put(ConfluenceFetcher.CONFIG_SPACES.name(), Arrays.asList("SEARCH"));
//
//		Configuration config = new ConfigurationImpl(configValues);
//		ConfluenceFetcher input = new ConfluenceFetcher(UUID.randomUUID().toString(), config, null);
//		TestConsumer testConsumer = new TestConsumer();
//		input.start(testConsumer);
//
//		List<Map<String, Object>> events = testConsumer.getEvents();
//
//		LOGGER.info("-> " + events.size());

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

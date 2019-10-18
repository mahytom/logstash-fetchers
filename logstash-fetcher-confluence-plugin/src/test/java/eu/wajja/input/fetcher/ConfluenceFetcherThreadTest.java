package eu.wajja.input.fetcher;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;

import org.apache.http.client.HttpClient;
import org.junit.Before;
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

	private Properties properties;

	@Before
	public void intialize() throws IOException {

		properties = new Properties();
		properties.load(this.getClass().getClassLoader().getResourceAsStream("test.properties"));
	}

	@Test
	public void testWebFetcher() {

		String url = "http://#";
		String username = "username";
		String password = "password";
		String dataFolder = null;
		Integer pageLimit = 10;
		List<String> spaces = Arrays.asList("TEST");
		HttpClient httpClient = Mockito.mock(HttpClient.class);

		ConfluenceFetcherThread confluenceFetcherThread = new ConfluenceFetcherThread(consumer, url, username, password, dataFolder, pageLimit, spaces, httpClient);
		confluenceFetcherThread.run();

	}

}

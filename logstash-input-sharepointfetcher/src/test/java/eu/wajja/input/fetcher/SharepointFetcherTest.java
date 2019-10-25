package eu.wajja.input.fetcher;

import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class SharepointFetcherTest {

	private Properties properties;

	@Before
	public void intialize() throws IOException {

		properties = new Properties();
		properties.load(this.getClass().getClassLoader().getResourceAsStream("test.properties"));
	}
	
	@Ignore
	@Test
	public void testWebFetcher() {

		// Do your test here
	}

}

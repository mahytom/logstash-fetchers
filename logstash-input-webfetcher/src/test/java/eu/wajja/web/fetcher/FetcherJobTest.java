package eu.wajja.web.fetcher;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.wajja.web.fetcher.controller.ProxyController;
import eu.wajja.web.fetcher.controller.URLController;
import eu.wajja.web.fetcher.model.Result;

@RunWith(MockitoJUnitRunner.class)
public class FetcherJobTest {

	@InjectMocks
	private FetcherJob fetcherJob;

	@Mock
	private ProxyController proxyController;

	@Mock
	private URLController urlController;

	@Mock
	private JobDetail jobDetail;

	@Mock
	private JobExecutionContext jobExecutionContext;

	@Mock
	private Consumer<Map<String, Object>> consumer;

	private JobDataMap jobDataMap;

	@Before
	public void intialize() throws IOException {

		Mockito.when(jobExecutionContext.getJobDetail()).thenReturn(jobDetail);

		ObjectMapper mapper = new ObjectMapper();
		String[] excludeData = mapper.readValue("[\".*.((PNG)|(png)|(ico)|(RSS)|(rss.xml)|(rss)|(mp3)|(png)|(PNG)|(xml)|(ico)|(css)|(js)|(mp4)|(jpg)|(JPG)|(jpeg)|(gif)|(zip)|(svg)|(splash_)|(2nd-language)|(site-language_)|(whatsapp)).*\",\".*(page=).*\"]", String[].class);
		String[] excludeLink = mapper.readValue("[\".*.((js)|(css)|(ico)|(whatsapp)).*\"]", String[].class);

		jobDataMap = new JobDataMap();
		jobDataMap.put(WebFetcher.PROPERTY_MAX_DEPTH, 1l);
		jobDataMap.put(WebFetcher.PROPERTY_MAX_PAGES, 10l);
		jobDataMap.put(WebFetcher.PROPERTY_CHROME_DRIVERS, Arrays.asList("http://localhost:3000"));
		jobDataMap.put(WebFetcher.PROPERTY_THREAD_ID, "UNIT_TESTS");
		jobDataMap.put(WebFetcher.PROPERTY_EXCLUDE_DATA, Arrays.asList(excludeData));
		jobDataMap.put(WebFetcher.PROPERTY_EXCLUDE_LINK, Arrays.asList(excludeLink));
		jobDataMap.put(WebFetcher.PROPERTY_PROXY_PORT, 80l);
		jobDataMap.put(WebFetcher.PROPERTY_TIMEOUT, 300l);
		jobDataMap.put(WebFetcher.PROPERTY_CONSUMER, consumer);

		Mockito.when(jobDetail.getJobDataMap()).thenReturn(jobDataMap);

	}

	@Test
	public void jpgExtensionTesting() throws JobExecutionException {

		String url = "https://ec.europa.eu/belgium/sites/belgium/files/og_image/poster_drapeau_europeen.jpg";
		jobDataMap.put(WebFetcher.PROPERTY_READ_ROBOT, false);
		jobDataMap.put(WebFetcher.PROPERTY_URLS, Arrays.asList(url));

		Result result = new Result();
		result.setContent("<html></html>".getBytes());
		result.setUrl(url);

		Mockito.when(urlController.getURL(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(result);

		fetcherJob.execute(jobExecutionContext);

		Mockito.verify(consumer, Mockito.never()).accept(Mockito.anyMap());

	}
}

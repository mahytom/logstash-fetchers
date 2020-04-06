package eu.wajja.web.fetcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import eu.wajja.web.fetcher.controller.ProxyController;
import eu.wajja.web.fetcher.controller.URLController;
import eu.wajja.web.fetcher.model.Result;

@RunWith(MockitoJUnitRunner.class)
public class WebFetcherLogicTest {

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

	private String chromeDriver = "http://localhost:3000";

	@Before
	public void intialize() {

		Mockito.when(jobExecutionContext.getJobDetail()).thenReturn(jobDetail);

		jobDataMap = new JobDataMap();
		jobDataMap.put(WebFetcher.PROPERTY_MAX_DEPTH, 1l);
		jobDataMap.put(WebFetcher.PROPERTY_MAX_PAGES, 10l);
		jobDataMap.put(WebFetcher.PROPERTY_CHROME_DRIVERS, Arrays.asList(chromeDriver));
		jobDataMap.put(WebFetcher.PROPERTY_THREAD_ID, "UNIT_TESTS");
		jobDataMap.put(WebFetcher.PROPERTY_EXCLUDE_DATA, new ArrayList<>());
		jobDataMap.put(WebFetcher.PROPERTY_EXCLUDE_LINK, new ArrayList<>());
		jobDataMap.put(WebFetcher.PROPERTY_PROXY_PORT, 80l);
		jobDataMap.put(WebFetcher.PROPERTY_TIMEOUT, 300l);
		jobDataMap.put(WebFetcher.PROPERTY_CONSUMER, consumer);

		Mockito.when(jobDetail.getJobDataMap()).thenReturn(jobDataMap);

	}

	/**
	 * Gets all children
	 * 
	 * @throws JobExecutionException
	 * @throws IOException
	 */
	@Test
	public void robotsCrawlEuropaLogicSimpleTest() throws JobExecutionException {

		String rootUrl = "https://ec.europa.eu/digital-single-market";

		jobDataMap.put(WebFetcher.PROPERTY_READ_ROBOT, false);
		jobDataMap.put(WebFetcher.PROPERTY_URLS, Arrays.asList(rootUrl));

		String content = "<html>"
				+ "<a href=\"" + rootUrl + "/page1\"></a>"
				+ "<a href=\"" + rootUrl + "/page2\"></a>"
				+ "</html>";

		Mockito.when(urlController.getURL(Mockito.eq(rootUrl), Mockito.eq(rootUrl), Mockito.eq(chromeDriver))).thenReturn(getMockResult(rootUrl, rootUrl, content));
		Mockito.when(urlController.getURL(Mockito.eq(rootUrl + "/page1"), Mockito.eq(rootUrl), Mockito.eq(chromeDriver))).thenReturn(getMockResult(rootUrl + "/page1", rootUrl, content));
		Mockito.when(urlController.getURL(Mockito.eq(rootUrl + "/page2"), Mockito.eq(rootUrl), Mockito.eq(chromeDriver))).thenReturn(getMockResult(rootUrl + "/page2", rootUrl, content));

		fetcherJob.execute(jobExecutionContext);

		Mockito.verify(urlController, Mockito.times(1)).getURL(Mockito.eq(rootUrl + "/page1"), Mockito.eq(rootUrl), Mockito.eq(chromeDriver));
		Mockito.verify(urlController, Mockito.times(1)).getURL(Mockito.eq(rootUrl + "/page2"), Mockito.eq(rootUrl), Mockito.eq(chromeDriver));

		ArgumentCaptor<Map> argument = ArgumentCaptor.forClass(Map.class);
		Mockito.verify(consumer, Mockito.times(3)).accept(argument.capture());

		List<String> urls = argument.getAllValues().stream().map(u -> (String) u.get("url")).collect(Collectors.toList());

		Assert.assertTrue(urls.stream().anyMatch(u -> u.equals(rootUrl)));
		Assert.assertTrue(urls.stream().anyMatch(u -> u.equals(rootUrl + "/page1")));
		Assert.assertTrue(urls.stream().anyMatch(u -> u.equals(rootUrl + "/page2")));

	}

	/**
	 * Gets 1 child, ignore the other one
	 * 
	 * @throws JobExecutionException
	 * @throws IOException
	 */
	@Test
	public void robotsCrawlEuropaLogicWithOneFailedTest() throws JobExecutionException {

		String rootUrl = "https://ec.europa.eu/digital-single-market";

		jobDataMap.put(WebFetcher.PROPERTY_READ_ROBOT, false);
		jobDataMap.put(WebFetcher.PROPERTY_URLS, Arrays.asList(rootUrl));

		String content = "<html>"
				+ "<a href=\"" + rootUrl + "/page1\"></a>"
				+ "<a href=\"https://ec.europa.eu/digital-single/page2\"></a>"
				+ "</html>";

		Mockito.when(urlController.getURL(Mockito.eq(rootUrl), Mockito.eq(rootUrl), Mockito.eq(chromeDriver))).thenReturn(getMockResult(rootUrl, rootUrl, content));
		Mockito.when(urlController.getURL(Mockito.eq(rootUrl + "/page1"), Mockito.eq(rootUrl), Mockito.eq(chromeDriver))).thenReturn(getMockResult(rootUrl + "/page1", rootUrl, content));
		;

		fetcherJob.execute(jobExecutionContext);

		Mockito.verify(urlController, Mockito.times(1)).getURL(Mockito.eq(rootUrl + "/page1"), Mockito.eq(rootUrl), Mockito.eq(chromeDriver));
		Mockito.verify(urlController, Mockito.never()).getURL(Mockito.eq(rootUrl + "/page2"), Mockito.eq(rootUrl), Mockito.eq(chromeDriver));

		ArgumentCaptor<Map> argument = ArgumentCaptor.forClass(Map.class);
		Mockito.verify(consumer, Mockito.times(2)).accept(argument.capture());

		List<String> urls = argument.getAllValues().stream().map(u -> (String) u.get("url")).collect(Collectors.toList());

		Assert.assertTrue(urls.stream().anyMatch(u -> u.equals(rootUrl)));
		Assert.assertTrue(urls.stream().anyMatch(u -> u.equals(rootUrl + "/page1")));

	}

	@Test
	public void robotsCrawlEuropaLogicDuplicateTest() throws JobExecutionException {

		String rootUrl = "https://ec.europa.eu/digital-single-market";

		jobDataMap.put(WebFetcher.PROPERTY_READ_ROBOT, false);
		jobDataMap.put(WebFetcher.PROPERTY_URLS, Arrays.asList(rootUrl));

		String content = "<html>"
				+ "<a href=\"" + rootUrl + "/page1\"></a>"
				+ "<a href=\"" + rootUrl + "/page2\"></a>"
				+ "<a href=\"" + rootUrl + "/page2/\"></a>"
				+ "<a href=\"" + rootUrl + "/page2\"></a>"
				+ "</html>";

		Mockito.when(urlController.getURL(Mockito.eq(rootUrl), Mockito.eq(rootUrl), Mockito.eq(chromeDriver))).thenReturn(getMockResult(rootUrl, rootUrl, content));
		Mockito.when(urlController.getURL(Mockito.eq(rootUrl + "/page1"), Mockito.eq(rootUrl), Mockito.eq(chromeDriver))).thenReturn(getMockResult(rootUrl + "/page1", rootUrl, content));
		Mockito.when(urlController.getURL(Mockito.eq(rootUrl + "/page2"), Mockito.eq(rootUrl), Mockito.eq(chromeDriver))).thenReturn(getMockResult(rootUrl + "/page2", rootUrl, content));

		fetcherJob.execute(jobExecutionContext);

		Mockito.verify(urlController, Mockito.times(1)).getURL(Mockito.eq(rootUrl + "/page1"), Mockito.eq(rootUrl), Mockito.eq(chromeDriver));
		Mockito.verify(urlController, Mockito.times(1)).getURL(Mockito.eq(rootUrl + "/page2"), Mockito.eq(rootUrl), Mockito.eq(chromeDriver));

		ArgumentCaptor<Map> argument = ArgumentCaptor.forClass(Map.class);
		Mockito.verify(consumer, Mockito.times(3)).accept(argument.capture());

		List<String> urls = argument.getAllValues().stream().map(u -> (String) u.get("url")).collect(Collectors.toList());

		Assert.assertTrue(urls.stream().anyMatch(u -> u.equals(rootUrl)));
		Assert.assertTrue(urls.stream().anyMatch(u -> u.equals(rootUrl + "/page1")));
		Assert.assertTrue(urls.stream().anyMatch(u -> u.equals(rootUrl + "/page2")));

	}

	/**
	 * removal of / at the end of urls
	 * 
	 * @throws JobExecutionException
	 * @throws IOException
	 */
	@Test
	public void robotsCrawlEuropaLogicSlashEscapeTest() throws JobExecutionException {

		String rootUrl = "https://ec.europa.eu/digital-single-market";

		jobDataMap.put(WebFetcher.PROPERTY_READ_ROBOT, false);
		jobDataMap.put(WebFetcher.PROPERTY_URLS, Arrays.asList("https://ec.europa.eu/digital-single-market/"));

		String content = "<html>"
				+ "<a href=\"" + rootUrl + "/page1\"></a>"
				+ "<a href=\"" + rootUrl + "/page2\"></a>"
				+ "<a href=\"" + rootUrl + "/page2/\"></a>"
				+ "<a href=\"" + rootUrl + "/page2\"></a>"
				+ "</html>";

		Mockito.when(urlController.getURL(Mockito.eq(rootUrl), Mockito.eq(rootUrl), Mockito.eq(chromeDriver))).thenReturn(getMockResult(rootUrl, rootUrl, content));
		Mockito.when(urlController.getURL(Mockito.eq(rootUrl + "/page1"), Mockito.eq(rootUrl), Mockito.eq(chromeDriver))).thenReturn(getMockResult(rootUrl + "/page1", rootUrl, content));
		Mockito.when(urlController.getURL(Mockito.eq(rootUrl + "/page2"), Mockito.eq(rootUrl), Mockito.eq(chromeDriver))).thenReturn(getMockResult(rootUrl + "/page2", rootUrl, content));

		fetcherJob.execute(jobExecutionContext);

		Mockito.verify(urlController, Mockito.times(1)).getURL(Mockito.eq(rootUrl + "/page1"), Mockito.eq(rootUrl), Mockito.eq(chromeDriver));
		Mockito.verify(urlController, Mockito.times(1)).getURL(Mockito.eq(rootUrl + "/page2"), Mockito.eq(rootUrl), Mockito.eq(chromeDriver));
		Mockito.verify(urlController, Mockito.never()).getURL(Mockito.eq(rootUrl + "/page2/"), Mockito.eq(rootUrl), Mockito.eq(chromeDriver));

		ArgumentCaptor<Map> argument = ArgumentCaptor.forClass(Map.class);
		Mockito.verify(consumer, Mockito.times(3)).accept(argument.capture());

		List<String> urls = argument.getAllValues().stream().map(u -> (String) u.get("url")).collect(Collectors.toList());

		Assert.assertTrue(urls.stream().anyMatch(u -> u.equals(rootUrl)));
		Assert.assertTrue(urls.stream().anyMatch(u -> u.equals(rootUrl + "/page1")));
		Assert.assertTrue(urls.stream().anyMatch(u -> u.equals(rootUrl + "/page2")));

	}

	private Result getMockResult(String currentUrl, String rootUrl, String content) {

		Result result = new Result();
		result.setUrl(currentUrl);
		result.setRootUrl(rootUrl);
		result.getHeaders().put("Content-Type", Arrays.asList("text/html"));
		result.setContent(content.getBytes());

		return result;
	}
}

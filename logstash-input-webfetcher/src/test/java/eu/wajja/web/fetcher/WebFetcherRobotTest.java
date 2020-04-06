//package eu.wajja.web.fetcher;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Map;
//import java.util.Set;
//import java.util.function.Consumer;
//
//import org.apache.commons.io.IOUtils;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.runners.MockitoJUnitRunner;
//import org.quartz.JobDataMap;
//import org.quartz.JobDetail;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//
//import eu.wajja.web.fetcher.controller.ProxyController;
//import eu.wajja.web.fetcher.controller.URLController;
//import eu.wajja.web.fetcher.model.Result;
//
//@RunWith(MockitoJUnitRunner.class)
//public class WebFetcherRobotTest {
//
//	@InjectMocks
//	private FetcherJob fetcherJob;
//
//	@Mock
//	private ProxyController proxyController;
//
//	@Mock
//	private URLController urlController;
//
//	@Mock
//	private JobDetail jobDetail;
//
//	@Mock
//	private JobExecutionContext jobExecutionContext;
//
//	@Mock
//	private Consumer<Map<String, Object>> consumer;
//
//	private JobDataMap jobDataMap;
//
//	@Before
//	public void intialize() {
//
//		Mockito.when(jobExecutionContext.getJobDetail()).thenReturn(jobDetail);
//
//		jobDataMap = new JobDataMap();
//		jobDataMap.put(WebFetcher.PROPERTY_THREADS, 1l);
//		jobDataMap.put(WebFetcher.PROPERTY_MAX_DEPTH, 1l);
//		jobDataMap.put(WebFetcher.PROPERTY_MAX_PAGES, 10l);
//		jobDataMap.put(WebFetcher.PROPERTY_THREAD_ID, "UNIT_TESTS");
//		jobDataMap.put(WebFetcher.PROPERTY_JAVASCRIPT, false);
//		jobDataMap.put(WebFetcher.PROPERTY_EXCLUDE_DATA, new ArrayList<>());
//		jobDataMap.put(WebFetcher.PROPERTY_EXCLUDE_LINK, new ArrayList<>());
//		jobDataMap.put(WebFetcher.PROPERTY_PROXY_PORT, 80l);
//		jobDataMap.put(WebFetcher.PROPERTY_TIMEOUT, 300l);
//		jobDataMap.put(WebFetcher.PROPERTY_CONSUMER, consumer);
//
//		Mockito.when(jobDetail.getJobDataMap()).thenReturn(jobDataMap);
//
//	}
//
//	@Test
//	public void robotsParseEuropaTest() throws JobExecutionException, IOException {
//
//		jobDataMap.put(WebFetcher.PROPERTY_READ_ROBOT, true);
//		jobDataMap.put(WebFetcher.PROPERTY_URL, Arrays.asList("https://ec.europa.eu/maritimeaffairs/press"));
//
//		byte[] content = IOUtils.toByteArray(this.getClass().getClassLoader().getResourceAsStream("robots1.txt"));
//
//		Result result = new Result();
//		result.setContent(content);
//
//		Mockito.when(urlController.getURL(Mockito.eq("https://ec.europa.eu/robots.txt"), Mockito.eq("https://ec.europa.eu/maritimeaffairs/press"))).thenReturn(result);
//
//		fetcherJob.execute(jobExecutionContext);
//
//		Map<String, Set<String>> disallowedLocations = fetcherJob.disallowedLocations;
//
//		Assert.assertTrue(disallowedLocations.get("Baiduspider").contains("/eurostat/tgm/"));
//		Assert.assertTrue(disallowedLocations.get("Baiduspider").contains("/europeaid/prag/"));
//
//		Assert.assertTrue(disallowedLocations.get("*").contains("/archives/"));
//		Assert.assertTrue(disallowedLocations.get("*").contains("/employment_social/anticipedia/xwiki/bin/viewrev/"));
//		Assert.assertTrue(disallowedLocations.get("*").contains("/clima/sites/registry/"));
//		Assert.assertTrue(disallowedLocations.get("*").contains("/employment_social/anticipedia/xwiki/bin/commentadd/"));
//		Assert.assertTrue(disallowedLocations.get("*").contains("/transparencyregister/"));
//		Assert.assertTrue(disallowedLocations.get("*").contains("/digital-single-market/country/"));
//		Assert.assertTrue(disallowedLocations.get("*").contains("/pmo/contact/en/staff/"));
//
//		Map<String, Set<String>> allowedLocations = fetcherJob.allowedLocations;
//
//		Assert.assertTrue(allowedLocations.get("Twitterbot").contains("/digital-single-market/en/files"));
//		Assert.assertTrue(allowedLocations.get("Twitterbot").contains("/digital-single-market/files"));
//
//		Map<String, Set<String>> sitemapLocations = fetcherJob.sitemapLocations;
//		Assert.assertTrue(sitemapLocations.get("*").contains("https://example.com/sitemap.xml"));
//	}
//
//	@Test
//	public void robotsCrawlEuropaTest() throws JobExecutionException, IOException {
//
//		String rootUrl = "https://ec.europa.eu/digital-single-market";
//
//		jobDataMap.put(WebFetcher.PROPERTY_READ_ROBOT, true);
//		jobDataMap.put(WebFetcher.PROPERTY_URL, Arrays.asList("https://ec.europa.eu/digital-single-market"));
//
//		byte[] content = IOUtils.toByteArray(this.getClass().getClassLoader().getResourceAsStream("robots2.txt"));
//
//		Result result = new Result();
//		result.setContent(content);
//		result.setRootUrl(rootUrl);
//		result.getHeaders().put("Content-Type", Arrays.asList("text/html"));
//
//		Mockito.when(urlController.getURL(Mockito.eq("https://ec.europa.eu/robots.txt"), Mockito.eq(rootUrl))).thenReturn(result);
//
//		Result result1 = new Result();
//		result1.setUrl(rootUrl);
//		result1.setRootUrl(rootUrl);
//		result1.getHeaders().put("Content-Type", Arrays.asList("text/html"));
//		result1.setContent(("<html>"
//				+ "<a href=\"" + rootUrl + "/page1\"></a>"
//				+ "<a href=\"" + rootUrl + "/page2\"></a>"
//				+ "<a href=\"" + rootUrl + "/sites/1\"></a>"
//				+ "</html>").getBytes());
//
//		Mockito.when(urlController.getURL(Mockito.eq(rootUrl), Mockito.eq(rootUrl))).thenReturn(result1);
//		Mockito.when(urlController.getURL(Mockito.eq(rootUrl + "/page1"), Mockito.eq(rootUrl))).thenReturn(result1);
//		Mockito.when(urlController.getURL(Mockito.eq(rootUrl + "/page2"), Mockito.eq(rootUrl))).thenReturn(result1);
//		Mockito.when(urlController.getURL(Mockito.eq(rootUrl + "/sites/1"), Mockito.eq(rootUrl))).thenReturn(result1);
//
//		fetcherJob.execute(jobExecutionContext);
//
//		Mockito.verify(urlController, Mockito.times(1)).getURL(Mockito.eq(rootUrl + "/page1"), Mockito.eq(rootUrl));
//		Mockito.verify(urlController, Mockito.times(1)).getURL(Mockito.eq(rootUrl + "/page2"), Mockito.eq(rootUrl));
//		Mockito.verify(urlController, Mockito.never()).getURL(Mockito.eq(rootUrl + "/sites/1"), Mockito.eq(rootUrl));
//
//	}
//
//}

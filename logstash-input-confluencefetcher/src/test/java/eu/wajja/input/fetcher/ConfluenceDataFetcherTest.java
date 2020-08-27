package eu.wajja.input.fetcher;

import java.io.IOException;
import java.net.URLConnection;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.atlassian.confluence.api.model.Expansion;
import com.atlassian.confluence.api.model.content.Content;
import com.atlassian.confluence.api.model.content.ContentStatus;
import com.atlassian.confluence.api.model.content.ContentType;
import com.atlassian.confluence.api.model.content.Space;
import com.atlassian.confluence.api.model.content.Version;
import com.atlassian.confluence.api.model.content.id.ContentId;
import com.atlassian.confluence.api.model.link.LinkType;
import com.atlassian.confluence.api.model.pagination.PageRequest;
import com.atlassian.confluence.api.model.pagination.PageResponse;
import com.atlassian.confluence.api.model.people.Person;
import com.atlassian.confluence.api.model.permissions.ContentRestrictionsPageResponse;
import com.atlassian.confluence.rest.client.RemoteAttachmentServiceImpl;
import com.atlassian.confluence.rest.client.RemoteAttachmentServiceImpl.RemoteAttachmentFinderImpl;
import com.atlassian.confluence.rest.client.RemoteContentRestrictionServiceImpl;
import com.atlassian.confluence.rest.client.RemoteContentServiceImpl;
import com.atlassian.confluence.rest.client.RemoteContentServiceImpl.RemoteContentFinderImpl;
import com.atlassian.confluence.rest.client.RemoteSpaceServiceImpl;
import com.atlassian.confluence.rest.client.RemoteSpaceServiceImpl.RemoteSpaceFinderImpl;
import com.atlassian.confluence.rpc.soap.beans.RemoteContentPermission;
import com.atlassian.confluence.rpc.soap.beans.RemoteSpacePermissionSet;
import com.atlassian.fugue.Option;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.util.concurrent.Promise;

import eu.wajja.input.fetcher.controller.ConfluenceDataFetcher;
import eu.wajja.input.fetcher.soap.confluence.ConfluenceSoapService;
import eu.wajja.input.fetcher.utils.UrlHelper;

@RunWith(MockitoJUnitRunner.class)
public class ConfluenceDataFetcherTest {

	@InjectMocks
	private ConfluenceDataFetcher confluenceDataFetcher;

	@Mock
	private JobDetail jobDetail;

	@Mock
	private JobExecutionContext context;

	@Mock
	private Consumer<Map<String, Object>> consumer;

	@Mock
	private UrlHelper urlHelper;

	/**
	 * RemoteSpaceServiceImpl
	 */

	@Mock
	private RemoteSpaceServiceImpl remoteSpaceServiceImpl;

	@Mock
	private RemoteSpaceFinderImpl remoteSpaceFinderImpl;

	@Mock
	private Promise<PageResponse<Space>> promisePageResponse;

	@Mock
	private PageResponse<Space> pageResponse;

	@Mock
	private PageResponse<Space> pageEmptyResponse;

	/**
	 * RemoteContentServiceImpl
	 */
	@Mock
	private RemoteContentServiceImpl remoteContentServiceImpl;

	@Mock
	private RemoteContentFinderImpl remoteContentFinderImpl;

	@Mock
	private Promise<PageResponse<Content>> promiseContentResponse;

	@Mock
	private PageResponse<Content> pageContentResponse;

	@Mock
	private PageResponse<Content> pageContentEmptyResponse;

	/**
	 * RemoteContentRestrictionServiceImpl
	 */

	@Mock
	private RemoteContentRestrictionServiceImpl remoteContentRestrictionServiceImpl;

	@Mock
	private Promise<ContentRestrictionsPageResponse> contentPromiseRestrictionsPageResponse;

	@Mock
	private ContentRestrictionsPageResponse contentRestrictionsPageResponse;

	/**
	 * RemoteAttachmentServiceImpl
	 */

	@Mock
	private RemoteAttachmentServiceImpl remoteAttachmentServiceImpl;

	@Mock
	private RemoteAttachmentFinderImpl remoteAttachmentFinderImpl;

	@Mock
	private Promise<PageResponse<Content>> pageAttachmentContentResponse;

	@Mock
	private PageResponse<Content> pageContentAttachmentResponse;

	@Mock
	private PageResponse<Content> pageContentAttachmentEmptyResponse;

	@Mock
	private ConfluenceSoapService confluenceSoapService;
	
	@Mock
	private RemoteSpacePermissionSet remoteSpacePermissionSet;
	
	private Person person;
	private JobDataMap jobDataMap = new JobDataMap();

	@Before
	public void intialize() throws RemoteException {

		Mockito.when(context.getJobDetail()).thenReturn(jobDetail);

		jobDataMap.put("soapService", confluenceSoapService);
		jobDataMap.put("remoteSpaceServiceImpl", remoteSpaceServiceImpl);
		jobDataMap.put("remoteContentServiceImpl", remoteContentServiceImpl);
		jobDataMap.put("remoteAttachmentServiceImpl", remoteAttachmentServiceImpl);
		jobDataMap.put("remoteContentRestrictionServiceImpl", remoteContentRestrictionServiceImpl);
		jobDataMap.put("consumer", consumer);
		jobDataMap.put("batchSize", 10l);
		jobDataMap.put("sites", Arrays.asList("SPACE_001"));
		jobDataMap.put("url", "http://localhost/confluence");
		jobDataMap.put("username", "USER_001");
		jobDataMap.put("password", "PASSWORD_001");
		jobDataMap.put("dataSyncThreadSize", 1l);

		Mockito.when(jobDetail.getJobDataMap()).thenReturn(jobDataMap);
		
		Mockito.when(remoteSpaceServiceImpl.find(Mockito.any()))
				.thenReturn(remoteSpaceFinderImpl);
		Mockito.when(remoteSpaceFinderImpl.withKeys(Mockito.any())).thenReturn(remoteSpaceFinderImpl);
		Mockito.when(remoteSpaceFinderImpl.fetchMany(Mockito.any(PageRequest.class))).thenReturn(promisePageResponse);
		Mockito.when(promisePageResponse.claim()).thenReturn(pageResponse).thenReturn(pageEmptyResponse);
		Mockito.when(pageEmptyResponse.getResults()).thenReturn(new ArrayList<>());

		Mockito.when(remoteContentServiceImpl.find(Mockito.any())).thenReturn(remoteContentFinderImpl);
		Mockito.when(remoteContentFinderImpl.withStatus(ContentStatus.CURRENT)).thenReturn(remoteContentFinderImpl);
		Mockito.when(remoteContentFinderImpl.withStatus(ContentStatus.TRASHED)).thenReturn(remoteContentFinderImpl);
		Mockito.when(remoteContentFinderImpl.withSpace(Mockito.any(Space.class))).thenReturn(remoteContentFinderImpl);
		Mockito.when(remoteContentFinderImpl.fetchMany(Mockito.any(ContentType.class), Mockito.any(PageRequest.class))).thenReturn(promiseContentResponse);
		Mockito.when(promiseContentResponse.claim()).thenReturn(pageContentResponse).thenReturn(pageContentEmptyResponse);

		Mockito.when(remoteContentRestrictionServiceImpl.getRestrictions(Mockito.any(ContentId.class), Mockito.any(PageRequest.class))).thenReturn(contentPromiseRestrictionsPageResponse);
		Mockito.when(contentPromiseRestrictionsPageResponse.claim()).thenReturn(contentRestrictionsPageResponse);

		Mockito.when(remoteAttachmentServiceImpl.find(Mockito.any())).thenReturn(remoteAttachmentFinderImpl);
		Mockito.when(remoteAttachmentFinderImpl.withContainerId(Mockito.any(ContentId.class))).thenReturn(remoteAttachmentFinderImpl);
		Mockito.when(remoteAttachmentFinderImpl.fetchMany(Mockito.any(PageRequest.class))).thenReturn(pageAttachmentContentResponse);
		Mockito.when(pageAttachmentContentResponse.claim()).thenReturn(pageContentAttachmentResponse).thenReturn(pageContentAttachmentEmptyResponse);
		Mockito.when(pageContentAttachmentEmptyResponse.getResults()).thenReturn(new ArrayList<>());

		person = new Person(null) {

			@Override
			public Option<UserKey> getUserKey() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Option<String> getOptionalUsername() {
				return Option.some("USER_001");
			}
		};
	}

	@Test
	public void baseContentFetcherTest() throws JobExecutionException {

		List<Space> spaceList = Arrays.asList(Space.builder()
				.id(1l)
				.name("SPACE_001")
				.key("SPACE_001")
				.build());

		Mockito.when(pageResponse.getResults()).thenReturn(spaceList);

		List<Content> contentList = new ArrayList<>();

		contentList.add(Content.builder()
				.id(ContentId.of(ContentType.PAGE, 2l))
				.title("FILE.txt")
				.version(Version.builder()
						.by(person)
						.when(new Date())
						.build())
				.build());

		Mockito.when(pageContentResponse.getResults()).thenReturn(contentList);
		Mockito.when(contentRestrictionsPageResponse.getResults()).thenReturn(new ArrayList<>());
		Mockito.when(pageContentAttachmentResponse.getResults()).thenReturn(new ArrayList<>());

		confluenceDataFetcher.execute(context);

		ArgumentCaptor<Map<String, Object>> argument = ArgumentCaptor.forClass(Map.class);
		Mockito.verify(consumer, Mockito.times(1)).accept(argument.capture());

		Map<String, Object> metadata = argument.getValue();

		Assert.assertTrue(metadata.containsKey("reference"));
		Assert.assertTrue(((String) metadata.get("reference")).equals("2"));
	}

	@Test
	public void contentFetcherIncludeAttachmentTest() throws JobExecutionException {

		jobDataMap.put("dataAttachmentsInclude", Arrays.asList(".*\\.doc"));

		List<Space> spaceList = Arrays.asList(Space.builder()
				.id(1l)
				.name("SPACE_001")
				.key("SPACE_001")
				.build());

		Mockito.when(pageResponse.getResults()).thenReturn(spaceList);

		List<Content> contentList = new ArrayList<>();

		contentList.add(Content.builder()
				.id(ContentId.of(ContentType.PAGE, 2l))
				.title("page_title")
				.version(Version.builder()
						.by(person)
						.when(new Date())
						.build())
				.build());

		Mockito.when(pageContentResponse.getResults()).thenReturn(contentList);
		Mockito.when(contentRestrictionsPageResponse.getResults()).thenReturn(new ArrayList<>());

		List<Content> attachments = new ArrayList<>();

		attachments.add(Content.builder()
				.id(ContentId.of(ContentType.PAGE, 3l))
				.title("attachment.txt")
				.version(Version.builder()
						.by(person)
						.when(new Date())
						.build())
				.build());

		attachments.add(Content.builder()
				.id(ContentId.of(ContentType.PAGE, 4l))
				.title("attachment.doc")
				.version(Version.builder()
						.by(person)
						.when(new Date())
						.build())
				.build());

		Mockito.when(pageContentAttachmentResponse.getResults()).thenReturn(attachments);

		confluenceDataFetcher.execute(context);

		ArgumentCaptor<Map<String, Object>> argument = ArgumentCaptor.forClass(Map.class);
		Mockito.verify(consumer, Mockito.times(2)).accept(argument.capture());

		List<Map<String, Object>> metadata = argument.getAllValues();

		Assert.assertTrue(metadata.stream().anyMatch(x -> x.get("reference").equals("4")));
		Assert.assertTrue(metadata.stream().anyMatch(x -> x.get("title").equals("attachment.doc")));

		Assert.assertTrue(metadata.stream().anyMatch(x -> x.get("reference").equals("2")));
		Assert.assertTrue(metadata.stream().anyMatch(x -> x.get("title").equals("page_title")));
	}

	@Test
	public void contentFetcherExcludeAttachmentTest() throws JobExecutionException {

		jobDataMap.put("dataAttachmentsExclude", Arrays.asList(".*\\.doc"));

		List<Space> spaceList = Arrays.asList(Space.builder()
				.id(1l)
				.name("SPACE_001")
				.key("SPACE_001")
				.build());

		Mockito.when(pageResponse.getResults()).thenReturn(spaceList);

		List<Content> contentList = new ArrayList<>();

		contentList.add(Content.builder()
				.id(ContentId.of(ContentType.PAGE, 2l))
				.title("page_title")
				.version(Version.builder()
						.by(person)
						.when(new Date())
						.build())
				.build());

		Mockito.when(pageContentResponse.getResults()).thenReturn(contentList);
		Mockito.when(contentRestrictionsPageResponse.getResults()).thenReturn(new ArrayList<>());

		List<Content> attachments = new ArrayList<>();

		attachments.add(Content.builder()
				.id(ContentId.of(ContentType.PAGE, 3l))
				.title("attachment.txt")
				.version(Version.builder()
						.by(person)
						.when(new Date())
						.build())
				.build());

		attachments.add(Content.builder()
				.id(ContentId.of(ContentType.PAGE, 4l))
				.title("attachment.doc")
				.version(Version.builder()
						.by(person)
						.when(new Date())
						.build())
				.build());

		Mockito.when(pageContentAttachmentResponse.getResults()).thenReturn(attachments);

		confluenceDataFetcher.execute(context);

		ArgumentCaptor<Map<String, Object>> argument = ArgumentCaptor.forClass(Map.class);
		Mockito.verify(consumer, Mockito.times(2)).accept(argument.capture());

		List<Map<String, Object>> metadata = argument.getAllValues();

		Assert.assertTrue(metadata.stream().anyMatch(x -> x.get("reference").equals("3")));
		Assert.assertTrue(metadata.stream().anyMatch(x -> x.get("title").equals("attachment.txt")));

		Assert.assertTrue(metadata.stream().anyMatch(x -> x.get("reference").equals("2")));
		Assert.assertTrue(metadata.stream().anyMatch(x -> x.get("title").equals("page_title")));
	}

	@Test
	public void contentFetcherExcludePageTest() throws JobExecutionException {

		jobDataMap.put("dataPageExclude", Arrays.asList("~.*"));

		List<Space> spaceList = Arrays.asList(Space.builder()
				.id(1l)
				.name("SPACE_001")
				.key("SPACE_001")
				.build());

		Mockito.when(pageResponse.getResults()).thenReturn(spaceList);

		List<Content> contentList = new ArrayList<>();

		contentList.add(Content.builder()
				.id(ContentId.of(ContentType.PAGE, 2l))
				.title("page_title")
				.version(Version.builder()
						.by(person)
						.when(new Date())
						.build())
				.build());

		contentList.add(Content.builder()
				.id(ContentId.of(ContentType.PAGE, 3l))
				.title("~page_title")
				.version(Version.builder()
						.by(person)
						.when(new Date())
						.build())
				.build());

		Mockito.when(pageContentResponse.getResults()).thenReturn(contentList);
		Mockito.when(contentRestrictionsPageResponse.getResults()).thenReturn(new ArrayList<>());

		Mockito.when(pageContentAttachmentResponse.getResults()).thenReturn(new ArrayList<>());

		confluenceDataFetcher.execute(context);

		ArgumentCaptor<Map<String, Object>> argument = ArgumentCaptor.forClass(Map.class);
		Mockito.verify(consumer, Mockito.times(1)).accept(argument.capture());

		List<Map<String, Object>> metadata = argument.getAllValues();

		Assert.assertTrue(metadata.stream().anyMatch(x -> x.get("reference").equals("2")));
		Assert.assertTrue(metadata.stream().anyMatch(x -> x.get("title").equals("page_title")));
	}

	@Test
	public void contentSpaceExcludePageTest() throws JobExecutionException {

		jobDataMap.put("dataSpaceExclude", Arrays.asList("SPACE_001"));

		List<Space> spaceList = Arrays.asList(Space.builder()
				.id(1l)
				.name("SPACE_001")
				.key("SPACE_001")
				.build(),
				Space.builder()
						.id(1l)
						.name("SPACE_002")
						.key("SPACE_002")
						.build());

		Mockito.when(pageResponse.getResults()).thenReturn(spaceList);

		List<Content> contentList = new ArrayList<>();

		Mockito.when(pageContentResponse.getResults()).thenReturn(contentList).thenReturn(new ArrayList<>());
		confluenceDataFetcher.execute(context);

		ArgumentCaptor<Space> argument = ArgumentCaptor.forClass(Space.class);
		Mockito.verify(remoteContentFinderImpl, Mockito.times(2)).withSpace(argument.capture());

		List<Space> spaces = argument.getAllValues();
		Assert.assertTrue(spaces.stream().allMatch(x -> x.getName().equals("SPACE_002")));
	}

	@Test
	public void contentFetcherExcludeAttachmentSizeTest() throws JobExecutionException, IOException {

		jobDataMap.put("dataAttachmentsMaxSize", 8l);

		List<Space> spaceList = Arrays.asList(Space.builder()
				.id(1l)
				.name("SPACE_001")
				.key("SPACE_001")
				.build());

		Mockito.when(pageResponse.getResults()).thenReturn(spaceList);

		List<Content> contentList = new ArrayList<>();

		contentList.add(Content.builder()
				.id(ContentId.of(ContentType.PAGE, 2l))
				.title("page_title")
				.version(Version.builder()
						.by(person)
						.when(new Date())
						.build())
				.build());

		Mockito.when(pageContentResponse.getResults()).thenReturn(contentList);
		Mockito.when(contentRestrictionsPageResponse.getResults()).thenReturn(new ArrayList<>());

		List<Content> attachments = new ArrayList<>();

		attachments.add(Content.builder()
				.id(ContentId.of(ContentType.PAGE, 3l))
				.title("attachment.txt")
				.addLink(LinkType.DOWNLOAD, "/download_003")
				.version(Version.builder()
						.by(person)
						.when(new Date())
						.build())
				.build());

		attachments.add(Content.builder()
				.id(ContentId.of(ContentType.PAGE, 4l))
				.title("attachment.doc")
				.addLink(LinkType.DOWNLOAD, "/download_004")
				.version(Version.builder()
						.by(person)
						.when(new Date())
						.build())
				.build());

		Mockito.when(pageContentAttachmentResponse.getResults()).thenReturn(attachments);

		URLConnection urlConnection2 = Mockito.mock(URLConnection.class);
		Mockito.when(urlConnection2.getInputStream()).thenReturn(IOUtils.toInputStream("CONTENT"));
		Mockito.when(urlHelper.getUrl("USER_001", "PASSWORD_001", "http://localhost/confluence/download_003")).thenReturn(urlConnection2);

		URLConnection urlConnection1 = Mockito.mock(URLConnection.class);
		Mockito.when(urlConnection1.getInputStream()).thenReturn(IOUtils.toInputStream("CONTENT_001"));
		Mockito.when(urlHelper.getUrl("USER_001", "PASSWORD_001", "http://localhost/confluence/download_004")).thenReturn(urlConnection1);

		confluenceDataFetcher.execute(context);

		ArgumentCaptor<Map<String, Object>> argument = ArgumentCaptor.forClass(Map.class);
		Mockito.verify(consumer, Mockito.times(3)).accept(argument.capture());

		List<Map<String, Object>> metadata = argument.getAllValues();

		Assert.assertTrue(metadata.stream().anyMatch(x -> x.get("reference").equals("3")));
		Assert.assertTrue(metadata.stream().filter(x -> x.get("reference").equals("3")).anyMatch(x -> x.get("title").equals("attachment.txt")));
		Assert.assertTrue(metadata.stream().filter(x -> x.get("reference").equals("3")).anyMatch(x -> ((String) x.get("content")).length() > 0));

		Assert.assertTrue(metadata.stream().anyMatch(x -> x.get("reference").equals("4")));
		Assert.assertTrue(metadata.stream().filter(x -> x.get("reference").equals("4")).anyMatch(x -> x.get("title").equals("attachment.doc")));
		Assert.assertFalse(metadata.stream().filter(x -> x.get("reference").equals("4")).findFirst().orElse(new HashMap<>()).containsKey("content"));

		Assert.assertTrue(metadata.stream().anyMatch(x -> x.get("reference").equals("2")));
		Assert.assertTrue(metadata.stream().anyMatch(x -> x.get("title").equals("page_title")));
	}

}

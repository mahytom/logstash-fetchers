package eu.wajja.input.fetcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.confluence.api.model.pagination.PageRequest;
import com.atlassian.confluence.api.model.pagination.PageResponse;
import com.atlassian.confluence.api.model.people.Group;
import com.atlassian.confluence.api.model.people.Person;
import com.atlassian.confluence.rest.client.RemotePersonServiceImpl;
import com.atlassian.confluence.rest.client.RemotePersonServiceImpl.RemotePersonFinderImpl;
import com.atlassian.confluence.rest.client.remoteservice.people.RemoteGroupService.RemoteGroupFinder;
import com.atlassian.confluence.rest.client.remoteservice.people.RemoteGroupServiceImpl;
import com.atlassian.fugue.Option;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.util.concurrent.Promise;

import eu.wajja.input.fetcher.controller.ConfluenceGroupFetcher;

@RunWith(MockitoJUnitRunner.class)
public class ConfluenceGroupFetcherTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfluenceGroupFetcherTest.class);

	@InjectMocks
	private ConfluenceGroupFetcher confluenceGroupFetcher;

	@Mock
	private JobDetail jobDetail;

	@Mock
	private JobExecutionContext context;

	@Mock
	private Consumer<Map<String, Object>> consumer;

	@Mock
	private RemotePersonServiceImpl remotePersonServiceImpl;

	@Mock
	private RemoteGroupServiceImpl remoteGroupServiceImpl;

	@Mock
	private RemoteGroupFinder remoteGroupFinder;

	@Mock
	private RemotePersonFinderImpl remotePersonFinderImpl;

	@Mock
	private Promise<PageResponse<Group>> promiseGroup;

	@Mock
	private Promise<PageResponse<Person>> promiseUsers;

	@Mock
	private PageResponse<Group> pageGroupResponse;

	@Mock
	private PageResponse<Group> pageGroupEmptyResponse;

	@Mock
	private PageResponse<Person> pageUserResponse;

	@Mock
	private PageResponse<Person> pageUserEmptyResponse;

	private JobDataMap jobDataMap;

	@Before
	public void intialize() {

		Mockito.when(context.getJobDetail()).thenReturn(jobDetail);

		jobDataMap = new JobDataMap();
		jobDataMap.put("remotePersonServiceImpl", remotePersonServiceImpl);
		jobDataMap.put("remoteGroupServiceImpl", remoteGroupServiceImpl);
		jobDataMap.put("consumer", consumer);
		jobDataMap.put("batchSize", 1l);

		Mockito.when(jobDetail.getJobDataMap()).thenReturn(jobDataMap);

	}

	@Test
	public void groupFetcherTest1() throws JobExecutionException {

		/** GROUPS **/

		List<Group> groupResults = new ArrayList<>();
		groupResults.add(new Group("GROUP_001"));
		groupResults.add(new Group("GROUP_002"));
		groupResults.add(new Group("GROUP_003"));

		Mockito.when(remoteGroupServiceImpl.find()).thenReturn(remoteGroupFinder);
		Mockito.when(remoteGroupFinder.fetchMany(Mockito.any(PageRequest.class))).thenReturn(promiseGroup);
		Mockito.when(promiseGroup.claim()).thenReturn(pageGroupResponse).thenReturn(pageGroupEmptyResponse);
		Mockito.when(pageGroupResponse.getResults()).thenReturn(groupResults);
		Mockito.when(pageGroupEmptyResponse.getResults()).thenReturn(new ArrayList<>());

		/** USERS **/

		List<Person> userResults = new ArrayList<>();

		userResults.add(new Person(null) {

			@Override
			public Option<UserKey> getUserKey() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Option<String> getOptionalUsername() {
				return Option.some("USER_001");
			}
		});

		Mockito.when(remotePersonServiceImpl.find()).thenReturn(remotePersonFinderImpl);
		Mockito.when(remotePersonFinderImpl.withMembershipOf(Mockito.any(Group.class))).thenReturn(remotePersonFinderImpl);
		Mockito.when(remotePersonFinderImpl.fetchMany(Mockito.any(PageRequest.class))).thenReturn(promiseUsers);
		Mockito.when(promiseUsers.claim()).thenReturn(pageUserResponse).thenReturn(pageUserEmptyResponse);
		Mockito.when(pageUserResponse.getResults()).thenReturn(userResults);
		Mockito.when(pageUserEmptyResponse.getResults()).thenReturn(new ArrayList<>());

		confluenceGroupFetcher.execute(context);

		ArgumentCaptor<Map> argument = ArgumentCaptor.forClass(Map.class);
		Mockito.verify(consumer).accept(argument.capture());

		Map<String, Object> metadata = argument.getValue();

		Assert.assertTrue(metadata.containsKey("reference"));
		Assert.assertTrue(((String) metadata.get("reference")).equals("USER_001"));

		Assert.assertTrue(metadata.containsKey("groups"));
		Assert.assertTrue(((List<String>) metadata.get("groups")).get(0).equals("group_002"));
	}

}

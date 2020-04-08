package eu.wajja.input.fetcher.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.confluence.api.model.content.Space;
import com.atlassian.confluence.api.model.pagination.PageRequest;
import com.atlassian.confluence.api.model.pagination.PageResponse;
import com.atlassian.confluence.api.model.pagination.SimplePageRequest;
import com.atlassian.confluence.api.model.people.Group;
import com.atlassian.confluence.api.model.people.Person;
import com.atlassian.confluence.rest.client.RemotePersonServiceImpl;
import com.atlassian.confluence.rest.client.RemoteSpaceServiceImpl;
import com.atlassian.confluence.rest.client.RemotePersonServiceImpl.RemotePersonFinderImpl;
import com.atlassian.confluence.rest.client.authentication.AuthenticatedWebResourceProvider;
import com.google.common.util.concurrent.ListeningExecutorService;

public class ConfluenceDataFetcher implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfluenceDataFetcher.class);

	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		
	}
	
	public void fetchAllContent(AuthenticatedWebResourceProvider provider, ListeningExecutorService executor, Consumer<Map<String, Object>> consumer, Long batchSize) {

		RemoteSpaceServiceImpl spaceServiceImpl = new RemoteSpaceServiceImpl(provider, executor);

		List<String> sites = getSites(spaceServiceImpl, batchSize);

		sites.stream().forEach(s -> {
			LOGGER.info("Site : " + s);
		});

//		RemoteContentServiceImpl remoteContentServiceImpl = new RemoteContentServiceImpl(provider, executor);
//		RemoteContentFinderImpl remoteContentFinderImpl=	(RemoteContentFinderImpl) remoteContentServiceImpl.find();
		
	}
	
	private List<String> getSites(RemoteSpaceServiceImpl spaceServiceImpl, Long batchSize) {

		int size = 0;
		PageRequest pageRequest = new SimplePageRequest(size, batchSize.intValue());

		PageResponse<Space> pageResponse = (PageResponse<Space>) spaceServiceImpl.find().fetchMany(pageRequest).claim();

		List<String> sites = new ArrayList<>();

		while (!pageResponse.getResults().isEmpty()) {

			sites.addAll(pageResponse.getResults().stream().map(s -> s.getKey()).filter(Objects::nonNull).collect(Collectors.toList()));

			size = size + batchSize.intValue();
			pageRequest = new SimplePageRequest(size, batchSize.intValue());
			pageResponse = (PageResponse<Space>) spaceServiceImpl.find().fetchMany(pageRequest).claim();

		}

		return sites;

	}


}

package eu.wajja.input.fetcher.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.confluence.api.model.pagination.PageRequest;
import com.atlassian.confluence.api.model.pagination.PageResponse;
import com.atlassian.confluence.api.model.pagination.SimplePageRequest;
import com.atlassian.confluence.api.model.people.Group;
import com.atlassian.confluence.api.model.people.Person;
import com.atlassian.confluence.api.service.exceptions.NotFoundException;
import com.atlassian.confluence.rest.client.RemotePersonServiceImpl;
import com.atlassian.confluence.rest.client.RemotePersonServiceImpl.RemotePersonFinderImpl;
import com.atlassian.confluence.rest.client.remoteservice.people.RemoteGroupServiceImpl;
import com.sun.jersey.api.client.ClientHandlerException;

import eu.wajja.input.fetcher.enums.Command;

public class ConfluenceGroupFetcher implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfluenceGroupFetcher.class);

	private static final String METADATA_REFERENCE = "reference";
	private static final String METADATA_GROUPS = "groups";
	private static final String METADATA_COMMAND = "command";
	private static final String METADATA_URL = "url";

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		JobDataMap dataMap = context.getJobDetail().getJobDataMap();

		Consumer<Map<String, Object>> consumer = (Consumer<Map<String, Object>>) dataMap.get("consumer");

		RemotePersonServiceImpl remotePersonServiceImpl = (RemotePersonServiceImpl) dataMap.get("remotePersonServiceImpl");
		RemoteGroupServiceImpl groupServiceImpl = (RemoteGroupServiceImpl) dataMap.get("remoteGroupServiceImpl");
		
		Long batchSize = (Long) dataMap.get("batchSize");

		int size = 0;
		PageRequest pageRequestGroups = new SimplePageRequest(size, batchSize.intValue());
		PageResponse<Group> groupPromises = groupServiceImpl.find().fetchMany(pageRequestGroups).claim();

		Map<String, List<String>> groupUser = new HashMap<>();
		Map<String, List<String>> userGroup = new HashMap<>();

		while (!groupPromises.getResults().isEmpty()) {

			try {
				groupPromises.getResults().stream().forEach(group -> {

					try {
						LOGGER.info("Searching group {}", group);
						List<String> users = getUsers(remotePersonServiceImpl, group, batchSize);

						if (!users.isEmpty()) {
							groupUser.put(group.getName(), users);
						}

					} catch (NotFoundException e) {
						LOGGER.info("Failed to find group", e);
					}
				});

			} catch (ClientHandlerException e) {
				LOGGER.info("Failed to retrieve group", e);
			}

			size = size + batchSize.intValue();
			pageRequestGroups = new SimplePageRequest(size, batchSize.intValue());
			groupPromises = groupServiceImpl.find().fetchMany(pageRequestGroups).claim();

		}

		groupUser.entrySet().stream().forEach(g ->

		g.getValue().stream().forEach(u -> {

			if (!userGroup.containsKey(u)) {
				userGroup.put(u, new ArrayList<>());
			}

			userGroup.get(u).add(g.getKey());

		}));

		userGroup.entrySet().stream().forEach(user -> {

			Map<String, Object> metadata = new HashMap<>();

			metadata.put(METADATA_REFERENCE, user.getKey());
			metadata.put(METADATA_GROUPS, user.getValue());
			metadata.put(METADATA_COMMAND, Command.USER.name());

			consumer.accept(metadata);

		});

		LOGGER.info("Finished Confluence User/group Sync");

	}

	private List<String> getUsers(RemotePersonServiceImpl remotePersonServiceImpl, Group group, Long batchSize) {

		int size = 0;
		PageRequest pageRequest = new SimplePageRequest(size, batchSize.intValue());

		PageResponse<Person> personResponse = ((RemotePersonFinderImpl) remotePersonServiceImpl.find().withMembershipOf(group)).fetchMany(pageRequest).claim();
		List<String> users = new ArrayList<>();

		while (!personResponse.getResults().isEmpty()) {

			LOGGER.info("Group {}, found batch user size {}, current size {}", group.getName(), personResponse.size(), users.size());

			users.addAll(personResponse.getResults().stream().map(u -> u.getOptionalUsername().getOrNull()).filter(Objects::nonNull).collect(Collectors.toList()));

			size = size + batchSize.intValue();
			pageRequest = new SimplePageRequest(size, batchSize.intValue());
			personResponse = ((RemotePersonFinderImpl) remotePersonServiceImpl.find().withMembershipOf(group)).fetchMany(pageRequest).claim();

		}

		LOGGER.info("Group {}, found total users {}", group.getName(), users.size());

		return users;

	}

}

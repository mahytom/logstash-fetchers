package eu.wajja.input.fetcher.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.confluence.api.model.Expansion;
import com.atlassian.confluence.api.model.content.Content;
import com.atlassian.confluence.api.model.content.ContentBody;
import com.atlassian.confluence.api.model.content.ContentRepresentation;
import com.atlassian.confluence.api.model.content.ContentStatus;
import com.atlassian.confluence.api.model.content.ContentType;
import com.atlassian.confluence.api.model.content.Space;
import com.atlassian.confluence.api.model.link.Link;
import com.atlassian.confluence.api.model.link.LinkType;
import com.atlassian.confluence.api.model.pagination.PageRequest;
import com.atlassian.confluence.api.model.pagination.PageResponse;
import com.atlassian.confluence.api.model.pagination.SimplePageRequest;
import com.atlassian.confluence.api.model.people.Person;
import com.atlassian.confluence.api.model.people.Subject;
import com.atlassian.confluence.api.model.people.SubjectType;
import com.atlassian.confluence.api.model.permissions.ContentRestriction;
import com.atlassian.confluence.api.model.permissions.ContentRestrictionsPageResponse;
import com.atlassian.confluence.api.model.permissions.OperationKey;
import com.atlassian.confluence.rest.client.RemoteAttachmentServiceImpl;
import com.atlassian.confluence.rest.client.RemoteContentRestrictionServiceImpl;
import com.atlassian.confluence.rest.client.RemoteContentServiceImpl;
import com.atlassian.confluence.rest.client.RemoteSpaceService.RemoteSpaceFinder;
import com.atlassian.confluence.rest.client.RemoteSpaceServiceImpl;
import com.atlassian.confluence.rest.client.authentication.AuthenticatedWebResourceProvider;
import com.atlassian.confluence.rpc.soap.beans.RemoteContentPermission;
import com.atlassian.confluence.rpc.soap.beans.RemoteSpacePermissionSet;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import eu.wajja.input.fetcher.enums.Command;
import eu.wajja.input.fetcher.model.Permissions;
import eu.wajja.input.fetcher.soap.confluence.ConfluenceSoapService;
import eu.wajja.input.fetcher.utils.UrlHelper;

public class ConfluenceDataFetcher implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfluenceDataFetcher.class);

    private static final String PREFIX_PAGE = "/pages/viewpage.action?pageId=";
    private static final String PREFIX_SPACE = "/display/";
    private static final String PREFIX_ATTACHMENT = "/download/attachments/";

    private static final String METADATA_TITLE = "title";
    private static final String METADATA_PAGE_NAME = "pageName";
    private static final String METADATA_FILE_NAME = "fileName";
    private static final String METADATA_REFERENCE = "reference";
    private static final String METADATA_CONTENT = "content";
    private static final String METADATA_URL = "url";
    private static final String METADATA_STATUS = "status";
    private static final String METADATA_COMMAND = "command";
    private static final String METADATA_CONTENT_TYPE = "contentType";

    private static final String METADATA_FETCHED_DATE = "fetchedDate";
    private static final String METADATA_MODIFIED_DATE = "modifiedDate";
    private static final String METADATA_MODIFIED_USER = "modifiedBy";

    private static final String METADATA_ACL_USERS = "aclUsers";
    private static final String METADATA_ACL_GROUPS = "aclGroups";

    private static final String METADATA_SPACE_ID = "spaceId";
    private static final String METADATA_SPACE_NAME = "spaceName";
    private static final String METADATA_SPACE_URL = "spaceUrl";

    private static final String METADATA_PARENT_ID = "parentId";
    private static final String METADATA_PARENT_NAME = "parentName";
    private static final String METADATA_PARENT_URL = "parentUrl";

    private static final String LOGGER_REFERENCE = "reference";
    private static final String LOGGER_STATUS = "status";
    private static final String LOGGER_URL = "url";
    private static final String LOGGER_ACTION = "action";
    private static final String LOGGER_SPACE = "space";
    private static final String LOGGER_INCLUDE_DATA = "include_data";
    private static final String LOGGER_EXCLUDE_DATA = "exclude_data";

    private static final String FETCHED_DATA_FOLDER = "/fetched-data/";

    private ObjectMapper objectMapper = new ObjectMapper();
    private UrlHelper urlHelper = new UrlHelper();

    private Expansion expansionDescendants = new Expansion("descendants");
    private Expansion expansionChildren = new Expansion("children.links");
    private Expansion expansionBody = new Expansion("body.storage");
    private Expansion expansionMetadata = new Expansion("metadata");
    private Expansion expansionVersion = new Expansion("version");
    private Expansion expansionContainer = new Expansion("container");
    private Expansion expansionAnscestors = new Expansion("ancestors");
    private Expansion expansionPermissions = new Expansion("permissions");

    private RemoteSpaceServiceImpl remoteSpaceServiceImpl;
    private RemoteContentServiceImpl remoteContentServiceImpl;
    private RemoteContentRestrictionServiceImpl remoteContentRestrictionServiceImpl;
    private RemoteAttachmentServiceImpl remoteAttachmentServiceImpl;

    private List<String> dataAttachmentsInclude;
    private List<String> dataAttachmentsExclude;
    private Long dataAttachmentsMaxSize;
    private List<String> dataPageExclude;
    private Long batchSize;
    private String dataFolder;
    private ThreadPoolExecutor executorService;
    private Long threads;
    private Long sleep;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        LOGGER.info("Starting confluence data fetcher");

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        Consumer<Map<String, Object>> consumer = (Consumer<Map<String, Object>>) dataMap.get("consumer");

        String username = dataMap.getString("username");
        String password = dataMap.getString("password");
        String url = dataMap.getString(METADATA_URL);

        this.remoteSpaceServiceImpl = (RemoteSpaceServiceImpl) dataMap.get("remoteSpaceServiceImpl");
        this.remoteContentServiceImpl = (RemoteContentServiceImpl) dataMap.get("remoteContentServiceImpl");
        this.remoteContentRestrictionServiceImpl = (RemoteContentRestrictionServiceImpl) dataMap.get("remoteContentRestrictionServiceImpl");
        this.remoteAttachmentServiceImpl = (RemoteAttachmentServiceImpl) dataMap.get("remoteAttachmentServiceImpl");

        this.dataAttachmentsInclude = (List<String>) dataMap.getOrDefault("dataAttachmentsInclude", new ArrayList<>());
        this.dataAttachmentsExclude = (List<String>) dataMap.getOrDefault("dataAttachmentsExclude", new ArrayList<>());
        this.dataAttachmentsMaxSize = (Long) dataMap.get("dataAttachmentsMaxSize");
        this.dataPageExclude = (List<String>) dataMap.getOrDefault("dataPageExclude", new ArrayList<>());
        this.batchSize = (Long) dataMap.getOrDefault("batchSize", 10l);
        this.dataFolder = dataMap.getString("dataFolder");
        this.sleep = (Long) dataMap.get("sleep");

        this.threads = (Long) dataMap.get("dataSyncThreadSize");
        this.executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads.intValue());

        /** Get spaces and start reading from them **/

        List<String> dataSpaceExclude = (List<String>) dataMap.getOrDefault("dataSpaceExclude", new ArrayList<>());
        List<String> spacesToCrawl = (List<String>) dataMap.getOrDefault("sites", new ArrayList<>());
        List<Space> spaces = getSpaces(spacesToCrawl);

        spaces.stream().forEach(space -> {

            try {

                LOGGER.info("Searching Space Permissions For Space {}", space);

                List<String> spacePermissions = new ArrayList<>();

                if (dataMap.containsKey("soapService") && dataMap.containsKey("soapToken")) {

                    LOGGER.info("Searching SOAP Space Permissions For Space {}", space);
                    
                    ConfluenceSoapService soapService = (ConfluenceSoapService) dataMap.get("soapService");
                    String soapToken = dataMap.getString("soapToken");

                    RemoteSpacePermissionSet remoteSpacePermissionSet = soapService.getSpacePermissionSet(soapToken, space.getKey(), "VIEWSPACE");
                    RemoteContentPermission[] remoteContentPermissions = remoteSpacePermissionSet.getSpacePermissions();

                    for (RemoteContentPermission remoteContentPermission : remoteContentPermissions) {
                        if (remoteContentPermission.getGroupName() != null) {
                            spacePermissions.add(remoteContentPermission.getGroupName());
                        }
                    }
                }

                if (dataSpaceExclude != null && !dataSpaceExclude.isEmpty() && dataSpaceExclude.stream().anyMatch(x -> space.getName().toLowerCase().matches(x.toLowerCase()))) {
                    LOGGER.info("Excluding space {}", space.getName());
                    return;
                } else {
                    LOGGER.info("Include space {}", space.getName());
                }

                LOGGER.info("Finished Space Permissions For Space {}", space);
                
                /**
                 * Each space has an index, so we can keep track of previously
                 * ingested files
                 **/

                String id = Base64.getEncoder().encodeToString(space.getKey().getBytes());
                Map<String, String> historyAddMap = new HashMap<>();
                Map<String, String> historyDeleteMap = new HashMap<>();

                if (dataFolder != null) {

                    historyAddMap = getHistoryFile(id, Command.ADD);
                    historyDeleteMap = getHistoryFile(id, Command.DELETE);
                }

                /** Data Extraction **/
                extractSpaceContent(consumer, username, password, url, space, ContentStatus.CURRENT, Command.ADD, historyAddMap, spacePermissions);
                extractSpaceContent(consumer, username, password, url, space, ContentStatus.TRASHED, Command.DELETE, historyDeleteMap, spacePermissions);

                /** Save the new history **/

                if (dataFolder != null) {

                    saveHistoryFile(id, historyAddMap, Command.ADD);
                    saveHistoryFile(id, historyDeleteMap, Command.DELETE);
                }

            } catch (Exception e) {
                LOGGER.error("Failed to get space permisisons", e);
            }

        });

        LOGGER.info("Finished Confluence Data Sync");

    }

    private void saveHistoryFile(String id, Map<String, String> historyMap, Command command) {

        Path pathFile = Paths.get(new StringBuilder(dataFolder).append(FETCHED_DATA_FOLDER).append(id).append("_").append(command.name()).append(".json").toString());

        try {
            String json = objectMapper.writeValueAsString(historyMap);
            Files.write(pathFile, json.getBytes());
        } catch (IOException e) {
            LOGGER.warn("Failed to write tmp file to disk {}", pathFile, e);
        }

    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getHistoryFile(String id, Command command) {

        Path pathFolder = Paths.get(new StringBuilder(dataFolder).append(FETCHED_DATA_FOLDER).toString());

        if (!pathFolder.toFile().exists()) {

            try {
                Files.createDirectory(pathFolder);
            } catch (IOException e) {
                LOGGER.info("Failed to create data folder : {}", pathFolder, e);
            }
        }

        Path pathFile = Paths.get(new StringBuilder(dataFolder).append(FETCHED_DATA_FOLDER).append(id).append("_").append(command.name()).append(".json").toString());

        if (pathFile.toFile().exists()) {

            LOGGER.info("Reading file {}", pathFile);

            try {
                return objectMapper.readValue(pathFile.toFile(), Map.class);
            } catch (IOException e) {
                LOGGER.info("Failed to read history file : {}", pathFile, e);
            }
        } else {
            LOGGER.info("Could not read file {}", pathFile);
        }

        return new HashMap<>();
    }

    private void extractSpaceContent(Consumer<Map<String, Object>> consumer, String username, String password, String url, Space space, ContentStatus contentStatus, Command command, Map<String, String> historyMap, List<String> spacePermissions) {

        int size = 0;
        ContentType contentType = ContentType.PAGE;
        PageRequest pageRequest = new SimplePageRequest(size, batchSize.intValue());

        try {

            PageResponse<Content> pageResponse = remoteContentServiceImpl
                    .find(expansionBody, expansionMetadata, expansionVersion, expansionDescendants, expansionChildren, expansionDescendants, expansionContainer, expansionAnscestors)
                    .withStatus(contentStatus)
                    .withSpace(space)
                    .fetchMany(contentType, pageRequest)
                    .claim();

            while (!pageResponse.getResults().isEmpty()) {

                List<Content> pages = pageResponse.getResults();

                for (Content page : pages) {

                    executorService.execute(() -> {

                        /** Metadata **/

                        Long id = page.getId().asLong();
                        Long modifiedDate = page.getVersion().getWhen().getMillis();

                        Map<String, Object> metadata = new HashMap<>();

                        metadata.put(METADATA_TITLE, page.getTitle());
                        metadata.put(METADATA_PAGE_NAME, page.getTitle());
                        metadata.put(METADATA_MODIFIED_DATE, modifiedDate.toString());
                        metadata.put(METADATA_MODIFIED_USER, page.getVersion().getBy().getOptionalUsername().get());
                        metadata.put(METADATA_COMMAND, command.name());
                        metadata.put(METADATA_STATUS, page.getStatus().getValue());
                        metadata.put(METADATA_CONTENT_TYPE, "confluence/" + contentType.getValue());
                        metadata.put(METADATA_REFERENCE, id.toString());
                        metadata.put(METADATA_URL, new StringBuilder(url).append(PREFIX_PAGE).append(page.getId().asLong()).toString());
                        metadata.put(METADATA_FETCHED_DATE, new Date().getTime());

                        metadata.put(METADATA_SPACE_ID, space.getId());
                        metadata.put(METADATA_SPACE_NAME, space.getName());
                        metadata.put(METADATA_SPACE_URL, new StringBuilder(url).append(PREFIX_SPACE).append(space.getKey()).toString());

                        Permissions permissions = addAllRestrictions(page, command, spacePermissions);

                        metadata.put(METADATA_ACL_USERS, new ArrayList<>(permissions.getUsers()));
                        metadata.put(METADATA_ACL_GROUPS, new ArrayList<>(permissions.getGroups()));

                        List<Content> attachments = getAttachments(page);
                        attachments.stream().forEach(attach -> {

                            try {
                                sendAttachment(consumer, username, password, url, space, attach, page, permissions, command, historyMap);
                            } catch (Exception e1) {
                                LOGGER.error("Failed to prepare the download of attachment", e1);
                            }
                        });

                        /** Body **/

                        Map<ContentRepresentation, ContentBody> body = page.getBody();
                        StringBuilder stringBuilder = new StringBuilder();
                        body.entrySet().stream().forEach(m -> stringBuilder.append(m.getValue().getValue()));
                        metadata.put(METADATA_CONTENT, Base64.getEncoder().encodeToString(stringBuilder.toString().getBytes()));

                        Map<String, Object> loggerMap = new HashMap<>();

                        if (this.dataPageExclude == null || this.dataPageExclude.isEmpty() || this.dataPageExclude.stream().noneMatch(x -> metadata.get(METADATA_PAGE_NAME).toString().toLowerCase().matches(x))) {

                            if (historyMap.containsKey(id.toString()) && historyMap.get(id.toString()).equals(modifiedDate.toString())) {
                                loggerMap.put(LOGGER_ACTION, LOGGER_EXCLUDE_DATA);

                            } else {
                                consumer.accept(metadata);
                                historyMap.put(id.toString(), modifiedDate.toString());
                                loggerMap.put(LOGGER_ACTION, LOGGER_INCLUDE_DATA);
                            }

                        } else {
                            loggerMap.put(LOGGER_ACTION, LOGGER_EXCLUDE_DATA);
                        }

                        loggerMap.put(LOGGER_REFERENCE, metadata.get(METADATA_REFERENCE));
                        loggerMap.put(LOGGER_STATUS, page.getStatus().getValue());
                        loggerMap.put(LOGGER_URL, metadata.get(METADATA_URL));
                        loggerMap.put(LOGGER_SPACE, metadata.get(METADATA_SPACE_NAME));

                        if (LOGGER.isInfoEnabled()) {

                            try {
                                LOGGER.info(objectMapper.writeValueAsString(loggerMap));
                            } catch (Exception e) {
                                LOGGER.error("Failed to log entry", e);
                            }
                        }
                    });

                    while (executorService.getActiveCount() >= threads) {
                        Thread.sleep(500);
                    }

                }

                try {
                    Thread.sleep(this.sleep);
                } catch (InterruptedException e) {
                    LOGGER.info("Could not continue sleeping");
                }

                size = size + batchSize.intValue();
                pageRequest = new SimplePageRequest(size, batchSize.intValue());
                pageResponse = remoteContentServiceImpl
                        .find(expansionBody, expansionMetadata, expansionVersion, expansionDescendants, expansionChildren, expansionDescendants, expansionContainer, expansionAnscestors)
                        .withStatus(contentStatus)
                        .withSpace(space)
                        .fetchMany(contentType, pageRequest)
                        .claim();

            }

        } catch (Exception e) {
            LOGGER.info("Failed to get {}", contentType.getValue(), e);
        }
    }

    private void sendAttachment(Consumer<Map<String, Object>> consumer, String username, String password, String url, Space space, Content attach, Content parent, Permissions permissions, Command command, Map<String, String> historyMap) throws IOException {

        Long id = attach.getId().asLong();
        Long modifiedDate = attach.getVersion().getWhen().getMillis();

        Map<String, Object> metadata = new HashMap<>();

        metadata.put(METADATA_TITLE, attach.getTitle());
        metadata.put(METADATA_FILE_NAME, attach.getTitle());
        metadata.put(METADATA_MODIFIED_DATE, attach.getVersion().getWhen().getMillis());
        metadata.put(METADATA_MODIFIED_USER, attach.getVersion().getBy().getOptionalUsername().get());
        metadata.put(METADATA_COMMAND, command.name());
        metadata.put(METADATA_STATUS, attach.getStatus().getValue());
        metadata.put(METADATA_CONTENT_TYPE, "confluence/" + ContentType.ATTACHMENT);
        metadata.put(METADATA_REFERENCE, id.toString());
        metadata.put(METADATA_URL, new StringBuilder(url).append(PREFIX_ATTACHMENT).append(parent.getId().asLong()).append("/").append(attach.getTitle()).toString());
        metadata.put(METADATA_FETCHED_DATE, new Date().getTime());

        metadata.put(METADATA_SPACE_ID, space.getId());
        metadata.put(METADATA_SPACE_NAME, space.getName());
        metadata.put(METADATA_SPACE_URL, new StringBuilder(url).append(PREFIX_SPACE).append(space.getKey()).toString());

        metadata.put(METADATA_PARENT_ID, parent.getId().asLong());
        metadata.put(METADATA_PARENT_NAME, parent.getTitle());
        metadata.put(METADATA_PARENT_URL, new StringBuilder(url).append(PREFIX_PAGE).append(parent.getId().asLong()).toString());

        metadata.put(METADATA_ACL_USERS, new ArrayList<>(permissions.getUsers()));
        metadata.put(METADATA_ACL_GROUPS, new ArrayList<>(permissions.getGroups()));

        Map<LinkType, Link> links = attach.getLinks();
        Link link = links.entrySet().stream().filter(x -> x.getKey().equals(LinkType.DOWNLOAD)).map(Entry::getValue).findFirst().orElse(null);

        Map<String, Object> loggerMap = new HashMap<>();

        if (this.dataAttachmentsInclude == null || this.dataAttachmentsInclude.isEmpty() || this.dataAttachmentsInclude.stream().anyMatch(x -> metadata.get(METADATA_FILE_NAME).toString().toLowerCase().matches(x))) {

            if (this.dataAttachmentsExclude == null || this.dataAttachmentsExclude.isEmpty() || this.dataAttachmentsExclude.stream().noneMatch(x -> metadata.get(METADATA_FILE_NAME).toString().toLowerCase().matches(x))) {

                if (historyMap.containsKey(id.toString()) && historyMap.get(id.toString()).equals(modifiedDate.toString())) {
                    loggerMap.put(LOGGER_ACTION, LOGGER_EXCLUDE_DATA);
                } else {

                    if (link != null && command.equals(Command.ADD)) {

                        String downloadUrl = url + link.getPath();
                        URLConnection urlConnection = urlHelper.getUrl(username, password, downloadUrl);

                        try (InputStream content = urlConnection.getInputStream()) {

                            byte[] bytes = IOUtils.toByteArray(content);

                            if (dataAttachmentsMaxSize == null || dataAttachmentsMaxSize == 0l || bytes.length < dataAttachmentsMaxSize.intValue()) {
                                String encodedContent = Base64.getEncoder().encodeToString(bytes);
                                metadata.put(METADATA_CONTENT, encodedContent);
                            }

                        } catch (Exception e) {
                            LOGGER.error("Failed to download attachment content", e);
                        }
                    }

                    consumer.accept(metadata);
                    historyMap.put(id.toString(), modifiedDate.toString());
                    loggerMap.put(LOGGER_ACTION, LOGGER_INCLUDE_DATA);
                }

            } else {
                loggerMap.put(LOGGER_ACTION, LOGGER_EXCLUDE_DATA);
            }

        } else {
            loggerMap.put(LOGGER_ACTION, LOGGER_EXCLUDE_DATA);
        }

        loggerMap.put(LOGGER_REFERENCE, metadata.get(METADATA_REFERENCE));
        loggerMap.put(LOGGER_STATUS, attach.getStatus().getValue());
        loggerMap.put(LOGGER_URL, metadata.get(METADATA_URL));
        loggerMap.put(LOGGER_SPACE, metadata.get(METADATA_SPACE_NAME));

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(objectMapper.writeValueAsString(loggerMap));
        }
    }

    private List<Content> getAttachments(Content content) {

        int size = 0;
        PageRequest pageRequest = new SimplePageRequest(size, batchSize.intValue());

        List<Content> contents = new ArrayList<>();

        try {
            PageResponse<Content> attachments = remoteAttachmentServiceImpl
                    .find(expansionBody, expansionMetadata, expansionVersion, expansionDescendants, expansionChildren, expansionDescendants, expansionContainer)
                    .withContainerId(content.getId())
                    .fetchMany(pageRequest).claim();

            while (!attachments.getResults().isEmpty()) {

                contents.addAll(attachments.getResults().stream().collect(Collectors.toList()));

                try {
                    Thread.sleep(this.sleep);
                } catch (InterruptedException e) {
                    LOGGER.info("Could not continue sleeping");
                }

                size = size + batchSize.intValue();
                pageRequest = new SimplePageRequest(size, batchSize.intValue());
                attachments = remoteAttachmentServiceImpl
                        .find(expansionBody, expansionMetadata, expansionVersion, expansionDescendants, expansionChildren, expansionDescendants, expansionContainer)
                        .withContainerId(content.getId())
                        .fetchMany(pageRequest).claim();

            }

        } catch (Exception e) {
            LOGGER.error("Failed to retrive attachments from page {}", content.getId());
        }

        return contents;

    }

    private Permissions addAllRestrictions(Content content, Command command, List<String> spacePermissions) {

        Permissions permissions = new Permissions();

        try {

            if (!command.equals(Command.DELETE)) {

                getContentPermissions(content, permissions);
                content.getAncestors().stream().forEach(ancestor -> getContentPermissions(ancestor, permissions));

                if (permissions.getGroups().isEmpty() && permissions.getUsers().isEmpty()) {
                    permissions.getGroups().addAll(spacePermissions);
                }

            }

        } catch (Exception e) {
            LOGGER.error("Failed to read page permissions", e);
        }

        return permissions;
    }

    private void getContentPermissions(Content content, Permissions permissions) {

        PageRequest pageRequest = new SimplePageRequest(0, 10);
        ContentRestrictionsPageResponse contentRestrictionsPageResponse = remoteContentRestrictionServiceImpl.getRestrictions(content.getId(), pageRequest).claim();

        contentRestrictionsPageResponse.getResults().stream().filter(x -> x.getOperation().getOperationKey().equals(OperationKey.READ)).forEach(perm -> {

            permissions.getUsers().addAll(perm.getRestrictions().entrySet().stream().filter(r -> r.getKey().equals(SubjectType.USER))
                    .flatMap(r -> r.getValue().getResults().stream())
                    .map(s -> (Person) s)
                    .map(p -> p.getOptionalUsername().getOrNull())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));

            permissions.getGroups().addAll(perm.getRestrictions().entrySet().stream()
                    .filter(r -> r.getKey().equals(SubjectType.GROUP))
                    .flatMap(r -> r.getValue().getResults().stream())
                    .map(Subject::getDisplayName)
                    .collect(Collectors.toList()));

        });
    }

    private List<Space> getSpaces(List<String> sites) {

        LOGGER.info("Searching Confluence Spaces");

        int size = 0;
        PageRequest pageRequest = new SimplePageRequest(size, batchSize.intValue());

        RemoteSpaceFinder remoteSpaceFinder = remoteSpaceServiceImpl.find(expansionBody, expansionMetadata, expansionVersion, expansionDescendants, expansionChildren, expansionDescendants, expansionContainer, expansionPermissions);

        if (!sites.isEmpty()) {
            remoteSpaceFinder.withKeys(sites.toArray(new String[sites.size()]));
        }

        PageResponse<Space> pageResponse = remoteSpaceFinder.fetchMany(pageRequest).claim();

        List<Space> spaces = new ArrayList<>();

        while (!pageResponse.getResults().isEmpty()) {

            List<Space> spacesSearch = pageResponse.getResults().stream().collect(Collectors.toList());

            LOGGER.info("Searching Confluence Spaces {}", spacesSearch);
            spaces.addAll(spacesSearch);

            size = size + batchSize.intValue();
            pageRequest = new SimplePageRequest(size, batchSize.intValue());

            remoteSpaceFinder = remoteSpaceServiceImpl.find(expansionBody, expansionMetadata, expansionVersion, expansionDescendants, expansionChildren, expansionDescendants, expansionContainer, expansionPermissions);

            if (!sites.isEmpty()) {
                remoteSpaceFinder.withKeys(sites.toArray(new String[sites.size()]));
            }

            try {
                Thread.sleep(this.sleep);
            } catch (InterruptedException e) {
                LOGGER.info("Could not continue sleeping");
            }

            pageResponse = remoteSpaceFinder.fetchMany(pageRequest).claim();

        }

        LOGGER.info("Finished Searching Confluence Spaces");

        return spaces;

    }

}

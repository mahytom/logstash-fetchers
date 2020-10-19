package eu.wajja.input.fetcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclEntryType;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;

import org.apache.commons.io.IOUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.wajja.web.fetcher.controller.ProxyController;
import eu.wajja.web.fetcher.elasticsearch.ElasticSearchService;

@DisallowConcurrentExecution
public class FilesystemFetcherJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilesystemFetcherJob.class);

    private ThreadPoolExecutor executorService = null;
    private List<String> excludedUrls;

    private ProxyController proxyController;
    private ElasticSearchService elasticSearchService;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        Consumer<Map<String, Object>> consumer = (Consumer<Map<String, Object>>) dataMap.get(FilesystemFetcher.PROPERTY_CONSUMER);

        List<String> paths = (List<String>) dataMap.get(FilesystemFetcher.PROPERTY_PATHS);
        Long threads = dataMap.getLong(FilesystemFetcher.PROPERTY_THREADS);

        if (proxyController == null) {

            proxyController = new ProxyController(dataMap.getString(FilesystemFetcher.PROPERTY_PROXY_USER),
                    dataMap.getString(FilesystemFetcher.PROPERTY_PROXY_PASS),
                    dataMap.getString(FilesystemFetcher.PROPERTY_PROXY_HOST),
                    dataMap.getLong(FilesystemFetcher.PROPERTY_PROXY_PORT),
                    dataMap.getBoolean(FilesystemFetcher.PROPERTY_SSL_CHECK));
        }

        List<String> hostnames = (List<String>) dataMap.get(FilesystemFetcher.PROPERTY_ELASTIC_HOSTNAMES);

        Long proxyPort = proxyController.getProxyPort();
        String proxyUsername = proxyController.getProxyUser();
        String proxyPassword = proxyController.getProxyPass();
        String username = dataMap.getString(FilesystemFetcher.PROPERTY_ELASTIC_USERNAME);
        String password = dataMap.getString(FilesystemFetcher.PROPERTY_ELASTIC_PASSWORD);
        String proxyScheme = proxyController.getProxyHost();
        String proxyHostname = proxyController.getProxyHost();

        elasticSearchService = new ElasticSearchService(hostnames, username, password, proxyScheme, proxyHostname, proxyPort, proxyUsername, proxyPassword);

        executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads.intValue());

        paths.stream().forEach(path -> {

            Path dataPath = Paths.get(path);
            String index = Base64.getEncoder().encodeToString(dataPath.toString().getBytes()).toLowerCase();
            elasticSearchService.checkIndex(index);

            File file = dataPath.toFile();
            Arrays.asList(file.listFiles()).stream().forEach(f -> parseFile(index, f, consumer));

        });

        while (executorService.getActiveCount() > 0) {

            LOGGER.info("Thread count is : {}", executorService.getActiveCount());

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {

                LOGGER.error("Failed to sleep ??", e);
                Thread.currentThread().interrupt();

            }
        }

        LOGGER.info("Finished Filesystem job");

    }

    private void parseFile(String index, File file, Consumer<Map<String, Object>> consumer) {

        if (file.isDirectory()) {

            Arrays.asList(file.listFiles()).stream().forEach(f -> executorService.submit(() -> parseFile(index, f, consumer)));

        } else {

            if (excludedUrls == null || excludedUrls.stream().noneMatch(ex -> file.getName().endsWith(ex))) {

                String path = file.getAbsolutePath();
                Date modifiedDate = new Date(file.lastModified());

                if (!elasticSearchService.documentExists(index, path, modifiedDate)) {

                    LOGGER.info("Sending : {}", path);

                    Map<String, Object> metadata = new HashMap<>();

                    try (FileInputStream fileInputStream = new FileInputStream(file)) {

                        List<String> aclUsers = new ArrayList<>();
                        List<String> aclNoUsers = new ArrayList<>();
                        List<String> aclGroups = new ArrayList<>();
                        List<String> aclNoGroups = new ArrayList<>();

                        getPermissions(path, aclUsers, aclNoUsers, aclGroups, aclNoGroups);

                        byte[] bytes = IOUtils.toByteArray(fileInputStream);

                        metadata.put("reference", Base64.getEncoder().encodeToString(path.getBytes()));
                        metadata.put("url", path);
                        metadata.put("epochSecond", file.lastModified());
                        metadata.put("path", path);
                        metadata.put("content", bytes);
                        metadata.put("aclUsers", aclUsers);
                        metadata.put("aclNoUsers", aclNoUsers);
                        metadata.put("aclGroups", aclGroups);
                        metadata.put("aclNoGroups", aclNoGroups);

                        consumer.accept(metadata);

                        elasticSearchService.addDocument(index, path, modifiedDate);

                    } catch (Exception e1) {
                        LOGGER.error("Failed to read file", e1);
                    }

                }

            }

        }

    }

    private void getPermissions(String path, List<String> aclUsers, List<String> aclNoUsers, List<String> aclGroups, List<String> aclNoGroups) throws IOException {

        AclFileAttributeView aclFileAttributeView = Files.getFileAttributeView(Paths.get(path), AclFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
        List<AclEntry> permissions = aclFileAttributeView.getAcl();

        permissions.forEach(u -> {

            UserPrincipal userPrincipal = u.principal();
            String clazz = userPrincipal.getClass().getName();
            String principalName = userPrincipal.getName();
            String name = principalName.substring(principalName.indexOf("\\") + 1);

            if (clazz.contains("$Group")) {

                if (u.type().equals(AclEntryType.ALLOW)) {
                    aclGroups.add(name);

                } else if (u.type().equals(AclEntryType.DENY)) {
                    aclNoGroups.add(name);

                }

            } else {

                if (u.type().equals(AclEntryType.ALLOW)) {
                    aclUsers.add(name);

                } else if (u.type().equals(AclEntryType.DENY)) {
                    aclNoUsers.add(name);

                }

            }

        });
    }

}

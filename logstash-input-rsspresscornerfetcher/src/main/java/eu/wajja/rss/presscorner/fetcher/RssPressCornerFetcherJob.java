package eu.wajja.rss.presscorner.fetcher;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import eu.wajja.rss.presscorner.fetcher.controller.URLController;
import eu.wajja.rss.presscorner.fetcher.elasticsearch.ElasticSearchService;
import eu.wajja.rss.presscorner.fetcher.enums.Command;
import eu.wajja.rss.presscorner.fetcher.enums.Status;
import eu.wajja.rss.presscorner.fetcher.enums.SubStatus;
import eu.wajja.rss.presscorner.fetcher.model.Result;
import eu.wajja.rss.presscorner.fetcher.services.constants.MetadataConstant;

@DisallowConcurrentExecution
public class RssPressCornerFetcherJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(RssPressCornerFetcherJob.class);
    private DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

    private String jobId;
    private List<String> excludedDataRegex;
    private List<String> excludedLinkRegex;
    private String rootUrl;

    private ElasticSearchService elasticSearchService;
    private URLController urlController;
    private ThreadPoolExecutor threadPoolExecutors = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

    @SuppressWarnings("unchecked")
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        Consumer<Map<String, Object>> consumer = (Consumer<Map<String, Object>>) dataMap.get(RssPressCornerFetcher.PROPERTY_CONSUMER);
        List<String> initialUrls = (List<String>) dataMap.get(RssPressCornerFetcher.PROPERTY_URLS);
        List<String> chromeThreads = (List<String>) dataMap.get(RssPressCornerFetcher.PROPERTY_CHROME_DRIVERS);

        jobId = UUID.randomUUID().toString();

        this.excludedDataRegex = (List<String>) dataMap.get(RssPressCornerFetcher.PROPERTY_EXCLUDE_DATA);
        this.excludedLinkRegex = (List<String>) dataMap.get(RssPressCornerFetcher.PROPERTY_EXCLUDE_LINK);

        String waitForCssSelector = dataMap.getString(RssPressCornerFetcher.PROPERTY_WAIT_FOR_CSS_SELECTOR);
        Long maxWaitForCssSelector = dataMap.getLong(RssPressCornerFetcher.PROPERTY_MAX_WAIT_FOR_CSS_SELECTOR);
        String dataFolderLocation = dataMap.getString(RssPressCornerFetcher.PROPERTY_DATA_FOLDER);

        List<String> hostnames = (List<String>) dataMap.get(RssPressCornerFetcher.PROPERTY_ELASTIC_HOSTNAMES);

        String username = dataMap.getString(RssPressCornerFetcher.PROPERTY_ELASTIC_USERNAME);
        String password = dataMap.getString(RssPressCornerFetcher.PROPERTY_ELASTIC_PASSWORD);

        elasticSearchService = new ElasticSearchService(hostnames, username, password);

        if (urlController == null) {

            urlController = new URLController(
                    elasticSearchService,
                    dataMap.getLong(RssPressCornerFetcher.PROPERTY_TIMEOUT),
                    dataMap.getString(RssPressCornerFetcher.PROPERTY_CRAWLER_USER_AGENT),
                    dataMap.getString(RssPressCornerFetcher.PROPERTY_CRAWLER_REFERER),
                    waitForCssSelector,
                    maxWaitForCssSelector.intValue());
        }

        int threadCount = 0;

        for (String initialUrl : initialUrls) {

            String id = Base64.getEncoder().encodeToString(initialUrl.getBytes()).replace("/", "_");
            String index = "logstash_rss_presscorner_fetcher_" + id.toLowerCase();
            elasticSearchService.checkIndex(index);

            File dataFolder = new File(dataFolderLocation);
            dataFolder.mkdirs();

            LocalDate localDateFrom = LocalDate.now().minus(1, ChronoUnit.DAYS);
            LocalDate localDateTo = LocalDate.now();

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");

            while (localDateFrom.toEpochDay() > 0l) {

                String urlString = new StringBuilder().append(initialUrl)
                        .append("&datefrom=").append(localDateFrom.format(dateTimeFormatter))
                        .append("&dateto=").append(localDateTo.format(dateTimeFormatter))
                        .append("&pagesize=100").toString();

                String xmlPath = new StringBuilder(dataFolder.getAbsolutePath())
                        .append("/").append(localDateFrom.getYear())
                        .append("/").append(localDateFrom.getMonthValue())
                        .append("/").append(localDateFrom.format(dateTimeFormatter) + "-" + localDateTo.format(dateTimeFormatter) + ".xml").toString();

                File file = new File(xmlPath);
                file.getParentFile().mkdirs();

                if (!file.exists()) {

                    try {

                        URL url = new URL(urlString);
                        URLConnection connection = url.openConnection();

                        try (InputStream inputStream = connection.getInputStream()) {

                            byte[] bytes = IOUtils.toByteArray(inputStream);
                            Files.write(Paths.get(file.getAbsolutePath()), bytes, StandardOpenOption.CREATE_NEW);

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                try {

                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(file);
                    NodeList nodeList = doc.getElementsByTagName("guid");

                    for (int x = 0; nodeList.getLength() > x; x++) {

                        String content = nodeList.item(x).getTextContent().toLowerCase();
                        String chromeThread = chromeThreads.get(threadCount);

                        boolean exists = elasticSearchService.existsInIndex(content, index);

                        if (!exists) {

                            LOGGER.info("URL to download {}", content);

                            threadPoolExecutors.execute(() -> {

                                try {

                                    Result result = urlController.getURL(index, content, initialUrl, chromeThread);

                                    LOGGER.info("Sending url {}", result.getUrl());

                                    Map<String, Object> metadata = new HashMap<>();
                                    metadata.put(MetadataConstant.METADATA_URL, result.getUrl());
                                    metadata.put(MetadataConstant.METADATA_INDEX, index);
                                    metadata.put(MetadataConstant.METADATA_CONTENT_TYPE, result.getContentType());
                                    metadata.put(MetadataConstant.METADATA_REFERENCE, Base64.getEncoder().encodeToString(result.getUrl().getBytes()));
                                    metadata.put(MetadataConstant.METADATA_EPOCH, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
                                    metadata.put(MetadataConstant.METADATA_UUID, UUID.randomUUID().toString());
                                    metadata.put(MetadataConstant.METADATA_STATUS, 200);
                                    metadata.put(MetadataConstant.METADATA_CONTEXT, initialUrl);
                                    metadata.put(MetadataConstant.METADATA_COMMAND, Command.ADD.toString());
                                    metadata.put(MetadataConstant.METADATA_CONTENT, Base64.getEncoder().encodeToString(result.getContent()));

                                    consumer.accept(metadata);

                                    elasticSearchService.addNewUrl(result, jobId, index, Status.processed, SubStatus.included, "Document sent to filter");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });

                            threadCount++;

                            if (threadCount >= chromeThreads.size()) {
                                threadCount = 0;
                            }

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                LOGGER.info("Rss URL {}", urlString);

                localDateFrom = localDateFrom.minus(1, ChronoUnit.DAYS);
                localDateTo = localDateTo.minus(1, ChronoUnit.DAYS);

                while (threadPoolExecutors.getActiveCount() > 1) {

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        LOGGER.info("Finished Thread {}", jobId);

    }

}

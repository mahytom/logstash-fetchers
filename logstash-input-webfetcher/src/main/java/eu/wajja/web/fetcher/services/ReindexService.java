package eu.wajja.web.fetcher.services;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.wajja.web.fetcher.elasticsearch.ElasticSearchService;
import eu.wajja.web.fetcher.enums.Command;
import eu.wajja.web.fetcher.enums.Status;
import eu.wajja.web.fetcher.enums.SubStatus;
import eu.wajja.web.fetcher.model.Result;
import eu.wajja.web.fetcher.services.constants.MetadataConstant;

public class ReindexService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReindexService.class);

    private ElasticSearchService elasticSearchService;
    private List<String> excludedDataRegex;
    private List<String> excludedLinkRegex;

    public ReindexService(ElasticSearchService elasticSearchService, List<String> excludedDataRegex, List<String> excludedLinkRegex) {

        this.elasticSearchService = elasticSearchService;
        this.excludedDataRegex = excludedDataRegex;
        this.excludedLinkRegex = excludedLinkRegex;
    }

    public void reIndex(Consumer<Map<String, Object>> consumer, String jobId, String initialUrl, String index) {

        LOGGER.info("Starting full reindex for thread : {}, url : {}", jobId, initialUrl);

        LinkedList<Result> results = new LinkedList<>();
        Future<Boolean> future = elasticSearchService.getAsyncUrls(index, results, Status.processed, SubStatus.included);
        List<Result> queueItems = new ArrayList<>();

        while (!future.isDone()) {

            while (!results.isEmpty()) {
                queueItems.add(results.pop());
            }

            queueItems.parallelStream().forEach(result -> sendResultToFilter(consumer, index, result));
            queueItems.clear();
        }

        results.parallelStream().forEach(result -> sendResultToFilter(consumer, index, result));

        LOGGER.info("Finished full reindex for thread : {}, url : {}", jobId, initialUrl);
    }

    private void sendResultToFilter(Consumer<Map<String, Object>> consumer, String index, Result result) {

        byte[] bytes = result.getContent();

        if (bytes == null || bytes.length == 0) {

            LOGGER.warn("Cannot Reindex, content is empty, url {}", result.getUrl());
            elasticSearchService.updateStatus(result.getUrl(), index, Status.failed, SubStatus.excluded, "content is empty");

        } else if (excludedLinkRegex.stream().anyMatch(ex -> result.getUrl().matches(ex))) {

            LOGGER.info("Url matched excludedLinkRegex, ignoring {}", result.getUrl());
            elasticSearchService.updateStatus(result.getUrl(), index, Status.processed, SubStatus.excluded, "regex excludedLinkRegex");

        } else if (excludedDataRegex.stream().anyMatch(ex -> result.getUrl().matches(ex))) {

            LOGGER.info("Url matched excludedDataRegex, ignoring {}", result.getUrl());
            elasticSearchService.updateStatus(result.getUrl(), index, Status.processed, SubStatus.excluded, "regex excludedDataRegex");

        } else {

            LOGGER.info("Reindexing url {}", result.getUrl());

            Map<String, Object> metadata = new HashMap<>();
            metadata.put(MetadataConstant.METADATA_URL, result.getUrl());
            metadata.put(MetadataConstant.METADATA_INDEX, index);
            metadata.put(MetadataConstant.METADATA_CONTENT_TYPE, result.getContentType());
            metadata.put(MetadataConstant.METADATA_REFERENCE, Base64.getEncoder().encodeToString(result.getUrl().getBytes()));
            metadata.put(MetadataConstant.METADATA_EPOCH, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
            metadata.put(MetadataConstant.METADATA_UUID, UUID.randomUUID().toString());
            metadata.put(MetadataConstant.METADATA_STATUS, 200);
            metadata.put(MetadataConstant.METADATA_CONTEXT, result.getRootUrl());
            metadata.put(MetadataConstant.METADATA_COMMAND, Command.ADD.toString());
            metadata.put(MetadataConstant.METADATA_CONTENT, Base64.getEncoder().encodeToString(bytes));

            consumer.accept(metadata);
            elasticSearchService.updateStatus(result.getUrl(), index, Status.processed, SubStatus.included, "Document sent to filter");

        }
    }
}

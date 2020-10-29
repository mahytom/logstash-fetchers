package eu.wajja.dpo.fetcher.elasticsearch;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.elasticsearch.action.admin.indices.flush.FlushRequest;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.wajja.dpo.fetcher.enums.Status;
import eu.wajja.dpo.fetcher.enums.SubStatus;
import eu.wajja.dpo.fetcher.model.Result;

public class ElasticSearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchService.class);
    private static final Scroll scroll = new Scroll(TimeValue.timeValueHours(1l));;

    private static final String DOCUMENT_TYPE = "_doc";
    private static final String INDEX_SHARDS = "index.number_of_shards";
    private static final String INDEX_NUMBER_OF_REPLICAS = "index.number_of_replicas";
    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String CODE = "code";
    private static final String CONTENT_TYPE = "contentType";
    private static final String CONTENT = "content";
    private static final String CONTENT_SIZE = "contentSize";
    private static final String MESSAGE = "message";
    private static final String REASON = "reason";
    private static final String ETAG = "etag";
    private static final String ROOT_URL = "rootUrl";
    private static final String URL = "url";
    private static final String HEADERS = "headers";
    private static final String MODIFIED_DATE = "modifiedDate";
    private static final String STATUS = "status";
    private static final String SUB_STATUS = "subStatus";
    private static final String JOB_ID = "jobId";

    private static final String MAPPINGS = "mappings";
    private static final String TYPE = "type";
    private static final String KEYWORD = "keyword";
    private static final String NUMERIC = "long";
    private static final String PROPERTIES = "properties";

    private RestHighLevelClient restHighLevelClient;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private BulkProcessor bulkProcessor;

    public ElasticSearchService(List<String> hostnames, String username, String password, String proxyScheme, String proxyHostname, Long proxyPort, String proxyUsername, String proxyPassword) {

        restHighLevelClient = new ElasticRestClient(hostnames, username, password, proxyScheme, proxyHostname, proxyPort, proxyUsername, proxyPassword).restHighLevelClient();

        BulkProcessor.Listener listener = new BulkProcessor.Listener() {

            @Override
            public void beforeBulk(long executionId, BulkRequest request) {

                LOGGER.info("Sending Queue Bulk Ingestion Request : {}, with documents : {}", executionId, request.numberOfActions());

                request.requests().stream().forEach(r -> {

                    String id = new String(Base64.getDecoder().decode(r.id()));
                    LOGGER.info("Sending execution {}, reference {}", executionId, id);
                });
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {

                LOGGER.error("Failed Queue Bulk Ingestion Request : {}, Error : {}", executionId, failure.getLocalizedMessage());
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {

                LOGGER.info("Finished Queue Bulk Ingestion Request : {}", executionId);

            }
        };

        BulkProcessor.Builder builder = BulkProcessor.builder(restHighLevelClient::bulkAsync, listener);

        builder.setBulkActions(100);
        builder.setConcurrentRequests(3);
        builder.setBulkSize(new ByteSizeValue(30, ByteSizeUnit.MB));
        builder.setFlushInterval(TimeValue.timeValueSeconds(30));
        builder.setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3));

        bulkProcessor = builder.build();

    }

    public void checkIndex(String index) {

        try {
            GetIndexRequest request = new GetIndexRequest(index);
            boolean indexExists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);

            if (!indexExists) {

                try (XContentBuilder xBuilder = XContentFactory.jsonBuilder()) {

                    xBuilder.startObject();

                    xBuilder.startObject("settings");
                    xBuilder.field(INDEX_SHARDS, 1);
                    xBuilder.field(INDEX_NUMBER_OF_REPLICAS, 1);
                    xBuilder.endObject();

                    xBuilder.startObject(MAPPINGS);
                    xBuilder.startObject(PROPERTIES);

                    xBuilder.startObject(CODE).field(TYPE, KEYWORD).endObject();
                    xBuilder.startObject(CONTENT_TYPE).field(TYPE, KEYWORD).endObject();
                    xBuilder.startObject(CONTENT_SIZE).field(TYPE, NUMERIC).endObject();
                    xBuilder.startObject(MESSAGE).field(TYPE, KEYWORD).endObject();
                    xBuilder.startObject(ROOT_URL).field(TYPE, KEYWORD).endObject();
                    xBuilder.startObject(URL).field(TYPE, KEYWORD).endObject();
                    xBuilder.startObject(HEADERS).field(TYPE, KEYWORD).endObject();
                    xBuilder.startObject(MODIFIED_DATE).field(TYPE, NUMERIC).endObject();
                    xBuilder.startObject(STATUS).field(TYPE, KEYWORD).endObject();
                    xBuilder.startObject(JOB_ID).field(TYPE, KEYWORD).endObject();

                    xBuilder.endObject();
                    xBuilder.endObject();

                    xBuilder.endObject();

                    // Create
                    CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
                    createIndexRequest.source(xBuilder);
                    restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);

                    flushIndex(index);

                }
            }

        } catch (IOException e1) {
            LOGGER.error("Failed to check if index exists", e1);
        }
    }

    public void flushIndex(String index) {

        try {

            bulkProcessor.flush();

            FlushRequest flushRequest = new FlushRequest(index);
            flushRequest.force(true);

            restHighLevelClient.indices().flush(flushRequest, RequestOptions.DEFAULT);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void updateStatus(String url, String index, Status status, SubStatus subStatus, String message) {

        String id = Base64.getEncoder().encodeToString(url.replace("https://", "").replace("http://", "").getBytes());

        UpdateRequest updateRequest = new UpdateRequest(index, DOCUMENT_TYPE, id);

        try (XContentBuilder contentBuilder = XContentFactory.jsonBuilder()) {

            contentBuilder.startObject();

            contentBuilder.field(STATUS, status.name());
            contentBuilder.field(SUB_STATUS, subStatus.name());
            contentBuilder.field(REASON, message);
            contentBuilder.endObject();

            updateRequest.doc(contentBuilder);

            bulkProcessor.add(updateRequest);

        } catch (IOException e) {
            LOGGER.error("Failed to update url to index", e);
        }

    }

    public void addNewUrl(Result result, String jobId, String index, Status status, SubStatus subStatus, String message) {

        String id = Base64.getEncoder().encodeToString(result.getUrl().replace("https://", "").replace("http://", "").getBytes());

        IndexRequest indexRequest = new IndexRequest(index);
        indexRequest.id(id);
        indexRequest.type(DOCUMENT_TYPE);

        try (XContentBuilder contentBuilder = XContentFactory.jsonBuilder()) {

            contentBuilder.startObject();

            if (result.getContent() != null) {

                if (result.getContentType().contains("html")) {
                    contentBuilder.field(CONTENT, new String(result.getContent()));
                } else {
                    String content = Base64.getEncoder().encodeToString(result.getContent());
                    contentBuilder.field(CONTENT, content);
                }
            }

            Map<String, List<String>> map = new HashMap<>();
            result.getHeaders().entrySet().stream().filter(e -> e.getKey() != null && e.getValue() != null).forEach(e -> map.put(e.getKey(), e.getValue()));

            contentBuilder.field(CODE, result.getCode());
            contentBuilder.field(CONTENT_TYPE, result.getContentType());
            contentBuilder.field(CONTENT_SIZE, result.getLength());
            contentBuilder.field(MESSAGE, result.getMessage());
            contentBuilder.field(ROOT_URL, result.getRootUrl());
            contentBuilder.field(URL, result.getUrl());
            contentBuilder.field(HEADERS, objectMapper.writeValueAsString(map));
            contentBuilder.field(MODIFIED_DATE, new Date().getTime());
            contentBuilder.field(STATUS, status.name());
            contentBuilder.field(SUB_STATUS, subStatus.name());
            contentBuilder.field(JOB_ID, jobId);
            contentBuilder.field(REASON, message);
            contentBuilder.field(ETAG, result.geteTag());
            contentBuilder.endObject();

            indexRequest.source(contentBuilder);
            bulkProcessor.add(indexRequest);

        } catch (IOException e) {
            LOGGER.error("Failed to addNewUrl to index", e);
        }

        if (result.getRedirectUrls() != null) {
            result.getRedirectUrls().forEach(currentUrl -> addNewUrl(currentUrl, result.getRootUrl(), jobId, index, status, subStatus, message));
        }
    }

    public void addNewUrl(String url, String rootUrl, String jobId, String index, Status status, SubStatus subStatus, String message) {

        String id = Base64.getEncoder().encodeToString(url.replace("https://", "").replace("http://", "").getBytes());

        IndexRequest indexRequest = new IndexRequest(index);
        indexRequest.id(id);
        indexRequest.type(DOCUMENT_TYPE);

        try (XContentBuilder contentBuilder = XContentFactory.jsonBuilder()) {

            contentBuilder.startObject();

            contentBuilder.field(MODIFIED_DATE, new Date().getTime());
            contentBuilder.field(STATUS, status);
            contentBuilder.field(SUB_STATUS, subStatus.name());
            contentBuilder.field(JOB_ID, jobId);
            contentBuilder.field(REASON, message);
            contentBuilder.field(ROOT_URL, rootUrl);
            contentBuilder.field(URL, url);

            contentBuilder.endObject();

            indexRequest.source(contentBuilder);

            bulkProcessor.add(indexRequest);

        } catch (IOException e) {
            LOGGER.error("Failed to addNewUrl to index", e);
        }
    }

    public Future<Boolean> getAsyncUrls(String index, List<Result> results, Status status) {

        return getAsyncUrls(index, results, status, null);
    }

    public Future<Boolean> getAsyncUrls(String index, List<Result> results, Status status, SubStatus subStatus) {

        return executor.submit(() -> {

            try {

                SearchRequest searchRequest = new SearchRequest(index);
                SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

                BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery();
                booleanQuery.must().add(QueryBuilders.termQuery(STATUS, status));

                if (subStatus != null) {
                    String subStatusKeyword = SUB_STATUS + ".keyword";
                    booleanQuery.must().add(QueryBuilders.termQuery(subStatusKeyword, subStatus));
                }

                searchSourceBuilder.query(booleanQuery);

                searchSourceBuilder.sort(URL, SortOrder.DESC);
                searchSourceBuilder.size(1);
                searchRequest.source(searchSourceBuilder);

                searchRequest.scroll(scroll);

                SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
                Long countDocuments = 0l;

                String scrollId = searchResponse.getScrollId();
                SearchHit[] searchHits = searchResponse.getHits().getHits();

                countDocuments = countDocuments + searchHits.length;

                while (searchHits != null && searchHits.length > 0) {

                    for (SearchHit searchHit : searchHits) {
                        results.add(mapResult(searchHit));
                    }

                    while (results.size() > 1000) {
                        LOGGER.debug("{} - {} list is becoming too big, sleeping a bit", index, status);
                        Thread.sleep(1000);
                    }

                    SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
                    scrollRequest.scroll(scroll);
                    searchResponse = restHighLevelClient.scroll(scrollRequest, RequestOptions.DEFAULT);
                    scrollId = searchResponse.getScrollId();
                    searchHits = searchResponse.getHits().getHits();

                    countDocuments = countDocuments + searchHits.length;
                }

                ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
                clearScrollRequest.addScrollId(scrollId);
                ClearScrollResponse clearScrollResponse = restHighLevelClient.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);

                return clearScrollResponse.isSucceeded();

            } catch (IOException e) {
                LOGGER.info("Failed to find next in scroll ", e);
            }

            return false;
        });
    }

    private Result mapResult(SearchHit searchHit) throws IOException {

        Map<String, Object> source = searchHit.getSourceAsMap();

        Result result = new Result();

        result.setRootUrl((String) source.get(ROOT_URL));
        result.setUrl((String) source.get(URL));
        result.setContentType((String) source.get(CONTENT_TYPE));
        result.setStatus((String) source.get(STATUS));
        result.setSubStatus((String) source.get(SUB_STATUS));

        if (result.getContentType() != null) {

            String contentTmp = (String) source.get(CONTENT);

            if (contentTmp != null) {

                if (result.getContentType().contains("html")) {
                    result.setContent(contentTmp.getBytes());
                } else {
                    byte[] content = Base64.getDecoder().decode(contentTmp.getBytes());
                    result.setContent(content);
                }
            }

            result.setCode((Integer) source.get(CODE));
            result.setHeaders(objectMapper.readValue((String) source.get(HEADERS), Map.class));
            result.setLength((Integer) source.get(CONTENT_SIZE));
            result.setMessage((String) source.get(MESSAGE));
            result.seteTag((String) source.get(ETAG));

        }

        return result;
    }

    public void addNewChildUrl(String url, String rootUrl, String jobId, String index) {

        String id = Base64.getEncoder().encodeToString(url.replace("https://", "").replace("http://", "").getBytes());

        GetRequest getRequest = new GetRequest(index);
        getRequest.id(id);

        try {

            boolean exists = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);

            if (exists) {

                SearchRequest searchRequest = new SearchRequest(index);
                SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
                sourceBuilder.size(0);

                BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery();
                booleanQuery.must().add(QueryBuilders.termQuery("_id", id));
                booleanQuery.must().add(QueryBuilders.termQuery(JOB_ID, jobId));

                sourceBuilder.query(booleanQuery);
                searchRequest.source(sourceBuilder);

                SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
                boolean documentWithJobIdExist = searchResponse.getHits().getTotalHits() > 0;

                if (documentWithJobIdExist) {
                    return;
                }
                
                updateStatus(url, index, Status.queue, SubStatus.included, "Found on parent page");
                return;

            }

            addNewUrl(url, rootUrl, jobId, index, Status.queue, SubStatus.included, "Found on parent page");

        } catch (IOException e1) {
            LOGGER.error("Failed to check if document exists", e1);
        }

    }

    public Result getFromIndex(String url, String index) throws IOException {

        String id = Base64.getEncoder().encodeToString(url.replace("https://", "").replace("http://", "").getBytes());

        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(1);
        sourceBuilder.query(QueryBuilders.termQuery("_id", id));

        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        if (searchResponse.getHits().getTotalHits() > 0) {

            SearchHit searchHit = searchResponse.getHits().getAt(0);
            Map<String, Object> source = searchHit.getSourceAsMap();

            Result result = new Result();
            result.setContentType((String) source.get(CONTENT_TYPE));

            String contentTmp = (String) source.get(CONTENT);

            if (contentTmp != null) {

                if (result.getContentType().contains("html")) {
                    result.setContent(contentTmp.getBytes());
                } else {
                    byte[] content = Base64.getDecoder().decode(contentTmp.getBytes());
                    result.setContent(content);
                }
            }

            result.setCode((Integer) source.get(CODE));

            String headerString = (String) source.get(HEADERS);
            if (headerString != null) {
                result.setHeaders(objectMapper.readValue(headerString, Map.class));
            }

            result.setLength((Integer) source.get(CONTENT_SIZE));
            result.setMessage((String) source.get(MESSAGE));
            result.setRootUrl((String) source.get(ROOT_URL));
            result.setUrl((String) source.get(URL));
            result.seteTag((String) source.get(ETAG));

            return result;
        }

        return null;
    }

    public boolean existsInIndex(String url, String index) throws IOException {

        String id = Base64.getEncoder().encodeToString(url.replace("https://", "").replace("http://", "").getBytes());

        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);

        sourceBuilder.query(QueryBuilders.termQuery("_id", id));
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        return searchResponse.getHits().getTotalHits() > 0;
    }

    public boolean hasMoreItemsInQueued(String index) {

        try {

            SearchRequest searchRequest = new SearchRequest(index);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.size(0);

            sourceBuilder.query(QueryBuilders.termQuery(STATUS, Status.queue));
            searchRequest.source(sourceBuilder);

            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            return searchResponse.getHits().getTotalHits() > 0;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public long totalCountWithJobId(String jobId, String index) {

        try {
            SearchRequest searchRequest = new SearchRequest(index);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.size(0);

            BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery();
            booleanQuery.must().add(QueryBuilders.termQuery(JOB_ID, jobId));
            booleanQuery.must().add(QueryBuilders.termQuery(STATUS, Status.processed));
            booleanQuery.must().add(QueryBuilders.termQuery(SUB_STATUS, SubStatus.included));
            
            sourceBuilder.query(booleanQuery);
            searchRequest.source(sourceBuilder);

            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            return searchResponse.getHits().getTotalHits();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0l;
    }
}

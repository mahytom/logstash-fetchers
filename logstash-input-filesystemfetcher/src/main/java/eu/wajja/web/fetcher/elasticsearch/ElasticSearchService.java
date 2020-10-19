package eu.wajja.web.fetcher.elasticsearch;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.flush.FlushRequest;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

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
                    xBuilder.startObject(MODIFIED_DATE).field(TYPE, NUMERIC).endObject();

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

    public void addDocument(String index, String path, Date modifiedDate) {

        String id = Base64.getEncoder().encodeToString(path.getBytes());

        IndexRequest indexRequest = new IndexRequest(index);
        indexRequest.id(id);
        indexRequest.type(DOCUMENT_TYPE);

        try (XContentBuilder contentBuilder = XContentFactory.jsonBuilder()) {

            contentBuilder.startObject();
            contentBuilder.field(MODIFIED_DATE, modifiedDate.getTime());

            contentBuilder.endObject();

            indexRequest.source(contentBuilder);
            bulkProcessor.add(indexRequest);

        } catch (IOException e) {
            LOGGER.error("Failed to addNewUrl to index", e);
        }
    }

    public boolean documentExists(String index, String path, Date modifiedDate) {

        String id = Base64.getEncoder().encodeToString(index.getBytes());

        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(1);
        sourceBuilder.query(QueryBuilders.termQuery("_id", id));

        searchRequest.source(sourceBuilder);

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            if (searchResponse.getHits().getTotalHits() > 0) {

                SearchHit searchHit = searchResponse.getHits().getAt(0);
                Map<String, Object> source = searchHit.getSourceAsMap();

                Long date = (Long) source.get(MODIFIED_DATE);

                if (modifiedDate.getTime() <= date) {
                    return true;
                }

            }
        } catch (IOException e) {
            LOGGER.error("Failed to query elastic", e);
        }

        return false;
    }

}

package eu.wajja.web.fetcher.elasticsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.flush.FlushRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.wajja.web.fetcher.model.Result;

public class ElasticSearchService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchService.class);

	private static final String DOCUMENT_TYPE = "_doc";
	public static final String INDEX_SHARDS = "index.number_of_shards";
	public static final String INDEX_NUMBER_OF_REPLICAS = "index.number_of_replicas";
	private ObjectMapper objectMapper = new ObjectMapper();

	private static final String CODE = "code";
	private static final String CONTENT_TYPE = "contentType";
	private static final String CONTENT = "content";
	private static final String CONTENT_SIZE = "contentSize";
	private static final String MESSAGE = "message";
	private static final String REASON = "reason";
	private static final String ROOT_URL = "rootUrl";
	private static final String URL = "url";
	private static final String HEADERS = "headers";
	private static final String MODIFIED_DATE = "modifiedDate";
	private static final String STATUS = "status";
	private static final String JOB_ID = "jobId";

	public static final String STATUS_QUEUE = "queue";
	public static final String STATUS_PROCESSED = "processed";
	public static final String STATUS_FAILED = "failed";
	public static final String STATUS_DELETED = "deleted";

	private static final String MAPPINGS = "mappings";
	private static final String TYPE = "type";
	private static final String KEYWORD = "keyword";
	private static final String NUMERIC = "long";
	private static final String PROPERTIES = "properties";

	private RestHighLevelClient restHighLevelClient;

	public ElasticSearchService(List<String> hostnames, String username, String password, String proxyScheme, String proxyHostname, Long proxyPort, String proxyUsername, String proxyPassword) {
		restHighLevelClient = new ElasticRestClient(hostnames, username, password, proxyScheme, proxyHostname, proxyPort, proxyUsername, proxyPassword).restHighLevelClient();
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

	public void flushIndex(String index) throws IOException {

		FlushRequest flushRequest = new FlushRequest(index);
		flushRequest.force(true);
		restHighLevelClient.indices().flush(flushRequest, RequestOptions.DEFAULT);
	}

	public void addNewUrl(Result result, String jobId, String index, String status, String message) throws IOException {

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
			contentBuilder.field(STATUS, status);
			contentBuilder.field(JOB_ID, jobId);
			contentBuilder.field(REASON, message);
			contentBuilder.endObject();

			indexRequest.source(contentBuilder);

			restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);

		} catch (IOException e) {
			LOGGER.error("Failed to addNewUrl to index", e);
		}
	}

	public void addNewUrl(String url, String jobId, String index, String status, String message) throws IOException {

		String id = Base64.getEncoder().encodeToString(url.replace("https://", "").replace("http://", "").getBytes());

		IndexRequest indexRequest = new IndexRequest(index);
		indexRequest.id(id);
		indexRequest.type(DOCUMENT_TYPE);

		try (XContentBuilder contentBuilder = XContentFactory.jsonBuilder()) {

			contentBuilder.startObject();

			contentBuilder.field(MODIFIED_DATE, new Date().getTime());
			contentBuilder.field(STATUS, status);
			contentBuilder.field(JOB_ID, jobId);
			contentBuilder.field(REASON, message);
			contentBuilder.endObject();

			indexRequest.source(contentBuilder);

			restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);

		} catch (IOException e) {
			LOGGER.error("Failed to addNewUrl to index", e);
		}
	}

	public List<Result> getUrlsToReindex(String index, Integer page) {

		List<Result> urls = new ArrayList<>();

		try {
			SearchRequest searchRequest = new SearchRequest(index);
			SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
			sourceBuilder.size(10);
			sourceBuilder.from(page * 10);

			BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery();
			booleanQuery.must().add(QueryBuilders.termQuery(STATUS, STATUS_PROCESSED));

			sourceBuilder.query(booleanQuery);
			searchRequest.source(sourceBuilder);

			SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

			for (SearchHit searchHit : searchResponse.getHits().getHits()) {

				Map<String, Object> source = searchHit.getSourceAsMap();

				Result result = new Result();

				result.setRootUrl((String) source.get(ROOT_URL));
				result.setUrl((String) source.get(URL));
				result.setContentType((String) source.get(CONTENT_TYPE));

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
					result.setRootUrl((String) source.get(ROOT_URL));
					result.setUrl((String) source.get(URL));
				}

				urls.add(result);
			}

		} catch (IOException e1) {
			LOGGER.error("Failed to find all pages to reindex", e1);
		}

		return urls;
	}

	public List<Result> getUrlsToProcess(String index, Integer page) {

		List<Result> urls = new ArrayList<>();

		try {
			SearchRequest searchRequest = new SearchRequest(index);
			SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
			sourceBuilder.size(10);
			sourceBuilder.from(page * 10);

			BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery();
			booleanQuery.must().add(QueryBuilders.termQuery(STATUS, STATUS_QUEUE));

			sourceBuilder.query(booleanQuery);
			searchRequest.source(sourceBuilder);

			SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

			for (SearchHit searchHit : searchResponse.getHits().getHits()) {

				Map<String, Object> source = searchHit.getSourceAsMap();

				Result result = new Result();

				result.setRootUrl((String) source.get(ROOT_URL));
				result.setUrl((String) source.get(URL));
				result.setContentType((String) source.get(CONTENT_TYPE));

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
					result.setRootUrl((String) source.get(ROOT_URL));
					result.setUrl((String) source.get(URL));
				}

				urls.add(result);
			}

		} catch (IOException e1) {
			LOGGER.error("Failed to find all pages to reindex", e1);
		}

		return urls;
	}

	public List<Result> getUrlsToProcess(String jobId, String index) {

		List<Result> urls = new ArrayList<>();

		try {
			SearchRequest searchRequest = new SearchRequest(index);
			SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
			sourceBuilder.size(10);

			BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery();
			booleanQuery.must().add(QueryBuilders.termQuery(JOB_ID, jobId));
			booleanQuery.must().add(QueryBuilders.termQuery(STATUS, STATUS_QUEUE));

			sourceBuilder.query(booleanQuery);
			searchRequest.source(sourceBuilder);

			SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

			for (SearchHit searchHit : searchResponse.getHits().getHits()) {

				Map<String, Object> source = searchHit.getSourceAsMap();

				Result result = new Result();

				result.setRootUrl((String) source.get(ROOT_URL));
				result.setUrl((String) source.get(URL));
				result.setContentType((String) source.get(CONTENT_TYPE));

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
					result.setRootUrl((String) source.get(ROOT_URL));
					result.setUrl((String) source.get(URL));
				}

				urls.add(result);
			}

		} catch (IOException e1) {
			LOGGER.error("Failed to check if document exists", e1);
		}

		return urls;
	}

	public List<Result> getUrlsToDelete(String jobId, String index) {

		List<Result> urls = new ArrayList<>();

		try {
			SearchRequest searchRequest = new SearchRequest(index);
			SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
			sourceBuilder.size(10);

			BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery();
			booleanQuery.mustNot().add(QueryBuilders.termQuery(JOB_ID, jobId));

			sourceBuilder.query(booleanQuery);
			searchRequest.source(sourceBuilder);

			SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

			for (SearchHit searchHit : searchResponse.getHits().getHits()) {

				Map<String, Object> source = searchHit.getSourceAsMap();

				Result result = new Result();

				result.setRootUrl((String) source.get(ROOT_URL));
				result.setUrl((String) source.get(URL));
				result.setContentType((String) source.get(CONTENT_TYPE));

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
					result.setRootUrl((String) source.get(ROOT_URL));
					result.setUrl((String) source.get(URL));
				}

				urls.add(result);
			}

		} catch (IOException e1) {
			LOGGER.error("Failed to check if document exists", e1);
		}

		return urls;
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
				sourceBuilder.size(1);

				BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery();
				booleanQuery.must().add(QueryBuilders.termQuery("_id", id));
				booleanQuery.must().add(QueryBuilders.termQuery(JOB_ID, jobId));
				booleanQuery.must().add(QueryBuilders.termsQuery(STATUS, STATUS_PROCESSED, STATUS_FAILED));

				sourceBuilder.query(booleanQuery);
				searchRequest.source(sourceBuilder);

				SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

				if (searchResponse.getHits().getTotalHits() == 0) {

					UpdateRequest updateRequest = new UpdateRequest(index, DOCUMENT_TYPE, id);

					try (XContentBuilder contentBuilder = XContentFactory.jsonBuilder()) {

						contentBuilder.startObject();
						contentBuilder.field(MODIFIED_DATE, new Date().getTime());
						contentBuilder.field(STATUS, STATUS_QUEUE);
						contentBuilder.field(JOB_ID, jobId);
						contentBuilder.endObject();

						updateRequest.doc(contentBuilder);

						restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);

					} catch (IOException e) {
						LOGGER.error("Failed to add cache to index", e);
					}

				}

			} else {

				IndexRequest indexRequest = new IndexRequest(index);
				indexRequest.id(id);
				indexRequest.type(DOCUMENT_TYPE);

				try (XContentBuilder contentBuilder = XContentFactory.jsonBuilder()) {

					contentBuilder.startObject();
					contentBuilder.field(URL, url);
					contentBuilder.field(JOB_ID, jobId);
					contentBuilder.field(ROOT_URL, rootUrl);
					contentBuilder.field(MODIFIED_DATE, new Date().getTime());
					contentBuilder.field(STATUS, STATUS_QUEUE);
					contentBuilder.field(CONTENT_SIZE, 0);
					contentBuilder.endObject();

					indexRequest.source(contentBuilder);

					restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);

				} catch (IOException e) {
					LOGGER.error("Failed to add cache to index", e);
				}

			}

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
			result.setHeaders(objectMapper.readValue((String) source.get(HEADERS), Map.class));
			result.setLength((Integer) source.get(CONTENT_SIZE));
			result.setMessage((String) source.get(MESSAGE));
			result.setRootUrl((String) source.get(ROOT_URL));
			result.setUrl((String) source.get(URL));

			return result;
		}

		return null;
	}

	public boolean existsInIndexWithSize(String url, Integer length, String jobId, String index) throws IOException {

		String id = Base64.getEncoder().encodeToString(url.replace("https://", "").replace("http://", "").getBytes());

		SearchRequest searchRequest = new SearchRequest(index);
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.size(1);

		BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery();
		booleanQuery.must().add(QueryBuilders.termQuery("_id", id));
		booleanQuery.must().add(QueryBuilders.termQuery(CONTENT_SIZE, length));
		booleanQuery.must().add(QueryBuilders.termQuery(JOB_ID, jobId));

		sourceBuilder.query(booleanQuery);
		searchRequest.source(sourceBuilder);

		SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		return searchResponse.getHits().getTotalHits() > 0;
	}

	public boolean existsInIndexWithSize(String url, Integer length, String index) throws IOException {

		String id = Base64.getEncoder().encodeToString(url.replace("https://", "").replace("http://", "").getBytes());

		SearchRequest searchRequest = new SearchRequest(index);
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.size(1);

		BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery();
		booleanQuery.must().add(QueryBuilders.termQuery("_id", id));
		booleanQuery.must().add(QueryBuilders.termQuery(CONTENT_SIZE, length));

		sourceBuilder.query(booleanQuery);
		searchRequest.source(sourceBuilder);

		SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		return searchResponse.getHits().getTotalHits() > 0;
	}

	public long totalCountWithJobId(String url, String jobId, String index) throws IOException {

		String id = Base64.getEncoder().encodeToString(url.replace("https://", "").replace("http://", "").getBytes());

		SearchRequest searchRequest = new SearchRequest(index);
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.size(0);

		BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery();
		booleanQuery.must().add(QueryBuilders.termQuery("_id", id));
		booleanQuery.must().add(QueryBuilders.termQuery(JOB_ID, jobId));

		sourceBuilder.query(booleanQuery);
		searchRequest.source(sourceBuilder);

		SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		return searchResponse.getHits().getTotalHits();
	}
}

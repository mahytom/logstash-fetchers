package eu.wajja.web.fetcher.elasticsearch;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Arrays;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

public class ElasticSearchRestClientTest {

    // @Test
    public void httpConnectionTest() throws IOException {

        ElasticRestClient client = new ElasticRestClient(Arrays.asList("http_es:1200"), "user", "*****", null, null, null, null, null);
        RestHighLevelClient restHighLevelClient = client.restHighLevelClient();
        ClusterHealthResponse health = restHighLevelClient.cluster().health(new ClusterHealthRequest(), RequestOptions.DEFAULT);
        assertNotNull(health.getClusterName());
    }

    // @Test
    public void httpsConnectionTest() throws IOException {

        ElasticRestClient client = new ElasticRestClient(Arrays.asList("https://es_https.com"), "user", "*****", null, null, null, null, null);
        RestHighLevelClient restHighLevelClient = client.restHighLevelClient();
        ClusterHealthResponse health = restHighLevelClient.cluster().health(new ClusterHealthRequest(), RequestOptions.DEFAULT);
        assertNotNull(health.getClusterName());
    }
}

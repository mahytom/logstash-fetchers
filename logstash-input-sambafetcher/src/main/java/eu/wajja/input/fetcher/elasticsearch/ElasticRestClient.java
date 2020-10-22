package eu.wajja.input.fetcher.elasticsearch;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticRestClient.class);

    private List<String> hostnames;
    private String username;
    private String password;

    public ElasticRestClient(List<String> hostnames, String username, String password) {

        this.hostnames = hostnames;
        this.username = username;
        this.password = password;
    }

    public RestHighLevelClient restHighLevelClient() {

        List<HttpHost> httpHosts = new ArrayList<>();
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        hostnames.stream().forEach(h -> {

            String host = h.split(":")[0];
            Integer port = new Integer(h.split(":")[1]);

            try {

                HttpHost httpHost = new HttpHost(InetAddress.getByName(host), port);
                httpHosts.add(httpHost);

                if (username != null && password != null) {
                    credentialsProvider.setCredentials(new AuthScope(httpHost), new UsernamePasswordCredentials(username, password));
                }

            } catch (NumberFormatException | UnknownHostException e) {
                LOGGER.error("Failed to find correct elastic node", e);
            }
        });

        RestClientBuilder restClientBuilder = RestClient.builder(httpHosts.toArray(new HttpHost[httpHosts.size()]));
        restClientBuilder = restClientBuilder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));

        return new RestHighLevelClient(restClientBuilder);
    }
}

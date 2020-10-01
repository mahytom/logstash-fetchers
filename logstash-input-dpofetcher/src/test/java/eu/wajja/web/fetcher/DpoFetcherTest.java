package eu.wajja.web.fetcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.logstash.plugins.ConfigurationImpl;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.elastic.logstash.api.Configuration;

public class DpoFetcherTest {

    private Properties properties;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void intialize() throws IOException {

        properties = new Properties();
        properties.load(this.getClass().getClassLoader().getResourceAsStream("test.properties"));
    }

    @Test
    public void testWebFetcher() throws IOException {

        Map<String, Object> configValues = new HashMap<>();

        String excludeData = (String) properties.get(DpoFetcher.PROPERTY_EXCLUDE_DATA);
        String[] arrayExcludeData = objectMapper.readValue(excludeData, String[].class);

        configValues.put(DpoFetcher.CONFIG_URLS.name(), Arrays.asList(objectMapper.readValue((String) properties.get(DpoFetcher.PROPERTY_URLS), String[].class)));
        configValues.put(DpoFetcher.CONFIG_EXCLUDE_DATA.name(), Arrays.asList(arrayExcludeData));
        configValues.put(DpoFetcher.CONFIG_EXCLUDE_LINK.name(), Arrays.asList(objectMapper.readValue((String) properties.get(DpoFetcher.PROPERTY_EXCLUDE_LINK), String[].class)));
        configValues.put(DpoFetcher.CONFIG_CRON.name(), properties.get(DpoFetcher.PROPERTY_CRON));
        configValues.put(DpoFetcher.CONFIG_TIMEOUT.name(), new Long((String) properties.get(DpoFetcher.PROPERTY_TIMEOUT)));
        configValues.put(DpoFetcher.CONFIG_MAX_PAGES.name(), new Long((String) properties.get(DpoFetcher.PROPERTY_MAX_PAGES)));
        configValues.put(DpoFetcher.CONFIG_DISABLE_SSL_CHECK.name(), new Boolean((String) properties.get(DpoFetcher.PROPERTY_SSL_CHECK)));
        configValues.put(DpoFetcher.CONFIG_CHROME_DRIVERS.name(), Arrays.asList(objectMapper.readValue((String) properties.get(DpoFetcher.PROPERTY_CHROME_DRIVERS), String[].class)));
        configValues.put(DpoFetcher.CONFIG_ELASTIC_HOSTNAMES.name(), Arrays.asList(objectMapper.readValue((String) properties.get(DpoFetcher.PROPERTY_ELASTIC_HOSTNAMES), String[].class)));
        configValues.put(DpoFetcher.CONFIG_REINDEX.name(), new Boolean((String) properties.get(DpoFetcher.PROPERTY_REINDEX)));
        configValues.put(DpoFetcher.CONFIG_ENABLE_CRAWL.name(), new Boolean((String) properties.get(DpoFetcher.PROPERTY_ENABLE_CRAWL)));
        configValues.put(DpoFetcher.CONFIG_READ_ROBOT.name(), new Boolean((String) properties.get(DpoFetcher.PROPERTY_READ_ROBOT)));
        configValues.put(DpoFetcher.CONFIG_WAIT_FOR_CSS_SELECTOR.name(), (String) properties.get(DpoFetcher.PROPERTY_WAIT_FOR_CSS_SELECTOR));

        configValues.put(DpoFetcher.CONFIG_ENABLE_REGEX.name(), new Boolean((String) properties.get(DpoFetcher.PROPERTY_ENABLE_REGEX)));
        configValues.put(DpoFetcher.CONFIG_ENABLE_DELETE.name(), new Boolean((String) properties.get(DpoFetcher.PROPERTY_ENABLE_DELETE)));
        configValues.put(DpoFetcher.CONFIG_ROOT_URL.name(), properties.get(DpoFetcher.PROPERTY_ROOT_URL));

        Configuration config = new ConfigurationImpl(configValues);
        DpoFetcher webFetcher = new DpoFetcher("test-id", config, null);
        webFetcher.stopped = true;

        TestConsumer testConsumer = new TestConsumer();
        webFetcher.start(testConsumer);

        List<Map<String, Object>> events = testConsumer.getEvents();

        Assert.assertEquals(0, events.size());
        webFetcher.stop();
    }

    private static class TestConsumer implements Consumer<Map<String, Object>> {

        private List<Map<String, Object>> events = new ArrayList<>();

        @Override
        public void accept(Map<String, Object> event) {

            synchronized (this) {

                if (events.size() < 10) {
                    events.add(event);
                }
            }
        }

        public List<Map<String, Object>> getEvents() {

            return events;
        }
    }
}

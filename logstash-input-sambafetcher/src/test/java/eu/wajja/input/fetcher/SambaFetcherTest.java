package eu.wajja.input.fetcher;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SambaFetcherTest {

    @Ignore
    @Test
    public void SambaFetcherTest() throws URISyntaxException, JobExecutionException {

        List<Map<String, Object>> savesItems = new ArrayList<>();

        Consumer<Map<String, Object>> consumer = new Consumer<Map<String, Object>>() {

            @Override
            public void accept(Map<String, Object> t) {

                savesItems.add(t);
            }
        };

        Map<String, Object> map = new HashMap<>();

        map.put(SambaFetcher.PROPERTY_CONSUMER, consumer);
        map.put(SambaFetcher.PROPERTY_ELASTIC_HOSTNAMES, Arrays.asList("localhost:9200"));

        JobDataMap jobDataMap = new JobDataMap(map);

        SambaFetcherJob SambaFetcherJob = new SambaFetcherJob();

        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        JobDetail jobDetail = Mockito.mock(JobDetail.class);

        Mockito.when(context.getJobDetail()).thenReturn(jobDetail);
        Mockito.when(jobDetail.getJobDataMap()).thenReturn(jobDataMap);

        SambaFetcherJob.execute(context);

        Assert.assertEquals(4, savesItems.size());

    }

    @Ignore
    @Test
    public void SambaFetcherExcludeJarTest() throws URISyntaxException, JobExecutionException {

        URI uriTestData = SambaFetcherTest.class.getClassLoader().getSystemClassLoader().getResource("test-data-2").toURI();
        File file = new File(uriTestData);
        String fullPath = file.getAbsolutePath();

        List<Map<String, Object>> savesItems = new ArrayList<>();

        Consumer<Map<String, Object>> consumer = new Consumer<Map<String, Object>>() {

            @Override
            public void accept(Map<String, Object> t) {

                savesItems.add(t);
            }
        };

        Map<String, Object> map = new HashMap<>();

        map.put(SambaFetcher.PROPERTY_CONSUMER, consumer);
        map.put(SambaFetcher.PROPERTY_ELASTIC_HOSTNAMES, Arrays.asList("localhost:9200"));
        map.put(SambaFetcher.PROPERTY_EXCLUDE, Arrays.asList(".*\\.jar"));

        JobDataMap jobDataMap = new JobDataMap(map);

        SambaFetcherJob SambaFetcherJob = new SambaFetcherJob();

        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        JobDetail jobDetail = Mockito.mock(JobDetail.class);

        Mockito.when(context.getJobDetail()).thenReturn(jobDetail);
        Mockito.when(jobDetail.getJobDataMap()).thenReturn(jobDataMap);

        SambaFetcherJob.execute(context);

        Assert.assertEquals(4, savesItems.size());

    }
}

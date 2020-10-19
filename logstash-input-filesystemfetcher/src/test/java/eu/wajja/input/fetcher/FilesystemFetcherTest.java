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
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class FilesystemFetcherTest {

    @Test
    public void filesystemFetcherTest() throws URISyntaxException, JobExecutionException {

        URI uriTestData = FilesystemFetcherTest.class.getClassLoader().getSystemClassLoader().getResource("test-data-1").toURI();
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
        map.put(FilesystemFetcher.PROPERTY_PATHS, Arrays.asList(fullPath));
        map.put(FilesystemFetcher.PROPERTY_CONSUMER, consumer);
        map.put(FilesystemFetcher.PROPERTY_THREADS, 1l);
        map.put(FilesystemFetcher.PROPERTY_PROXY_PORT, 1l);
        map.put(FilesystemFetcher.PROPERTY_SSL_CHECK, false);
        map.put(FilesystemFetcher.PROPERTY_ELASTIC_HOSTNAMES, Arrays.asList("localhost:9200"));

        JobDataMap jobDataMap = new JobDataMap(map);

        FilesystemFetcherJob filesystemFetcherJob = new FilesystemFetcherJob();

        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        JobDetail jobDetail = Mockito.mock(JobDetail.class);

        Mockito.when(context.getJobDetail()).thenReturn(jobDetail);
        Mockito.when(jobDetail.getJobDataMap()).thenReturn(jobDataMap);

        filesystemFetcherJob.execute(context);

        Assert.assertEquals(4, savesItems.size());

    }

    @Test
    public void filesystemFetcherExcludeJarTest() throws URISyntaxException, JobExecutionException {

        URI uriTestData = FilesystemFetcherTest.class.getClassLoader().getSystemClassLoader().getResource("test-data-2").toURI();
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
        map.put(FilesystemFetcher.PROPERTY_PATHS, Arrays.asList(fullPath));
        map.put(FilesystemFetcher.PROPERTY_CONSUMER, consumer);
        map.put(FilesystemFetcher.PROPERTY_THREADS, 1l);
        map.put(FilesystemFetcher.PROPERTY_PROXY_PORT, 1l);
        map.put(FilesystemFetcher.PROPERTY_SSL_CHECK, false);
        map.put(FilesystemFetcher.PROPERTY_ELASTIC_HOSTNAMES, Arrays.asList("localhost:9200"));
        map.put(FilesystemFetcher.PROPERTY_EXCLUDE, Arrays.asList(".*\\.jar"));

        JobDataMap jobDataMap = new JobDataMap(map);

        FilesystemFetcherJob filesystemFetcherJob = new FilesystemFetcherJob();

        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        JobDetail jobDetail = Mockito.mock(JobDetail.class);

        Mockito.when(context.getJobDetail()).thenReturn(jobDetail);
        Mockito.when(jobDetail.getJobDataMap()).thenReturn(jobDataMap);

        filesystemFetcherJob.execute(context);

        Assert.assertEquals(4, savesItems.size());

    }
}

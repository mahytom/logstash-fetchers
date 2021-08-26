package eu.wajja.web.fetcher.content;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import eu.wajja.web.fetcher.model.Result;

public class HtmlAnalyzerTest {

    @Test
    public void testNoRobotsMetaHtml() throws IOException {

        byte[] content = null;
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("noRobots.html")) {
            content = IOUtils.toByteArray(is);
        }

        Result result = new Result();
        result.setContentType("text/html");
        result.setContent(content);
        ContentAnalyzer analyzer = ContentAnalyzer.getInstance(result, true);
        assertNotNull(analyzer);
        assertFalse(analyzer.isExcluded());
    }

    @Test
    public void testIndexRobotsMetaHtml() throws IOException {

        byte[] content = null;
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("indexRobots.html")) {
            content = IOUtils.toByteArray(is);
        }

        Result result = new Result();
        result.setContentType("text/html");
        result.setContent(content);
        ContentAnalyzer analyzer = ContentAnalyzer.getInstance(result, true);
        assertNotNull(analyzer);
        assertFalse(analyzer.isExcluded());
    }

    @Test
    public void testNoIndexRobotsMetaHtml() throws IOException {

        byte[] content = null;
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("noindexRobots.html")) {
            content = IOUtils.toByteArray(is);
        }

        Result result = new Result();
        result.setContentType("text/html");
        result.setContent(content);
        ContentAnalyzer analyzer = ContentAnalyzer.getInstance(result, true);
        assertNotNull(analyzer);
        assertTrue(analyzer.isExcluded());
        
        analyzer = ContentAnalyzer.getInstance(result, false);
        assertNotNull(analyzer);
        assertFalse(analyzer.isExcluded());
    }

}

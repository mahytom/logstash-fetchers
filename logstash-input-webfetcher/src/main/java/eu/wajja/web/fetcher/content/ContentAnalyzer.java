package eu.wajja.web.fetcher.content;

import static java.util.Objects.nonNull;
import static org.apache.http.entity.ContentType.TEXT_HTML;

import eu.wajja.web.fetcher.model.Result;

/**
 * Base interface for content analysis.
 * 
 * @author miekuma
 *
 */
public interface ContentAnalyzer {

    /**
     * Does analysis of the content and returns the flag if it should be
     * excluded or not
     * 
     * @return
     */
    boolean isExcluded();

    /**
     * It returns the reason of exclusion
     * 
     * @return
     */
    String getExclusionReason();

    public static ContentAnalyzer getInstance(Result result, boolean readRobot) {

        if (nonNull(result) && TEXT_HTML.getMimeType().equals(result.getContentType())) {
            return new HtmlAnalyzer(result, readRobot);
        }

        return null;
    }
}

package eu.wajja.rss.presscorner.fetcher.controller;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.net.ssl.SSLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.wajja.rss.presscorner.fetcher.elasticsearch.ElasticSearchService;
import eu.wajja.rss.presscorner.fetcher.model.Result;

public class URLController {

    private static final Logger LOGGER = LoggerFactory.getLogger(URLController.class);

    private Long timeout;
    private String userAgent;
    private String referer;
    private String waitForCssSelector;
    private Integer maxWaitForCssSelector;
    private ElasticSearchService elasticSearchService;
    private WebDriverController webDriverController = new WebDriverController();

    public URLController(ElasticSearchService elasticSearchService, Long timeout, String userAgent, String referer, String waitForCssSelector, Integer maxWaitForCssSelector) {

        this.timeout = timeout;
        this.userAgent = userAgent;
        this.referer = referer;
        this.waitForCssSelector = waitForCssSelector;
        this.maxWaitForCssSelector = maxWaitForCssSelector;
        this.elasticSearchService = elasticSearchService;
    }

    public Result getURL(String index, String currentUrl, String initialUrl, String chromeDriver) {

        return getURL(index, currentUrl, initialUrl, chromeDriver, new HashSet<>());
    }

    private Result getURL(String index, String currentUrl, String initialUrl, String chromeDriver, Set<String> redirectUrls) {

        Result result = new Result();
        result.setUrl(currentUrl);
        result.setRootUrl(initialUrl);
        result.setCode(404);
        result.setRedirectUrls(redirectUrls);

        HttpURLConnection httpURLConnection = null;

        try {

            URL url = this.createUrl(currentUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setConnectTimeout(timeout.intValue());
            httpURLConnection.setReadTimeout(timeout.intValue());
            httpURLConnection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
            httpURLConnection.addRequestProperty("User-Agent", userAgent);
            httpURLConnection.addRequestProperty("Referer", referer);
            httpURLConnection.setRequestMethod("HEAD");

            httpURLConnection.connect();

            int code = httpURLConnection.getResponseCode();
            String message = httpURLConnection.getResponseMessage();

            result.setCode(code);
            result.setMessage(message);
            result.setContentType(parseContentType(httpURLConnection.getContentType()));

            if (code == HttpURLConnection.HTTP_OK) {

                if (elasticSearchService.existsInIndex(currentUrl, index)) {

                    result = elasticSearchService.getFromIndex(currentUrl, index);
                    result.setRedirectUrls(redirectUrls);
                    return result;
                }

                result.setContentType("text/html");
                closeConnection(httpURLConnection);
                result.setHeaders(httpURLConnection.getHeaderFields());
                byte[] bytes = webDriverController.getURL(result.getUrl(), chromeDriver, waitForCssSelector, maxWaitForCssSelector);
                result.setContent(bytes);

            } else if (code == HttpURLConnection.HTTP_MOVED_TEMP || code == HttpURLConnection.HTTP_MOVED_PERM || code == 307 || code == HttpURLConnection.HTTP_SEE_OTHER) {

                String newUrl = httpURLConnection.getHeaderField("Location");
                closeConnection(httpURLConnection);

                LOGGER.debug("Redirect needed to :  {}", newUrl);
                result.getRedirectUrls().add(currentUrl);
                return getURL(index, newUrl, initialUrl, chromeDriver, result.getRedirectUrls());

            } else {
                LOGGER.warn("Failed To Read status {}, url {}, message {}", code, url, message);
            }

        } catch (SocketTimeoutException e) {

            LOGGER.warn("Thread url {}, sleeping and trying again", currentUrl);
            closeConnection(httpURLConnection);
            return result;

        } catch (SSLException e) {

            LOGGER.warn("Thread url {}, SSLException error, sleeping and trying again", currentUrl, e);
            closeConnection(httpURLConnection);
            return getURL(index, currentUrl, initialUrl, chromeDriver);

        } catch (MalformedURLException mue) {
            LOGGER.error("Malformed url : {}", currentUrl, mue);
            closeConnection(httpURLConnection);
            return result;

        } catch (Exception e) {
            LOGGER.error("Failed to retrieve URL url {}", currentUrl, e);
        } finally {
            closeConnection(httpURLConnection);
        }

        return result;
    }

    private String parseContentType(String contentType) {

        if (contentType == null || contentType.isEmpty()) {
            return "application/octet-stream";
        }

        if (contentType.contains(";")) {
            contentType = contentType.substring(0, contentType.indexOf(';'));
        }

        contentType = contentType.replace("\"", "");

        return contentType.trim().toLowerCase();
    }

    private void closeConnection(HttpURLConnection httpURLConnection) {

        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
    }

    public URL createUrl(String currentUrl) throws MalformedURLException {

        return new URL(currentUrl);
    }

    public void setTimeout(Long timeOut) {

        this.timeout = timeOut;
    }

}

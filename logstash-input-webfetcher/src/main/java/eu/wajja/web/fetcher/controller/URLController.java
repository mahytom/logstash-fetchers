package eu.wajja.web.fetcher.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.net.ssl.SSLException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.wajja.web.fetcher.elasticsearch.ElasticSearchService;
import eu.wajja.web.fetcher.model.Result;
import eu.wajja.web.fetcher.model.WebDriverResult;

public class URLController {

    private static final Logger LOGGER = LoggerFactory.getLogger(URLController.class);

    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";
    
    private Proxy proxy;
    private Long timeout;
    private String userAgent;
    private String referer;
    private Boolean enableJsLinks;
    private String waitForCssSelector;
    private Integer maxWaitForCssSelector;
    private ElasticSearchService elasticSearchService;
    private WebDriverController webDriverController = new WebDriverController();

    public URLController(ElasticSearchService elasticSearchService, Proxy proxy, Long timeout, String userAgent, String referer, String waitForCssSelector, Integer maxWaitForCssSelector, Boolean enableJsLinks) {

        this.proxy = proxy;
        this.timeout = timeout;
        this.userAgent = userAgent;
        this.referer = referer;
        this.waitForCssSelector = waitForCssSelector;
        this.maxWaitForCssSelector = maxWaitForCssSelector;
        this.elasticSearchService = elasticSearchService;
        this.enableJsLinks = enableJsLinks;
    }

    public Result getURL(String index, String currentUrl, String initialUrl, String chromeDriver) {

        return getURL(index, currentUrl, initialUrl, chromeDriver, new HashSet<>(), 0);
    }

    private Result getURL(String index, String currentUrl, String initialUrl, String chromeDriver, Set<String> redirectUrls, Integer redirectCount) {

        Result result = new Result();
        result.setUrl(currentUrl);
        result.setReferrer(currentUrl);
        result.setRootUrl(initialUrl);
        result.setCode(404);
        result.setRedirectUrls(redirectUrls);
        result.setCached(false);
        
        HttpURLConnection httpURLConnection = null;

        try {

            URL url = this.createUrl(currentUrl);

            if (proxy == null) {
                httpURLConnection = (HttpURLConnection) url.openConnection();
            } else {
                httpURLConnection = (HttpURLConnection) url.openConnection(proxy);
            }

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

                    if (isSameDocument(httpURLConnection, result)) {
                        result.setCached(true);
                        return result;
                    }

                    // Result that was queued from parent url. still needs
                    // populating

                    result.setCode(code);
                    result.setMessage(message);
                    result.setContentType(parseContentType(httpURLConnection.getContentType()));
                    result.setRootUrl(initialUrl);
                }

                /*
                 * If pdf, do not use the webdriver, which was only used
                 * intitially to handle js
                 */
                if (httpURLConnection.getContentType().equals("application/pdf") || currentUrl.substring(currentUrl.length() - 3).equalsIgnoreCase("pdf")) {

                    LOGGER.debug("Found pdf, downloading {}", currentUrl);
                    result.setHeaders(httpURLConnection.getHeaderFields());
                    
                    byte[] content = downloadContent(currentUrl);
                    String md5 = DigestUtils.md5Hex(content);
                    
                    result.setContent(content);
                    result.setMd5(md5);
                    
                } else {

                    closeConnection(httpURLConnection);
                    result.setHeaders(httpURLConnection.getHeaderFields());

                    if (chromeDriver == null) {
                        
                        byte[] content = downloadContent(currentUrl);
                        String md5 = DigestUtils.md5Hex(content);
                        
                        result.setContent(content);
                        result.setMd5(md5);
                        
                    } else {
                       
                        WebDriverResult webDriverResult = webDriverController.getURL(result.getUrl(), chromeDriver, waitForCssSelector, maxWaitForCssSelector, enableJsLinks);
                        String md5 = DigestUtils.md5Hex(webDriverResult.getBytes());
                        
                        result.setContent(webDriverResult.getBytes());
                        result.setMd5(md5);
                        
                        result.setChildUrls(webDriverResult.getUrls());
                    }

                }

            } else if (code == HttpURLConnection.HTTP_MOVED_PERM) {
            	
            	LOGGER.warn("Pages moved permanently, status {}, url {}, message {}", code, url, message);
            	return result;
            	
            } else if (code == HttpURLConnection.HTTP_MOVED_TEMP || code == 307 || code == HttpURLConnection.HTTP_SEE_OTHER) {

                String newUrl = httpURLConnection.getHeaderField("Location");
                closeConnection(httpURLConnection);

				if (redirectCount < 10) {

					String simpleUrlString = newUrl.replace(HTTP, "").replace(HTTPS, "");
					String simpleRootUrlString = result.getRootUrl().replace(HTTP, "").replace(HTTPS, "");

					if (simpleUrlString.startsWith(simpleRootUrlString)) {
						
						LOGGER.debug("Redirect needed to :  {}", newUrl);
						result.getRedirectUrls().add(currentUrl);
						return getURL(index, newUrl, initialUrl, chromeDriver, result.getRedirectUrls(), redirectCount + 1);
					}
				}

            } else {
                LOGGER.warn("Failed To Read status {}, url {}, message {}", code, url, message);

                if (redirectCount < 10 && code == 502) {

                    // Bad Gateway, sleep and try again
                    Thread.sleep(5000);
                    return getURL(index, currentUrl, initialUrl, chromeDriver, redirectUrls, redirectCount + 1);
                }
            }

        } catch (SocketTimeoutException e) {

            LOGGER.warn("Thread url {}, sleeping and trying again", currentUrl);
            closeConnection(httpURLConnection);
            return result;

        } catch (SSLException e) {

            LOGGER.warn("Thread url {}, SSLException error, sleeping and trying again", currentUrl, e);
            closeConnection(httpURLConnection);
            return result;

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

    private boolean isSameDocument(HttpURLConnection httpURLConnection, Result result) {

        int length = httpURLConnection.getContentLength();
        String eTag = httpURLConnection.getHeaderField("ETag");

        if (eTag != null && result.geteTag() != null && result.geteTag().equals(eTag)) {
            return true;

        } else if (length > 0 && result.getLength() != null && result.getLength().equals(length)) {
            return true;

        } else {

            result.setLength(length);
            result.seteTag(eTag);

            return false;
        }

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

    private byte[] downloadContent(String currentUrl) {

        HttpURLConnection httpURLConnection = null;

        try {

            URL url = this.createUrl(currentUrl);

            if (proxy == null) {
                httpURLConnection = (HttpURLConnection) url.openConnection();
            } else {
                httpURLConnection = (HttpURLConnection) url.openConnection(proxy);
            }

            httpURLConnection.setConnectTimeout(timeout.intValue());
            httpURLConnection.setReadTimeout(timeout.intValue());
            httpURLConnection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
            httpURLConnection.addRequestProperty("User-Agent", userAgent);
            httpURLConnection.addRequestProperty("Referer", referer);

            httpURLConnection.connect();

            try (InputStream inputStream = httpURLConnection.getInputStream()) {

                byte[] bytes = IOUtils.toByteArray(inputStream);
                closeConnection(httpURLConnection);
                return bytes;
            }

        } catch (SocketTimeoutException e) {

            LOGGER.warn("Thread url {}, sleeping and trying again", currentUrl);
            closeConnection(httpURLConnection);

        } catch (SSLException e) {

            LOGGER.warn("Thread url {}, SSLException error, sleeping and trying again", currentUrl, e);
            closeConnection(httpURLConnection);

        } catch (MalformedURLException mue) {
            LOGGER.error("Malformed url : {}", currentUrl, mue);
            closeConnection(httpURLConnection);

        } catch (Exception e) {
            LOGGER.error("Failed to retrieve URL url {}", currentUrl, e);
        } finally {
            closeConnection(httpURLConnection);
        }

        return null;

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

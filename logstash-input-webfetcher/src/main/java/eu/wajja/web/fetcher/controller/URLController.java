package eu.wajja.web.fetcher.controller;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import eu.wajja.web.fetcher.FetcherJob;
import eu.wajja.web.fetcher.model.Result;

public class URLController {

	private static final Logger LOGGER = LogManager.getLogger(FetcherJob.class);

	private Proxy proxy;
	private Long timeout;
	private String userAgent;
	private String referer;

	public URLController(Proxy proxy, Long timeout, String userAgent, String referer) {

		this.proxy = proxy;
		this.timeout = timeout;
		this.userAgent = userAgent;
		this.referer = referer;
	}

	public Result getURL(String currentUrl, String initialUrl) {

		HttpURLConnection httpURLConnection = null;
		Result result = new Result();

		try {

			URL url = new URL(currentUrl);

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

			int code = httpURLConnection.getResponseCode();
			String message = httpURLConnection.getResponseMessage();
			result.setCode(code);
			result.setMessage(message);
			result.setUrl(currentUrl);
			result.setRootUrl(initialUrl);

			if (code == HttpURLConnection.HTTP_OK) {

				InputStream inputStream = httpURLConnection.getInputStream();
				byte[] content = IOUtils.toByteArray(inputStream);

				result.setContent(content);
				result.setHeaders(httpURLConnection.getHeaderFields());

			} else if (code == HttpURLConnection.HTTP_MOVED_TEMP || code == HttpURLConnection.HTTP_MOVED_PERM || code == HttpURLConnection.HTTP_SEE_OTHER) {

				String newUrl = httpURLConnection.getHeaderField("Location");

				if (!newUrl.startsWith(initialUrl)) {
					LOGGER.debug("Not redirecting to external url  {}", newUrl);
				} else {
					LOGGER.debug("Redirect needed to :  {}", newUrl);
					return getURL(newUrl, initialUrl);
				}

			} else {
				LOGGER.warn("Failed To Read status {}, url {}, message {}", code, url, message);
			}

		} catch (SocketTimeoutException e) {

			LOGGER.warn("Thread url {}, sleeping and trying again", currentUrl);

			try {
				Thread.sleep(3000);
				return getURL(currentUrl, initialUrl);

			} catch (InterruptedException e1) {
				Thread.currentThread().interrupt();
			}

		} catch (Exception e) {
			LOGGER.error("Failed to retrieve URL url {}", currentUrl, e);
		} finally {

			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}

		return result;
	}

}

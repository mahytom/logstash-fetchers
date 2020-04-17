package eu.wajja.input.fetcher.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

public class UrlHelper {

	public URLConnection getUrl(String username, String password, String urlToDownload) throws MalformedURLException, IOException {

		String basicAuthentication = new StringBuilder(username).append(":").append(password).toString();
		String encoding = Base64.getEncoder().encodeToString(basicAuthentication.getBytes());

		URLConnection urlConnection = new URL(urlToDownload).openConnection();
		urlConnection.setRequestProperty("Authorization", new StringBuilder("Basic ").append(encoding).toString());

		return urlConnection;

	}
}

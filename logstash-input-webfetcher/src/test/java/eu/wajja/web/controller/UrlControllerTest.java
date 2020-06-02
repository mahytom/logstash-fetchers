package eu.wajja.web.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import eu.wajja.web.fetcher.controller.URLController;;

@RunWith(MockitoJUnitRunner.class)
public class UrlControllerTest {

	@InjectMocks
	@Spy
	private URLController urlController;

	@Mock
	private HttpURLConnection httpURLConnection;

	@Mock
	private URL url;

	@Test
	public void pdfExtensionSuffixTesting() throws IOException {

		String chromeDriver = "http://localhost:3000";
		String currentUrl = "https://ec.europa.eu/belgium/sites/belgium/files/og_image/poster_drapeau_europeen.pdf";
		String initialUrl = "https://ec.europa.eu/belgium/";

		urlController.setTimeout(5L);
		Mockito.when(url.openConnection()).thenReturn(httpURLConnection);
		Mockito.doReturn(url).when(urlController).createUrl(Mockito.anyString());
		Mockito.when(httpURLConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
		Mockito.when(httpURLConnection.getContentType()).thenReturn("html");

		urlController.getURL(currentUrl, initialUrl, chromeDriver);

		Mockito.verify(httpURLConnection).getInputStream();

	}

	@Test
	public void pdfExtensionContentTypeTesting() throws IOException {

		String chromeDriver = "http://localhost:3000";
		String currentUrl = "https://ec.europa.eu/belgium/sites/belgium/files/og_image/poster_drapeau_europeen.html";
		String initialUrl = "https://ec.europa.eu/belgium/";

		urlController.setTimeout(5L);
		Mockito.when(url.openConnection()).thenReturn(httpURLConnection);
		Mockito.doReturn(url).when(urlController).createUrl(Mockito.anyString());
		Mockito.when(httpURLConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
		Mockito.when(httpURLConnection.getContentType()).thenReturn("application/pdf");

		urlController.getURL(currentUrl, initialUrl, chromeDriver);

		Mockito.verify(httpURLConnection).getInputStream();

	}

	@Test
	public void nonPdfExtensionTesting() throws IOException {

		String chromeDriver = "http://localhost:3000";
		String currentUrl = "https://ec.europa.eu/belgium/sites/belgium/files/og_image/poster_drapeau_europeen.html";
		String initialUrl = "https://ec.europa.eu/belgium/";

		urlController.setTimeout(5L);
		Mockito.when(url.openConnection()).thenReturn(httpURLConnection);
		Mockito.doReturn(url).when(urlController).createUrl(Mockito.anyString());
		Mockito.when(httpURLConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
		Mockito.when(httpURLConnection.getContentType()).thenReturn("html");

		urlController.getURL(currentUrl, initialUrl, chromeDriver);

		Mockito.verify(httpURLConnection, Mockito.never()).getInputStream();

	}
}

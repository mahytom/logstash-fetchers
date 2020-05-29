package eu.wajja.web.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import java.net.HttpURLConnection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import eu.wajja.web.fetcher.controller.URLController;
import eu.wajja.web.fetcher.controller.WebDriverController;;

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
	public void pdfExtensionTesting() throws IOException {

        String chromeDriver = "http://localhost:3000";
		String currentUrl = "https://ec.europa.eu/belgium/sites/belgium/files/og_image/poster_drapeau_europeen.pdf";
        String initialUrl = "https://ec.europa.eu/belgium/";

        urlController.setTimeout(5L);
        Mockito.when(url.openConnection()).thenReturn(httpURLConnection);
        Mockito.doReturn(url).when(urlController).createUrl(Mockito.anyString());
        Mockito.when(httpURLConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

        urlController.getURL(currentUrl,initialUrl,chromeDriver);

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

        urlController.getURL(currentUrl,initialUrl,chromeDriver);

		Mockito.verify(httpURLConnection, Mockito.never()).getInputStream();

	}
}

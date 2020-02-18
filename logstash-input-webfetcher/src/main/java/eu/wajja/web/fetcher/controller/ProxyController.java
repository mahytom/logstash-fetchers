package eu.wajja.web.fetcher.controller;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.ProxyConfig;
import com.machinepublishers.jbrowserdriver.ProxyConfig.Type;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.Settings.Builder;
import com.machinepublishers.jbrowserdriver.Timezone;
import com.machinepublishers.jbrowserdriver.UserAgent;

public class ProxyController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyController.class);

	private Proxy proxy = null;
	private WebDriver driver = null;

	public ProxyController(String proxyUser, String proxyPass, String proxyHost, Long proxyPort, Boolean disableSSLcheck, String chromeDriver, Boolean waitJavascript, Long timeout) {

		if (proxyUser != null && proxyPass != null) {

			LOGGER.info("Initializing Proxy Security {}:{}", proxyHost, proxyPort);
			Authenticator authenticator = new Authenticator() {

				@Override
				public PasswordAuthentication getPasswordAuthentication() {

					return new PasswordAuthentication(proxyUser, proxyPass.toCharArray());
				}
			};

			Authenticator.setDefault(authenticator);
		}

		if (proxyHost != null && proxyPort != null) {

			LOGGER.info("Initializing Proxy {}:{}", proxyHost, proxyPort);
			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort.intValue()));
		}

		if (!disableSSLcheck) {

			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			try {
				SSLContext sc = SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			} catch (NoSuchAlgorithmException | KeyManagementException e) {
				LOGGER.error("Failed to set authentication cert trust", e);
			}

			HostnameVerifier allHostsValid = new HostnameVerifier() {

				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		}

		if (waitJavascript) {

			if (StringUtils.isEmpty(chromeDriver)) {

				Builder settings = Settings.builder().timezone(Timezone.EUROPE_BRUSSELS)
						.connectTimeout(timeout.intValue())
						.maxConnections(50)
						.quickRender(true)
						.blockMedia(true)
						.userAgent(UserAgent.CHROME)
						.logger("ch.qos.logback.core.ConsoleAppender")
						.processes(2)
						.loggerLevel(java.util.logging.Level.INFO)
						.hostnameVerification(false);

				if (proxyUser != null && proxyPass != null && proxyPort != null) {

					ProxyConfig proxyConfig = new ProxyConfig(Type.HTTP, proxyHost, proxyPort.intValue(), proxyUser, proxyPass);
					settings.proxy(proxyConfig);
					settings.javaOptions("-Djdk.http.auth.tunneling.disabledSchemes=");
				}

				driver = new JBrowserDriver(settings.build());

			} else {

				Path chrome = Paths.get(chromeDriver);
				Boolean isExecutable = chrome.toFile().setExecutable(true);

				if (isExecutable) {
					LOGGER.info("set {} to be executable", chromeDriver);
				}

				System.setProperty("webdriver.chrome.driver", chrome.toAbsolutePath().toString());

				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.addArguments("--headless");
				chromeOptions.addArguments("--no-sandbox");
				chromeOptions.addArguments("--disable-dev-shm-usage");

				driver = new ChromeDriver(chromeOptions);

				// https://github.com/seleniumhq/selenium-google-code-issue-archive/issues/27
				((JavascriptExecutor) driver).executeScript("window.alert = function(msg) { }");
				((JavascriptExecutor) driver).executeScript("window.confirm = function(msg) { }");

			}

		}
	}

	public void close() {

		if (this.driver != null) {
			LOGGER.info("Closing driver");
			this.driver.close();
		}
	}

	public Proxy getProxy() {
		return proxy;
	}

	public WebDriver getDriver() {
		return driver;
	}

}

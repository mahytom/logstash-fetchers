package eu.wajja.web.fetcher.controller;

import java.nio.file.Path;
import java.nio.file.Paths;

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

import eu.wajja.web.fetcher.model.Result;

public class WebDriverController {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverController.class);

	private String proxyUser;
	private String proxyPass;
	private String proxyHost;
	private Long proxyPort;
	private String chromeDriver;
	private Long timeout;

	public WebDriverController(String proxyUser, String proxyPass, String proxyHost, Long proxyPort, String chromeDriver, Long timeout) {

		this.proxyUser = proxyUser;
		this.proxyPass = proxyPass;
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
		this.chromeDriver = chromeDriver;
		this.timeout = timeout;
	}

	public Result getURL(String currentUrl) {

		Result result = new Result();
		result.setUrl(currentUrl);

		WebDriver webDriver = null;

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

			webDriver = new JBrowserDriver(settings.build());

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

			webDriver = new ChromeDriver(chromeOptions);

			// https://github.com/seleniumhq/selenium-google-code-issue-archive/issues/27
			((JavascriptExecutor) webDriver).executeScript("window.alert = function(msg) { }");
			((JavascriptExecutor) webDriver).executeScript("window.confirm = function(msg) { }");

		}

		try {

			webDriver.get(currentUrl);
			String content = webDriver.getPageSource();
			result.setCode(200);
			result.setContent(content.getBytes());

		} catch (Exception e) {
			LOGGER.error("Failed to retrieve js page {}", currentUrl);
		} finally {
			webDriver.close();
		}

		return result;
	}

}

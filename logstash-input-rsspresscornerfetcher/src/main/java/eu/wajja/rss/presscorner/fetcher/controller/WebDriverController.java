package eu.wajja.rss.presscorner.fetcher.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.wajja.rss.presscorner.fetcher.exception.WebDriverException;
import eu.wajja.rss.presscorner.fetcher.model.Result;

public class WebDriverController {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverController.class);

	public byte[] getURL(String url, String chromeDriver, String waitForCssSelector, Integer maxWaitForCssSelector) throws MalformedURLException, WebDriverException {

		WebDriver webDriver = null;

		if (StringUtils.isEmpty(chromeDriver)) {

			throw new WebDriverException("You need to specify a valid chrome driver. Either the path to the executable or a browserless/chrome instance");

		} else if (chromeDriver.startsWith("http")) {

			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.addArguments("--headless");
			chromeOptions.addArguments("--no-sandbox");

			webDriver = new RemoteWebDriver(new URL(chromeDriver), chromeOptions);

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

			webDriver.get(url);

			if (waitForCssSelector != null) {

				List<WebElement> webElement = webDriver.findElements(By.cssSelector(waitForCssSelector));
				int x = 0;

				while (webElement.isEmpty()) {

					Thread.sleep(1000);
					webElement = webDriver.findElements(By.cssSelector(waitForCssSelector));
					LOGGER.info("Waiting for css selector {} to appear on page {}", waitForCssSelector, url);

					if (x > maxWaitForCssSelector) {
						LOGGER.info("Could not find css selector {} on page {}", waitForCssSelector, url);
						break;
					}

					x++;

				}

			}

			String content = webDriver.getPageSource();

			if (content == null || content.isEmpty()) {
				LOGGER.error("Current url {} is empty or null", url);
			} else {
				return content.getBytes();
			}

		} catch (Exception e) {
			LOGGER.error("Failed to retrieve page {}", url);
		} finally {
			webDriver.close();
		}

		return null;
	}

}
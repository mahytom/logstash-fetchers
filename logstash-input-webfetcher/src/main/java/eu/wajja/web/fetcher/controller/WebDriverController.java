package eu.wajja.web.fetcher.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.json.JsonException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.wajja.web.fetcher.exception.WebDriverException;
import eu.wajja.web.fetcher.model.WebDriverResult;

public class WebDriverController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverController.class);

    public WebDriverResult getURL(String url, String chromeDriver, String userAgent, String waitForCssSelector, Integer maxWaitForCssSelector, boolean enableJsLinks) throws MalformedURLException, WebDriverException {

        WebDriverResult webDriverResult = new WebDriverResult();
        WebDriver webDriver = getWebDriver(chromeDriver, userAgent);

        Set<String> childUrls = new HashSet<>();
        Set<String> unparsedJavascriptChildUrls = new HashSet<>();

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

            /*
             * Extract the content
             */

            String content = webDriver.getPageSource();

            if (content == null || content.isEmpty()) {
                LOGGER.error("Current url {} is empty or null", url);
            } else {
                webDriverResult.setBytes(content.getBytes());
            }

            /*
             * Find all child urls
             */
            List<WebElement> webElements = webDriver.findElements(By.tagName("a"));

            childUrls = webElements.stream().filter(h -> h.getAttribute("href") != null).map(h -> h.getAttribute("href")).collect(Collectors.toSet());

            unparsedJavascriptChildUrls = webElements.stream()
                    .filter(h -> h.getAttribute("href") == null)
                    .map(WebElement::getText).collect(Collectors.toSet());

        } catch (Exception e) {
            LOGGER.error("Failed to retrieve page {}", url, e);
        } finally {

            try {
                webDriver.quit();
            } catch (JsonException e) {
                LOGGER.warn("Failed to close webDriver");
            }
        }

        if (enableJsLinks) {
            Set<String> newChildUrls = getJsLinks(url, chromeDriver, userAgent, unparsedJavascriptChildUrls);
            childUrls.addAll(newChildUrls);
        }

        webDriverResult.setUrls(childUrls);

        return webDriverResult;
    }

    private Set<String> getJsLinks(String url, String chromeDriver, String userAgent, Set<String> unparsedJavascriptChildUrls) throws WebDriverException, MalformedURLException {

        Set<String> childUrls = new HashSet<>();

        for (String childUrlText : unparsedJavascriptChildUrls) {

            WebDriver webDriverForChild = getWebDriver(chromeDriver, userAgent);

            try {

                webDriverForChild.get(url);
                List<WebElement> webElementsChild = webDriverForChild.findElements(By.tagName("a"));

                WebElement webElement = webElementsChild.stream()
                        .filter(h -> h.getAttribute("href") == null)
                        .filter(h -> h.isDisplayed() && h.isEnabled())
                        .filter(h -> h.getText().equals(childUrlText))
                        .findFirst().orElse(null);

                if (webElement != null) {

                    webElement.click();
                    String childUrl = webDriverForChild.getCurrentUrl();
                    LOGGER.info("Javascript child url detected {}", childUrl);

                    childUrls.add(childUrl);

                }

            } catch (Exception e) {
                LOGGER.error("Failed to retrieve child page {}", childUrlText);
            } finally {

                try {
                    webDriverForChild.quit();
                } catch (JsonException e) {
                    LOGGER.warn("Failed to close webDriverForChild");
                }
            }
        }

        return childUrls;
    }

    private WebDriver getWebDriver(String chromeDriver, String userAgent) throws WebDriverException, MalformedURLException {

        WebDriver webDriver = null;

        if (StringUtils.isEmpty(chromeDriver)) {

            throw new WebDriverException("You need to specify a valid chrome driver. Either the path to the executable or a browserless/chrome instance");

        } else if (chromeDriver.startsWith("http")) {

            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--headless");
            chromeOptions.addArguments("--no-sandbox");
            Optional.ofNullable(userAgent)
                    .filter(StringUtils::isNotBlank)
                    .ifPresent(ua -> chromeOptions.addArguments("user-agent=" + ua));

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
            Optional.ofNullable(userAgent)
                    .filter(StringUtils::isNotBlank)
                    .ifPresent(ua -> chromeOptions.addArguments("user-agent=" + ua));

            webDriver = new ChromeDriver(chromeOptions);

            // https://github.com/seleniumhq/selenium-google-code-issue-archive/issues/27
            ((JavascriptExecutor) webDriver).executeScript("window.alert = function(msg) { }");
            ((JavascriptExecutor) webDriver).executeScript("window.confirm = function(msg) { }");

        }

        return webDriver;
    }

}
package eu.wajja.web.fetcher.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.wajja.web.fetcher.controller.URLController;
import eu.wajja.web.fetcher.elasticsearch.ElasticSearchService;
import eu.wajja.web.fetcher.enums.Status;
import eu.wajja.web.fetcher.enums.SubStatus;
import eu.wajja.web.fetcher.model.Result;

public class RobotService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReindexService.class);

	private static final String ROBOTS = "robots.txt";
	private Map<String, Set<String>> disallowedLocations = new HashMap<>();
	private Map<String, Set<String>> allowedLocations = new HashMap<>();
	private Map<String, Set<String>> sitemapLocations = new HashMap<>();

	private URLController urlController;
	private ElasticSearchService elasticSearchService;
	private boolean readRobot;

	public RobotService(URLController urlController, ElasticSearchService elasticSearchService, boolean readRobot) {
		this.urlController = urlController;
		this.elasticSearchService = elasticSearchService;
		this.readRobot = readRobot;
	}

	public void checkRobot(String chromeThread, String url, String index, String jobId) {

		if (readRobot) {

			LOGGER.info("Reading Robot : {}, url : {}", jobId, url);

			Pattern p = Pattern.compile("(http).*(\\/\\/)[^\\/]{2,}(\\/)");
			Matcher m = p.matcher(url);

			if (m.find()) {
				String robotUrl = m.group(0) + ROBOTS;
				readRobot(index, url, robotUrl, chromeThread, jobId);

			} else {
				LOGGER.warn("Failed to find robot.txt url {}", url);
			}

			LOGGER.info("Finished Reading Robot : {}, url : {}", jobId, url);

		}
	}

	private void readRobot(String index, String initialUrl, String robotUrl, String chromeDriver, String jobId) {

		Result result = urlController.getURL(index, robotUrl, initialUrl, chromeDriver, false);

		if (result != null && result.getContent() != null) {

			try (Scanner scanner = new Scanner(IOUtils.toString(result.getContent(), StandardCharsets.UTF_8.name()))) {

				elasticSearchService.addNewUrl(result, jobId, index, Status.processed, SubStatus.excluded, "robot read");
				String userAgent = "*";

				while (scanner.hasNextLine()) {

					String line = scanner.nextLine().trim();

					if (!line.startsWith("#") && !line.isEmpty()) {

						if (line.startsWith("User-agent:")) {
							userAgent = line.replace("User-agent:", "").trim();

						} else if (line.startsWith("Disallow:")) {

							String perm = line.replace("Disallow:", "").trim();

							if (!disallowedLocations.containsKey(userAgent)) {
								disallowedLocations.put(userAgent, new HashSet<>());
							}

							disallowedLocations.get(userAgent).add(perm);

						} else if (line.startsWith("Allow:")) {

							String perm = line.replace("Allow:", "").trim();

							if (!allowedLocations.containsKey(userAgent)) {
								allowedLocations.put(userAgent, new HashSet<>());
							}

							allowedLocations.get(userAgent).add(perm);

						} else if (line.startsWith("Sitemap:")) {

							String perm = line.replace("Sitemap:", "").trim();

							if (!sitemapLocations.containsKey(userAgent)) {
								sitemapLocations.put(userAgent, new HashSet<>());
							}

							sitemapLocations.get(userAgent).add(perm);
						}
					}
				}

			} catch (Exception e) {
				LOGGER.error("Failed to parse robots.txt from url {}", robotUrl, e);
			}

		} else {
			LOGGER.warn("Failed to read robot.txt url, status {}", initialUrl);
		}
	}

	public boolean isAllowed(String urlString, String rootUrl, String index, String jobId, String crawlerUserAgent) throws IOException {

		if (readRobot) {

			Set<String> disallowedList = new HashSet<>();

			if (disallowedLocations.containsKey("*")) {
				disallowedList = disallowedLocations.get("*");
			}

			if (disallowedLocations.containsKey(crawlerUserAgent)) {
				disallowedList.addAll(disallowedLocations.get(crawlerUserAgent));
			}

			if (!disallowedList.isEmpty()) {

				String regex = (".*(" + String.join(")|(", disallowedList).replace("*", ".*").replace("/", "\\/") + ").*").trim();
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(urlString);

				if (m.find()) {
					LOGGER.info("URL {} is dissallowed", urlString);
					elasticSearchService.addNewUrl(urlString, rootUrl, jobId, index, Status.failed, SubStatus.excluded, "excluded by robot");
					return false;
				}
			}
		}

		return true;
	}

}

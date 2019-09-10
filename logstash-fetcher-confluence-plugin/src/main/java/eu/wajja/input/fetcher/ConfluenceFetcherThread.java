package eu.wajja.input.fetcher;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.wajja.input.fetcher.model.Result;
import eu.wajja.input.fetcher.model.Results;

public class ConfluenceFetcherThread implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfluenceFetcherThread.class);

	private static final ObjectMapper mapper = new ObjectMapper();

	private Consumer<Map<String, Object>> consumer;

	private String url;
	private String username;
	private String password;
	private String dataFolder;
	private Integer pageLimit;
	private List<String> spaces;
	private HttpClient httpClient;

	public ConfluenceFetcherThread(Consumer<Map<String, Object>> consumer, String url, String username, String password, String dataFolder, Integer pageLimit, List<String> spaces, HttpClient httpClient) {
		super();
		this.consumer = consumer;
		this.url = url;
		this.username = username;
		this.password = password;
		this.dataFolder = dataFolder;
		this.pageLimit = pageLimit;
		this.spaces = spaces;
		this.httpClient = httpClient;
	}

	@Override
	public void run() {

		try {
			indexConfluenceContent(url, 0, consumer);
		} catch (IOException | InterruptedException e) {
			LOGGER.error("Failed to fetch source {}", url, e);
		}
	}

	private void indexConfluenceContent(String confluenceRestUrl, Integer from, Consumer<Map<String, Object>> consumer) throws IOException, InterruptedException {

		StringBuilder stringBuilder = new StringBuilder(confluenceRestUrl).append("/rest/api/content/?limit=").append(pageLimit).append("&start=").append(from).append("&expand=version");

		if (spaces != null && !spaces.isEmpty()) {

			StringBuilder spacesBuilder = new StringBuilder();
			spaces.stream().forEach(s -> spacesBuilder.append(s).append(","));
			stringBuilder.append("&spaceKey=").append(spacesBuilder.toString().substring(0, spacesBuilder.length() - 1));
		}

		HttpGet get = getHttpGet(stringBuilder.toString());
		HttpResponse response = httpClient.execute(get);
		String output = IOUtils.toString(new InputStreamReader(response.getEntity().getContent()));

		Results results = mapper.reader().forType(Results.class).readValue(output);
		LOGGER.info("Results found : " + results.getResults().size());

		results.getResults().stream().forEach(result -> {

			try {

				String spaceKey = result.get_expandable().getSpace().substring(result.get_expandable().getSpace().lastIndexOf("/"));

				Path pathDirectory = Paths.get(dataFolder + "/spaces/" + spaceKey);
				Files.createDirectories(pathDirectory);

				Path pathFile = Paths.get(dataFolder + "/spaces/" + spaceKey + "/" + result.getId() + ".json");
				Boolean skipDocument = false;

				if (Files.exists(pathFile)) {

					String localJson = IOUtils.toString(Files.newInputStream(pathFile), "UTF-8");
					Result localResult = mapper.reader().forType(Result.class).readValue(localJson);
					String dateString = localResult.getVersion().getWhen();

					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
					Date oldDate = simpleDateFormat.parse(dateString);
					Date newDate = simpleDateFormat.parse(result.getVersion().getWhen());

					skipDocument = newDate.after(oldDate) || newDate.equals(oldDate);

				}

				if (!skipDocument) {

					downloadFile(confluenceRestUrl, result, pathFile);

					String localJson = IOUtils.toString(Files.newInputStream(pathFile), "UTF-8");
					Result localResult = mapper.reader().forType(Result.class).readValue(localJson);

					String content = Jsoup.clean(localResult.getBody().getView().getValue(), Whitelist.simpleText());

					Map<String, Object> metadata = new HashMap<>();
					metadata.put("reference", localResult.getId());
					metadata.put("content", content);
					metadata.put("url", confluenceRestUrl + "/pages/viewpage.action?pageId=" + localResult.getId());

					consumer.accept(metadata);

					LOGGER.info("Add Or Update -> " + result.getId() + " -> " + result.get_expandable().getSpace() + " -> " + result.getType() + " -> " + result.getTitle());

				} else {
					LOGGER.info("Skipping -> " + result.getId() + " -> " + result.get_expandable().getSpace() + " -> " + result.getType() + " -> " + result.getTitle());
				}

			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}

		});

		if (results.getResults().size() == pageLimit) {
			indexConfluenceContent(confluenceRestUrl, from + pageLimit, consumer);
		} else {
			LOGGER.info("Finished last page size : " + results.getResults().size());
		}

	}

	private void downloadFile(String confluenceRestUrl, Result result, Path pathFile) throws IOException, ClientProtocolException {

		StringBuilder stringBuilderContent = new StringBuilder(confluenceRestUrl).append("/rest/api/content/").append(result.getId()).append("?expand=body.view,version");
		HttpGet getHttp = getHttpGet(stringBuilderContent.toString());
		HttpResponse responseHttp = httpClient.execute(getHttp);
		String outputJson = IOUtils.toString(new InputStreamReader(responseHttp.getEntity().getContent()));

		Files.write(pathFile, outputJson.getBytes());
	}

	private HttpGet getHttpGet(String url) {

		HttpGet get = new HttpGet(url);

		String encodedSecurity = Base64.getEncoder().encodeToString(new StringBuilder(username).append(":").append(password).toString().getBytes());
		get.setHeader(HttpHeaders.AUTHORIZATION, new StringBuilder("Basic ").append(encodedSecurity).toString());
		get.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

		return get;
	}
}

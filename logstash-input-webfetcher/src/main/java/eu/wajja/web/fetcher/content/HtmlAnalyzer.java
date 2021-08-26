package eu.wajja.web.fetcher.content;

import static java.util.Objects.nonNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.wajja.web.fetcher.model.Result;

public class HtmlAnalyzer implements ContentAnalyzer {

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlAnalyzer.class);

    private Result result;
    private boolean readRobot;

    HtmlAnalyzer(Result result, boolean readRobot) {

        this.result = result;
        this.readRobot = readRobot;
    }

    @Override
    public boolean isExcluded() {

        if (readRobot && nonNull(result.getContent())) {
            try {
                Document doc = Jsoup.parse(IOUtils.toString(result.getContent(), StandardCharsets.UTF_8.name()));
                return !doc.select("meta[name=robots][content*=noindex]").isEmpty();
            } catch (IOException e) {
                LOGGER.error("Jsoup parse failed", e);
            }
        }
        return false;
    }

    @Override
    public String getExclusionReason() {

        return "There is a meta robots tag with noindex content.";
    }
}

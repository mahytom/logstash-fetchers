package eu.wajja.rss.presscorner.fetcher.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Hits {

    private List<Hit> hits;

    public List<Hit> getHits() {

        return hits;
    }

    public void setHits(List<Hit> hits) {

        this.hits = hits;
    }

}

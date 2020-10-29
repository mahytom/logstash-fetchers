package eu.wajja.dpo.fetcher.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultHits {

    private Hits hits;

    public Hits getHits() {

        return hits;
    }

    public void setHits(Hits hits) {

        this.hits = hits;
    }

}

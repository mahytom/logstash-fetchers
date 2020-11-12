package eu.wajja.rss.presscorner.fetcher.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Hit {

    private String _id;

    public String get_id() {

        return _id;
    }

    public void set_id(String _id) {

        this._id = _id;
    }

}

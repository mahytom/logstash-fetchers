package eu.wajja.web.fetcher.model;

import java.util.HashSet;
import java.util.Set;

public class WebDriverResult {

    private byte[] bytes;
    private Set<String> urls = new HashSet<>();

    public byte[] getBytes() {

        return bytes;
    }

    public void setBytes(byte[] bytes) {

        this.bytes = bytes;
    }

    public Set<String> getUrls() {

        return urls;
    }

    public void setUrls(Set<String> urls) {

        this.urls = urls;
    }

}

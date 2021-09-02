package eu.wajja.web.fetcher.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Result {

    private String url;
    private String rootUrl;
    private Set<String> redirectUrls = new HashSet<>();
    private Integer code;
    private String md5;
    private Integer length;
    private String eTag;
    private String status;
    private String subStatus;
    private String message;
    private byte[] content;
    private String contentType;
    private boolean isCached = false;
    private Set<String> childUrls = new HashSet<>();
    private Map<String, List<String>> headers = new HashMap<>();

    public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public Set<String> getChildUrls() {

        return childUrls;
    }

    public void setChildUrls(Set<String> childUrls) {

        this.childUrls = childUrls;
    }

    public boolean isCached() {

        return isCached;
    }

    public void setCached(boolean isCached) {

        this.isCached = isCached;
    }

    public Set<String> getRedirectUrls() {

        return redirectUrls;
    }

    public void setRedirectUrls(Set<String> redirectUrls) {

        this.redirectUrls = redirectUrls;
    }

    public String geteTag() {

        return eTag;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public String getSubStatus() {

        return subStatus;
    }

    public void setSubStatus(String subStatus) {

        this.subStatus = subStatus;
    }

    public void seteTag(String eTag) {

        this.eTag = eTag;
    }

    public String getContentType() {

        return contentType;
    }

    public void setContentType(String contentType) {

        this.contentType = contentType;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    public byte[] getContent() {

        return content;
    }

    public void setContent(byte[] content) {

        this.content = content;
    }

    public Map<String, List<String>> getHeaders() {

        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {

        this.headers = headers;
    }

    public Integer getCode() {

        return code;
    }

    public void setCode(Integer code) {

        this.code = code;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }

    public String getRootUrl() {

        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {

        this.rootUrl = rootUrl;
    }

    public Integer getLength() {

        return length;
    }

    public void setLength(Integer length) {

        this.length = length;
    }
}

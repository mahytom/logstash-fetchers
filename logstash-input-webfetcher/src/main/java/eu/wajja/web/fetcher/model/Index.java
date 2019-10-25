package eu.wajja.web.fetcher.model;

import java.io.Serializable;

/**
 * Stores the status of the webpage
 * 
 * @author mahytom
 *
 */
public class Index implements Serializable {

	private static final long serialVersionUID = 2478966715943538693L;

	private String url;
	private String uuid;
	private Long epochSecond;

	public Index() {
	}

	public Index(String url, String uuid, Long epochSecond) {
		this.url = url;
		this.uuid = uuid;
		this.epochSecond = epochSecond;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Long getEpochSecond() {
		return epochSecond;
	}

	public void setEpochSecond(Long epochSecond) {
		this.epochSecond = epochSecond;
	}

}

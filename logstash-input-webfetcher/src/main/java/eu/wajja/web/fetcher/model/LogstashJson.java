package eu.wajja.web.fetcher.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


/**
 * JSON object that will be sent to logstash
 * 
 * @author mahytom
 *
 */
@JsonInclude(Include.NON_NULL)
public class LogstashJson implements Serializable {

	private static final long serialVersionUID = 3432390880016557970L;
	private Long thread;
	private String job;
	private String url;
	private String method;
	private String environment;

	public Long getThread() {
		return thread;
	}

	public void setThread(Long thread) {
		this.thread = thread;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

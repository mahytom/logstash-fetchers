package eu.wajja.input.fetcher;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.elastic.logstash.api.Configuration;
import co.elastic.logstash.api.Context;
import co.elastic.logstash.api.Input;
import co.elastic.logstash.api.LogstashPlugin;
import co.elastic.logstash.api.PluginConfigSpec;

@LogstashPlugin(name = "sharepointfetcher")
public class SharepointFetcher implements Input {

	private static final Logger LOGGER = LoggerFactory.getLogger(SharepointFetcher.class);

	private String id;

	private final CountDownLatch done = new CountDownLatch(1);
	private volatile boolean stopped;

	/**
	 * Mandatory constructor
	 * 
	 * @param id
	 * @param config
	 * @param context
	 */
	public SharepointFetcher(String id, Configuration config, Context context) {

		this.id = id;

	}

	@Override
	public void start(Consumer<Map<String, Object>> consumer) {

		stopped = true;
		done.countDown();
	}

	@Override
	public void stop() {
		stopped = true;
	}

	@Override
	public void awaitStop() throws InterruptedException {
		done.await();
	}

	/**
	 * Returs a list of all configuration
	 */
	@Override
	public Collection<PluginConfigSpec<?>> configSchema() {
		return null;
	}

	@Override
	public String getId() {
		return this.id;
	}
}

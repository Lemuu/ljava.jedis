package info.lemuu.jedis.thread;

import info.lemuu.jedis.connection.RedisConnection;
import info.lemuu.jedis.credentials.RedisCredentials;
import info.lemuu.jedis.fallback.RedisFallBack;

import java.util.Arrays;

public abstract class RedisThread extends RedisConnection implements IRedisThread {

	protected StringBuilder channels;
	private final RedisFallBack redisFallBack;
	
	public RedisThread(RedisCredentials redisCredentials) {
		super(redisCredentials);
		this.channels = new StringBuilder();
		this.redisFallBack = new RedisFallBack();
	}

	@Override
	public void subscribeChannel(String channel) {
		this.channels.append(channel).append("@");
	}

	@Override
	public void subscribeChannels(String... channels) {
		Arrays.asList(channels).forEach(channel -> this.channels.append(channel).append("@"));
	}
	
	public String[] getChannels() {
		return this.channels.toString().split("@");
	}
	
	public boolean hasChannel(String channel) {
		return this.channels.toString().contains(channel);
	}
	
	public RedisFallBack getRedisFallBack() {
		return redisFallBack;
	}
	
	@Override
	public void closeConnection() {
		this.jedisPool.close();
	}

}
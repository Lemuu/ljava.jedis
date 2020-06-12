package info.lemuu.jedis.thread.runnable;

import info.lemuu.jedis.Redis;
import info.lemuu.jedis.handler.JedisPubSubHandler;

public class RunnableJedisPubSub implements Runnable {
	
	private final Redis redis;
	
	public RunnableJedisPubSub(Redis redis) {
		this.redis = redis;
	}
	
	@Override
	public void run() {
		if (this.redis.getRedisFallBack().canReconnect()) {
			this.redis.reconnectPubSub();
			return;
		}
		
		this.redis.getJedisPool().getResource()
			.subscribe(new JedisPubSubHandler(), this.redis.getChannels());
	}

}
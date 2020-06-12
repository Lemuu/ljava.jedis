package info.lemuu.jedis.handler;

import redis.clients.jedis.JedisPubSub;

public class JedisPubSubHandler extends JedisPubSub {
	
	@Override
	public void onMessage(String channel, String message) {
		RedisListener.callEvent(channel, message);
	}

}
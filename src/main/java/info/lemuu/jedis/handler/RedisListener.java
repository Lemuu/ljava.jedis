package info.lemuu.jedis.handler;

import java.util.List;
import java.util.LinkedList;
import info.lemuu.jedis.thread.RedisThread;
import info.lemuu.jedis.credentials.RedisCredentials;
import info.lemuu.jedis.handler.listener.JedisListener;
import info.lemuu.jedis.handler.reflection.JedisInvoke;

public abstract class RedisListener extends RedisThread {
	
	private final static List<JedisListener> listeners = new LinkedList<JedisListener>();
	
	public RedisListener(RedisCredentials redisCredentials) {
		super(redisCredentials);
	}

	public void registerListener(JedisListener jedisListener) {
		RedisListener.listeners.add(jedisListener);
	}
	
	public void unregisterListener(JedisListener jedisListener) {
		RedisListener.listeners.remove(jedisListener);
	}

	protected static void callEvent(String channel, String message) {
		new Thread(()->{
			RedisListener.listeners.forEach(jedisListener -> {
				new JedisInvoke(jedisListener, channel, message).invoke();
			});
		}).start();
	}

}
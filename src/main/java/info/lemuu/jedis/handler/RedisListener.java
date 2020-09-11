package info.lemuu.jedis.handler;

import info.lemuu.jedis.credentials.RedisCredentials;
import info.lemuu.jedis.handler.listener.JedisListener;
import info.lemuu.jedis.handler.reflection.JedisInvoke;
import info.lemuu.jedis.thread.RedisThread;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
		new Thread(()-> {
			Iterator<JedisListener> iterator = RedisListener.listeners.iterator();
			synchronized (iterator) {
				while (iterator.hasNext()) {
					new JedisInvoke(iterator.next(), channel, message).invoke();
				}
			}
		}).start();
	}

}
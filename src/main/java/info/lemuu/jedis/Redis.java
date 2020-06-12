package info.lemuu.jedis;

import redis.clients.jedis.Jedis;
import info.lemuu.jedis.handler.RedisListener;
import info.lemuu.jedis.credentials.RedisCredentials;
import info.lemuu.jedis.thread.runnable.RunnableJedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class Redis extends RedisListener implements Cloneable {

	public Redis(RedisCredentials redisCredentials) {
		super(redisCredentials);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void sendMessage(String channel, String message) {
		Jedis jedis = this.jedisPool.getResource();
		try {
			jedis.publish(channel, message);
		} catch (JedisConnectionException ex) {
			ex.printStackTrace();
			this.jedisPool.returnBrokenResource(jedis);
		} finally {
			this.jedisPool.returnResource(jedis);
		}
	}
	
	@Override
	public void init() {
		this.thread = new Thread(new RunnableJedisPubSub(this), "Thread Redis channels");
		this.thread.start();
	}

	@SuppressWarnings("deprecation")
	public void reconnectPubSub() {
		this.getRedisFallBack().updateMillis();
		this.thread.stop();
		this.reconnect();
		this.init();
	}
	
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException ex) {
			System.out.println("Cloning not allowed. Cloning force activated! " + this.getClass());
			return new Redis(super.getRedisCredentials());
		}
	}
	
}
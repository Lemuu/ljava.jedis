package info.lemuu.jedis;

import info.lemuu.jedis.credentials.RedisCredentials;
import info.lemuu.jedis.handler.JedisPubSubHandler;
import info.lemuu.jedis.handler.RedisListener;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.ArrayList;
import java.util.List;

public class Redis extends RedisListener implements Cloneable {

	private final static List<Redis> redis = new ArrayList<Redis>();

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
		redis.add(this);
		new Thread(()-> {
			this.jedisPool.getResource().subscribe(new JedisPubSubHandler(), this.getChannels());
		}).start();
	}

	public void reconnectPubSub() {
		this.getRedisFallBack().updateMillis();
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

	public static List<Redis> getRedis() {
		return redis;
	}
}
package info.lemuu.jedis.connection;

import redis.clients.jedis.JedisPool;

public interface IRedisConnection {
	
	void openConnection();
	
	JedisPool getJedisPool();
	
	void closeConnection();
	
	void reconnect();
	
	boolean isClosed();
	
	boolean isConnected();
	
	void sendMessage(String channel, String message);

}
package info.lemuu.jedis.connection;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import info.lemuu.jedis.credentials.RedisCredentials;

public abstract class RedisConnection implements IRedisConnection {
	
	private final RedisCredentials redisCredentials;
	protected JedisPool jedisPool;
	
	public RedisConnection(RedisCredentials redisCredentials) {
		this.redisCredentials = redisCredentials;
		
		this.openConnection();
	}
	
	@Override
	public void openConnection() {
		this.jedisPool = new JedisPool(this.config(),
				this.redisCredentials.getHost(),
				this.redisCredentials.getPort(),
				0,
				this.redisCredentials.getPassword());
	}
	
	@Override
	public JedisPool getJedisPool() {
		return jedisPool;
	}
	
	@Override
	public boolean isClosed() {
		return this.jedisPool.isClosed();
	}
	@Override
	public boolean isConnected() {
		return !this.isClosed() || this.jedisPool == null;
	}

	@Override
	public void reconnect() {
		this.jedisPool.close();
		this.jedisPool = null;
		this.openConnection();
	}
	
	// config defined for the developer
	private JedisPoolConfig config() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(70);
		jedisPoolConfig.setMaxIdle(100);
		jedisPoolConfig.setMinIdle(50);
		jedisPoolConfig.setMaxWaitMillis(10000);
		jedisPoolConfig.setTestOnBorrow(true);
		jedisPoolConfig.setTestOnReturn(true);
		jedisPoolConfig.setTimeBetweenEvictionRunsMillis(30000);
		jedisPoolConfig.setTestWhileIdle(true);
		jedisPoolConfig.setNumTestsPerEvictionRun(50);
		
		return jedisPoolConfig;
	}
	
	public RedisCredentials getRedisCredentials() {
		return redisCredentials;
	}
	
}
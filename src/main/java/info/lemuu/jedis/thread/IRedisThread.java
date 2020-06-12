package info.lemuu.jedis.thread;

public interface IRedisThread {
	
	void init();
	
	void subscribeChannel(String channel);
	
	void subscribeChannels(String... channels);
	
}
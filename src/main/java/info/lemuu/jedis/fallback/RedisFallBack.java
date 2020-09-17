package info.lemuu.jedis.fallback;

public class RedisFallBack implements Runnable {
	
	// defined for the developer
	public static final int SECONDS_CONNECTIONS_NO_RESPONSE = 15;
	
	private long millis;

	public void updateMillis() {
		this.millis = System.currentTimeMillis();
	}
	
	public int getMillisSecond() {
		return (int) (RedisFallBack.SECONDS_CONNECTIONS_NO_RESPONSE - (System.currentTimeMillis() - this.millis) / 1000L);
	}
	
	public boolean canReconnect() {
		return this.getMillisSecond() <= 0;
	}
	
	@Override
	public void run() {
		this.updateMillis();
	}

}
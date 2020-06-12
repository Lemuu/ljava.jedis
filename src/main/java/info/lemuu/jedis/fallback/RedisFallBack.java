package info.lemuu.jedis.fallback;

public class RedisFallBack {
	
	// defined for the developer
	public final int SECONDS_CONNECTIONS_NO_RESPONSE = 15;
	
	private long millis;
	private final Thread thread;
	
	public RedisFallBack() {
		this.thread = new Thread(new RedisFallBackRunnable(this));
		this.thread.start();
	}
	
	public long getMillis() {
		return millis;
	}
	public void updateMillis() {
		this.millis = System.currentTimeMillis();
	}
	
	public int getMillisSecond() {
		return (int) (this.SECONDS_CONNECTIONS_NO_RESPONSE - (System.currentTimeMillis() - this.millis) / 1000l);
	}
	
	public boolean canReconnect() {
		return this.getMillisSecond() <= 0;
	}
	
	@SuppressWarnings("deprecation")
	public void stop() {
		this.thread.stop();
	}

}
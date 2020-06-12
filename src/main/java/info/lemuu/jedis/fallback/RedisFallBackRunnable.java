package info.lemuu.jedis.fallback;

import java.util.concurrent.TimeUnit;

public class RedisFallBackRunnable implements Runnable {
	
	private final RedisFallBack redisFallBack;

	public RedisFallBackRunnable(RedisFallBack redisFallBack) {
		this.redisFallBack = redisFallBack;
	}

	@Override
	public void run() {
		try {
			while (true) {
				this.redisFallBack.updateMillis();
				
				TimeUnit.SECONDS.sleep(this.redisFallBack.SECONDS_CONNECTIONS_NO_RESPONSE);
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
	
}
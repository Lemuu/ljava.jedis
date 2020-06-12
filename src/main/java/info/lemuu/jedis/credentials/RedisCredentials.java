package info.lemuu.jedis.credentials;

public class RedisCredentials {
	
	private final String host, password;
	private final int port;
	
	public RedisCredentials(String host, int port, String password) {
		this.host = host;
		this.port = port;
		this.password = password;
	}

	public String getHost() {
		return this.host;
	}
	public int getPort() {
		return this.port;
	}
	public String getPassword() {
		return this.password;
	}

}
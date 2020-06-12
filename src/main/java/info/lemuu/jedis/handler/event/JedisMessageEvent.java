package info.lemuu.jedis.handler.event;

import org.json.simple.JSONValue;
import org.json.simple.JSONObject;

public class JedisMessageEvent {
	
	private final String channel, message;

	public JedisMessageEvent(String channel, String message) {
		this.channel = channel;
		this.message = message;
	}

	public String getChannel() {
		return channel;
	}

	public String getMessage() {
		return message;
	}
	
	public JSONObject getJson() {
		return (JSONObject) JSONValue.parse(message);
	}
	
}
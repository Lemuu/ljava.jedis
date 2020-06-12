package info.lemuu.jedis.handler.reflection;

import java.util.List;
import java.util.Arrays;
import java.lang.reflect.Method;
import java.util.stream.Collectors;
import info.lemuu.jedis.handler.Channel;
import java.lang.reflect.InvocationTargetException;
import info.lemuu.jedis.handler.listener.JedisListener;
import info.lemuu.jedis.handler.event.JedisMessageEvent;

public class JedisInvoke {
	
	private String channel;
	private String message;
	private JedisListener jedisListener;
	
	public JedisInvoke(JedisListener jedisListener, String channel, String message) {
		this.channel = channel;
		this.message = message;
		this.jedisListener = jedisListener;
	}
	
	private List<Method> collectMethods() {
		return Arrays.stream(jedisListener.getClass().getDeclaredMethods())
				.filter(method -> 
						method.getAnnotation(Channel.class) != null
						&& method.getAnnotation(Channel.class).name().equalsIgnoreCase(this.channel))
				.collect(Collectors.toList());
	}

	public void invoke() {
		this.collectMethods().forEach(method -> {
			try {
				method.setAccessible(true);
				method.invoke(jedisListener, new JedisMessageEvent(this.channel, this.message));
			} catch (IllegalAccessException | IllegalArgumentException ex) {
				ex.printStackTrace();
			} catch (InvocationTargetException ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex.getTargetException());
			}
		});
	}

}
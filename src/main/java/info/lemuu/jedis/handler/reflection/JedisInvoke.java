package info.lemuu.jedis.handler.reflection;

import info.lemuu.jedis.handler.Channel;
import info.lemuu.jedis.handler.event.JedisMessageEvent;
import info.lemuu.jedis.handler.listener.JedisListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JedisInvoke {
	
	private final String channel;
	private final String message;
	private final JedisListener jedisListener;

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
		List<Method> methods = this.collectMethods();
		if (methods == null) return;
		methods.forEach(method -> {
			try {
				method.setAccessible(true);
				if (jedisListener != null) {
					method.invoke(jedisListener, new JedisMessageEvent(this.channel, this.message));
				}
			} catch (IllegalAccessException | IllegalArgumentException ex) {
				ex.printStackTrace();
			} catch (InvocationTargetException ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex.getTargetException());
			}
		});
	}

}
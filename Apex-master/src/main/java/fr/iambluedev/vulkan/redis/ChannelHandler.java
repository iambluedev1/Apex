package fr.iambluedev.vulkan.redis;

import redis.clients.jedis.JedisPubSub;

public class ChannelHandler extends JedisPubSub {
	
	@Override
	public void onMessage(String channel, String message) {
		
	}
}

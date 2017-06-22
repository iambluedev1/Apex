package fr.iambluedev.vulkan.redis;

import fr.iambluedev.spartan.redis.SpartanPublisher;
import fr.iambluedev.spartan.utils.Callback;
import redis.clients.jedis.Jedis;

public class RedisPublisher extends SpartanPublisher
{
	public RedisPublisher(){}

	@Override
	public RedisPublisher send(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				RedisPublisher.this.getRedisSubscriber().getRedis().get(new Callback<Jedis>() {
					@Override
					public void call(Jedis jedis) {
						jedis.publish(RedisPublisher.this.getChannel(), RedisPublisher.this.getJson());
					}
				});
			}
		}).start();
		return this;
	}
	
	@Override
	public String getJson(){
		return "{\"id\": \"" + RedisPublisher.this.getId().toString() + "\",\"message\": \"" + RedisPublisher.this.getChannel() + "\"}";
	}
}
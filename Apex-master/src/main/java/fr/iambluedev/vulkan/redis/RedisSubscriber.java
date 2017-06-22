package fr.iambluedev.vulkan.redis;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import fr.iambluedev.spartan.redis.SpartanPublisher;
import fr.iambluedev.spartan.redis.SpartanRedis;
import fr.iambluedev.spartan.redis.SpartanSubscriber;
import fr.iambluedev.spartan.utils.Callback;
import redis.clients.jedis.Jedis;

public class RedisSubscriber extends SpartanSubscriber{

	private String response;
	private int timeout;
	
	public RedisSubscriber(SpartanPublisher publisher, SpartanRedis redis) {
		super(publisher, redis);
		this.response = "";
		this.timeout = 10000;
	}

	@Override
	public String[] call() throws Exception {
		while (this.response == "") {
			Thread.sleep(1);
			if(this.timeout == 0) return null;
			this.timeout--;
		}
		return new String[] { this.response, 10000 - this.timeout + "" };
	}

	@Override
	public void onMessage(String channel, String message) {
		if(!message.equals(this.getRedisPublisher().getJson()))
			if(message.contains(this.getRedisPublisher().getId().toString())) 
				this.response = message;
	}
	
	@Override
	public void get(Callback<String[]> callback) throws InterruptedException, ExecutionException {
		new Thread(new Runnable(){
			@Override
			public void run() {
				RedisSubscriber.this.getRedis().get(new Callback<Jedis>() {
					@Override
					public void call(Jedis jedis) {
						jedis.subscribe(RedisSubscriber.this, RedisSubscriber.this.getRedisPublisher().getChannel());
					}
				});
			}
		}).start();
		
		ExecutorService threadpool = Executors.newFixedThreadPool(1);
		Future<String[]> future = threadpool.submit(this);
		while (!future.isDone()) {}
		callback.call(future.get());
		threadpool.shutdown();
		future.cancel(true);
	}

	
}

package fr.iambluedev.spartan.redis;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import fr.iambluedev.spartan.utils.Callback;
import redis.clients.jedis.JedisPubSub;

public abstract class SpartanSubscriber extends JedisPubSub implements Callable<String[]>{

	private SpartanPublisher redisPublisher;
	private SpartanRedis redis;
	
	public SpartanSubscriber(SpartanPublisher publisher, SpartanRedis redis){
		this.redis = redis;
		this.redisPublisher = publisher.setRedisSubscriber(this).send();
	}
	
	public SpartanPublisher getRedisPublisher() {
		return this.redisPublisher;
	}

	public void setRedisPublisher(SpartanPublisher redisPublisher) {
		this.redisPublisher = redisPublisher;
	}
	
	public SpartanRedis getRedis() {
		return this.redis;
	}
	
	public abstract void get(Callback<String[]> callback) throws InterruptedException, ExecutionException;
	
}

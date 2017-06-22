package fr.iambluedev.spartan.redis;

import java.util.UUID;

public abstract class SpartanPublisher {

	private String channel, message;
	private UUID id;
	private SpartanSubscriber redisSubscriber;
	
	public SpartanPublisher(){
		this.id = UUID.randomUUID();
	}

	public String getChannel() {
		return this.channel;
	}

	public SpartanPublisher setChannel(String channel) {
		this.channel = channel;
		return this;
	}

	public String getMessage() {
		return this.message;
	}

	public SpartanPublisher setMessage(String message) {
		this.message = message;
		return this;
	}
	
	public UUID getId() {
		return this.id;
	}

	public SpartanSubscriber getRedisSubscriber() {
		return this.redisSubscriber;
	}

	public SpartanPublisher setRedisSubscriber(SpartanSubscriber redisSubscriber) {
		this.redisSubscriber = redisSubscriber;
		return this;
	}
	
	public abstract SpartanPublisher send();
	
	public abstract String getJson();
	
}

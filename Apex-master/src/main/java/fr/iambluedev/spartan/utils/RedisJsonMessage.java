package fr.iambluedev.spartan.utils;

import fr.iambluedev.spartan.api.gson.JSONObject;

public class RedisJsonMessage {

	private JSONObject jsonObj;
	
	public RedisJsonMessage(){
		this.jsonObj = new JSONObject();
	}
	
	@SuppressWarnings("unchecked")
	public RedisJsonMessage setCmd(String cmd){
		this.jsonObj.put("cmd", cmd);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public RedisJsonMessage setContent(String content){
		this.jsonObj.put("content", content);
		return this;
	}
	
	public String get(){
		return this.jsonObj.toJSONString();
	}
}

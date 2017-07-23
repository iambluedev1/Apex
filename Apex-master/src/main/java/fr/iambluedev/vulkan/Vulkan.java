package fr.iambluedev.vulkan;

import java.util.ArrayList;
import java.util.List;

import de.jackwhite20.apex.Apex;
import de.jackwhite20.apex.util.BackendInfo;
import fr.iambluedev.spartan.api.gson.JSONArray;
import fr.iambluedev.spartan.api.gson.JSONObject;
import fr.iambluedev.spartan.utils.Callback;
import fr.iambluedev.vulkan.backend.DefaultMcBackend;
import fr.iambluedev.vulkan.backend.DefaultWebBackend;
import fr.iambluedev.vulkan.config.ApexConfig;
import fr.iambluedev.vulkan.config.WhiteListConfig;
import fr.iambluedev.vulkan.redis.ChannelHandler;
import fr.iambluedev.vulkan.redis.Redis;
import fr.iambluedev.vulkan.state.ListeningState;
import fr.iambluedev.vulkan.state.WhitelistState;
import redis.clients.jedis.Jedis;

public class Vulkan {

	private static Vulkan instance;
	
	private ListeningState listeningState;
	private WhitelistState whitelistState;
	
	private WhiteListConfig whiteListConfig;
	private ApexConfig apexConfig;
	
	private List<String> whitelistedIp;
	
	private Redis redis;
	
	private BackendInfo mcBackend;
	private BackendInfo webBackend;
	
	public Vulkan(){
		instance = this;
		
		this.whiteListConfig = new WhiteListConfig();
		this.apexConfig = new ApexConfig();
		
		this.listeningState = ListeningState.OPEN;
		this.whitelistState = WhitelistState.OFF;
		
		this.whitelistedIp = new ArrayList<String>();
		JSONObject jsonObj = (JSONObject) this.whiteListConfig.getJsonObject();	
		JSONArray jsonArr = (JSONArray) jsonObj.get("whitelist");
		for(Object obj : jsonArr){
			this.whitelistedIp.add((String) obj);
			Apex.getLogger().debug("Added " + obj + " to the whitelist");
		}
		
		jsonObj = (JSONObject) this.apexConfig.getJsonObject().get("redis");
		String redisHost = (String) jsonObj.get("host");
		Integer redisPort = Integer.valueOf(jsonObj.get("port") + "");
		@SuppressWarnings("unused")
		String redisPassword = (String) jsonObj.get("password");
		
		Apex.getLogger().info("Connection to Redis !");
		this.redis = new Redis(redisHost, redisPort);
		
		jsonObj = (JSONObject) this.apexConfig.getJsonObject().get("defaults");
		
		JSONObject defaultBack = (JSONObject) jsonObj.get("mc");
		this.mcBackend = new DefaultMcBackend((String) defaultBack.get("ip"), Integer.valueOf(defaultBack.get("port") + ""));
		Apex.getLogger().debug("Default MC Backend set to : " + this.mcBackend.getHost() + ":" + this.mcBackend.getPort());
		
		defaultBack = (JSONObject) jsonObj.get("web");
		this.webBackend = new DefaultWebBackend((String) defaultBack.get("ip"), Integer.valueOf(defaultBack.get("port") + ""));
		Apex.getLogger().debug("Default WEB Backend set to : " + this.webBackend.getHost() + ":" + this.webBackend.getPort());
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				Vulkan.this.getRedis().get(new Callback<Jedis>() {
					@Override
					public void call(Jedis jedis) {
						jedis.subscribe(new ChannelHandler(), "apex", "node");
					}
				});
			}
		}).start();
	}

	public ListeningState getListeningState() {
		return this.listeningState;
	}

	public void setListeningState(ListeningState listeningState) {
		this.listeningState = listeningState;
	}

	public static Vulkan getInstance() {
		return instance;
	}

	public WhiteListConfig getWhiteListConfig() {
		return this.whiteListConfig;
	}

	public ApexConfig getApexConfig() {
		return this.apexConfig;
	}

	public List<String> getWhitelistedIp() {
		return this.whitelistedIp;
	}

	public WhitelistState getWhitelistState() {
		return this.whitelistState;
	}

	public void setWhitelistState(WhitelistState whitelistState) {
		this.whitelistState = whitelistState;
	}
	
	public void addIp(String ip){
		if(!this.whitelistedIp.contains(ip)){
			this.whitelistedIp.add(ip);
			this.whiteListConfig.update(this.whitelistedIp);
		}
	}
	
	public void removeIp(String ip){
		if(this.whitelistedIp.contains(ip)){
			this.whitelistedIp.remove(ip);
			this.whiteListConfig.update(this.whitelistedIp);
		}
	}

	public Redis getRedis() {
		return this.redis;
	}

	public BackendInfo getMcBackend() {
		return this.mcBackend;
	}

	public BackendInfo getWebBackend() {
		return this.webBackend;
	}
}

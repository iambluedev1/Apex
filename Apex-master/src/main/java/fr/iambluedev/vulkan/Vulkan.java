package fr.iambluedev.vulkan;

import java.util.ArrayList;
import java.util.List;

import de.jackwhite20.apex.Apex;
import fr.iambluedev.spartan.api.gson.JSONArray;
import fr.iambluedev.spartan.api.gson.JSONObject;
import fr.iambluedev.vulkan.config.ApexConfig;
import fr.iambluedev.vulkan.config.WhiteListConfig;
import fr.iambluedev.vulkan.redis.Redis;
import fr.iambluedev.vulkan.state.ListeningState;
import fr.iambluedev.vulkan.state.WhitelistState;

public class Vulkan {

	private static Vulkan instance;
	
	private ListeningState listeningState;
	private WhitelistState whitelistState;
	
	private WhiteListConfig whiteListConfig;
	private ApexConfig apexConfig;
	
	private List<String> whitelistedIp;
	
	private Redis redis;
	
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
}

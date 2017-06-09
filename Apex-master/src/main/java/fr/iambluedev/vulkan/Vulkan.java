package fr.iambluedev.vulkan;

import java.util.ArrayList;
import java.util.List;

import de.jackwhite20.apex.Apex;
import fr.iambluedev.spartan.api.gson.JSONArray;
import fr.iambluedev.spartan.api.gson.JSONObject;
import fr.iambluedev.vulkan.config.ApexConfig;
import fr.iambluedev.vulkan.config.WhiteListConfig;
import fr.iambluedev.vulkan.state.ListeningState;

public class Vulkan {

	private static Vulkan instance;
	private ListeningState listeningState;
	private WhiteListConfig whiteListConfig;
	private ApexConfig apexConfig;
	
	private List<String> whitelistedIp;
	
	public Vulkan(){
		instance = this;
		this.listeningState = ListeningState.OPEN;
		this.whiteListConfig = new WhiteListConfig();
		this.apexConfig = new ApexConfig();
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
}

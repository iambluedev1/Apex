package fr.iambluedev.vulkan.config;

import java.util.List;

import fr.iambluedev.spartan.api.config.SpartanConfig;
import fr.iambluedev.spartan.api.gson.JSONArray;

public class WhiteListConfig extends SpartanConfig{

	public WhiteListConfig() {
		super("whitelist");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setupConfig() {
		JSONArray whitelistObj = new JSONArray();
		whitelistObj.add("127.0.0.1");
		this.getJsonObject().put("whitelist", whitelistObj);
		this.save();
	}
	
	@SuppressWarnings("unchecked")
	public void update(List<String> ips){
		this.getJsonObject().clear();
		JSONArray whitelistObj = new JSONArray();
		for(String ip : ips){
			whitelistObj.add(ip);
		}
		this.getJsonObject().put("whitelist", whitelistObj);
		this.save();
	}

}

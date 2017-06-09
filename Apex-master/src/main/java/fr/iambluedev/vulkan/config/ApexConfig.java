package fr.iambluedev.vulkan.config;

import fr.iambluedev.spartan.api.config.SpartanConfig;
import fr.iambluedev.spartan.api.gson.JSONObject;

public class ApexConfig extends SpartanConfig{

	public ApexConfig() {
		super("apex");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setupConfig() {
		JSONObject generalObj = new JSONObject();
		generalObj.put("mode", "tcp");
		generalObj.put("debug", false);
		generalObj.put("ip", "0.0.0.0");
		generalObj.put("port", 80);
		generalObj.put("boss", 1);
		generalObj.put("backlog", 100);
		generalObj.put("worker", 4);
		generalObj.put("timeout", 30);
		generalObj.put("balance", "RANDOM");
		generalObj.put("probe", 5000);
		generalObj.put("stats", false);
		this.getJsonObject().put("general", generalObj);
		
		JSONObject restObj = new JSONObject();
		restObj.put("ip", "localhost");
		restObj.put("port", 6000);
		this.getJsonObject().put("rest", restObj);
		
		JSONObject backendObj = new JSONObject();
		
		JSONObject backend1Obj = new JSONObject();
		backend1Obj.put("ip", "164.132.48.233");
		backend1Obj.put("port", 80);
		backendObj.put("web-01", backend1Obj);
		
		this.getJsonObject().put("backend", backendObj);
		this.save();
	}
}

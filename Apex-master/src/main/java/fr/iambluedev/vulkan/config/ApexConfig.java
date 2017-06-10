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
		generalObj.put("debug", false);
		generalObj.put("stats", false);
		generalObj.put("boss", 1);
		generalObj.put("backlog", 100);
		generalObj.put("worker", 4);
		generalObj.put("probe", 5000);
		
		// CONVERT TO FRONTEND
		generalObj.put("mode", "tcp");
		generalObj.put("ip", "0.0.0.0");
		generalObj.put("port", 80);
		generalObj.put("timeout", 30);
		generalObj.put("balance", "RANDOM");
		//---
		
		this.getJsonObject().put("general", generalObj);
		
		JSONObject restObj = new JSONObject();
		restObj.put("ip", "localhost");
		restObj.put("port", 6000);
		this.getJsonObject().put("rest", restObj);
		
		JSONObject frontendObj = new JSONObject();
		
		JSONObject frontend1Obj = new JSONObject();
		frontend1Obj.put("ip", "0.0.0.0");
		frontend1Obj.put("port", 80);
		frontend1Obj.put("balance", "RANDOM");
		frontend1Obj.put("timeout", 30);
		frontend1Obj.put("mode", "tcp");
		frontendObj.put("web", frontend1Obj);
		
		JSONObject frontend2Obj = new JSONObject();
		frontend2Obj.put("ip", "0.0.0.0");
		frontend2Obj.put("port", 25565);
		frontend2Obj.put("balance", "RANDOM");
		frontend2Obj.put("timeout", 30);
		frontend2Obj.put("mode", "tcp");
		frontendObj.put("mc", frontend2Obj);
		
		this.getJsonObject().put("frontend", frontendObj);
		
		JSONObject backendObj = new JSONObject();
		
		JSONObject backend1Obj = new JSONObject();
		backend1Obj.put("ip", "164.132.48.233");
		backend1Obj.put("port", 80);
		backend1Obj.put("frontend", "web");
		backendObj.put("web-01", backend1Obj);
		
		this.getJsonObject().put("backend", backendObj);
		this.save();
	}
}

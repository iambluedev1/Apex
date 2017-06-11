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
		generalObj.put("debug", true);
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
		
		JSONObject backend2Obj = new JSONObject();
		backend2Obj.put("ip", "164.132.48.233");
		backend2Obj.put("port", 25565);
		backend2Obj.put("frontend", "mc");
		backendObj.put("mc-01", backend2Obj);
		
		JSONObject backend3Obj = new JSONObject();
		backend3Obj.put("ip", "164.132.48.233");
		backend3Obj.put("port", 25566);
		backend3Obj.put("frontend", "mc");
		backendObj.put("mc-02", backend3Obj);
		
		JSONObject backend4Obj = new JSONObject();
		backend4Obj.put("ip", "164.132.48.233");
		backend4Obj.put("port", 82);
		backend4Obj.put("frontend", "web");
		backendObj.put("web-02", backend4Obj);
		
		this.getJsonObject().put("backend", backendObj);
		this.save();
	}
}

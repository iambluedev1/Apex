package fr.iambluedev.vulkan.config;

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

}

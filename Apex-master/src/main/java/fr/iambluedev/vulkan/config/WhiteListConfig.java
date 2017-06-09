package fr.iambluedev.vulkan.config;

import fr.iambluedev.spartan.api.config.SpartanConfig;
import fr.iambluedev.spartan.api.gson.JSONObject;

public class WhiteListConfig extends SpartanConfig{

	public WhiteListConfig() {
		super("whitelist");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setupConfig() {
		JSONObject whitelistObj = new JSONObject();
		this.getJsonObject().put("whitelist", whitelistObj);
		this.save();
	}

}

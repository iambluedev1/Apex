package fr.iambluedev.vulkan.rest;

import com.google.gson.Gson;

import de.jackwhite20.apex.Main;
import de.jackwhite20.apex.rest.response.ApexResponse;
import de.jackwhite20.cobra.server.filter.FilteredRequest;
import de.jackwhite20.cobra.server.filter.RequestFilter;
import de.jackwhite20.cobra.shared.Status;
import de.jackwhite20.cobra.shared.http.Response;
import fr.iambluedev.spartan.api.gson.JSONObject;

public class VulkanFilter implements RequestFilter{

	@Override
	public void filter(FilteredRequest request) {
		Gson gson = new Gson();
		JSONObject restObj = (JSONObject) Main.getVulkan().getApexConfig().getJsonObject().get("rest");
		String token = restObj.get("token") + "";
		if(request.header("token") == null) {
			request.abortWith(Response.ok().content(gson.toJson(new ApexResponse(Status.FORBIDDEN, "Please specify a token"))).build());
		}else if(!request.header("token").equals(token)) {
			request.abortWith(Response.ok().content(gson.toJson(new ApexResponse(Status.UNAUTHORIZED, "Please enter a valid token"))).build());
		}
	}

}

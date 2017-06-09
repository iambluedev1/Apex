package fr.iambluedev.vulkan.rest;

import com.google.gson.Gson;

import de.jackwhite20.apex.rest.response.ApexResponse;
import de.jackwhite20.cobra.server.http.Request;
import de.jackwhite20.cobra.server.http.annotation.Path;
import de.jackwhite20.cobra.server.http.annotation.Produces;
import de.jackwhite20.cobra.server.http.annotation.method.GET;
import de.jackwhite20.cobra.shared.ContentType;
import de.jackwhite20.cobra.shared.http.Response;

@Path("/vulkan")
public class VulkanResource {
	
	private static Gson gson = new Gson();
	
	@GET
    @Path("/stats")
    @Produces(ContentType.APPLICATION_JSON)
    public Response stats(Request httpRequest) {
		 return Response.ok().content(gson.toJson(new ApexResponse(ApexResponse.Status.OK, "Successfully added server"))).build();
	}
}

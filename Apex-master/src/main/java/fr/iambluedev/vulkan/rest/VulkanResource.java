package fr.iambluedev.vulkan.rest;

import com.google.gson.Gson;

import de.jackwhite20.apex.Apex;
import de.jackwhite20.apex.Main;
import de.jackwhite20.apex.rest.response.ApexListResponse;
import de.jackwhite20.apex.rest.response.ApexResponse;
import de.jackwhite20.cobra.server.http.Request;
import de.jackwhite20.cobra.server.http.annotation.Path;
import de.jackwhite20.cobra.server.http.annotation.PathParam;
import de.jackwhite20.cobra.server.http.annotation.Produces;
import de.jackwhite20.cobra.server.http.annotation.method.GET;
import de.jackwhite20.cobra.shared.ContentType;
import de.jackwhite20.cobra.shared.http.Response;
import fr.iambluedev.vulkan.Vulkan;
import fr.iambluedev.vulkan.state.ListeningState;
import fr.iambluedev.vulkan.state.WhitelistState;

@Path("/vulkan")
public class VulkanResource {
	
	private static Gson gson = new Gson();
	
	@GET
    @Path("/whitelist/list")
    @Produces(ContentType.APPLICATION_JSON)
    public Response list(Request httpRequest) {
		return this.list();
	}
	
	public Response list(){
		return Response.ok().content(gson.toJson(new ApexListResponse(ApexResponse.Status.OK, "List of whitelisted ip : ", Main.getVulkan().getWhitelistedIp()))).build();
	}

	@GET
    @Path("/whitelist/status")
    @Produces(ContentType.APPLICATION_JSON)
    public Response status(Request httpRequest) {
		return this.status();
	}
	
	public Response status(){
		return Response.ok().content(gson.toJson(new ApexResponse(ApexResponse.Status.OK, "Status : " + Vulkan.getInstance().getWhitelistState()))).build();
	}
	
	@GET
    @Path("/whitelist/state/{state}")
    @Produces(ContentType.APPLICATION_JSON)
    public Response state(Request httpRequest, @PathParam String state) {
		return this.state(state);
	}
	
	public Response state(String state){
		if(state.equalsIgnoreCase("on")){
			Main.getVulkan().setWhitelistState(WhitelistState.ON);
			Apex.getLogger().info("Turning on whitelisting !");
			return Response.ok().content(gson.toJson(new ApexResponse(ApexResponse.Status.OK, "Turning on whitelisting"))).build();
		}else if(state.equalsIgnoreCase("off")){
			Main.getVulkan().setWhitelistState(WhitelistState.OFF);
			Apex.getLogger().info("Turning off whitelisting !");
			return Response.ok().content(gson.toJson(new ApexResponse(ApexResponse.Status.OK, "Turning off whitelisting"))).build();
		}else{
			return Response.ok().content(gson.toJson(new ApexResponse(ApexResponse.Status.ERROR, "Please specify a valid arg !"))).build();
		}
	}
	
	@GET
    @Path("/whitelist/add/{ip}")
    @Produces(ContentType.APPLICATION_JSON)
    public Response add(Request httpRequest, @PathParam String ip) {
		return this.add(ip);
	}
	
	public Response add(String ip){
		Vulkan.getInstance().addIp(ip);
		Apex.getLogger().info(ip + " added to the whitelist !");
		return Response.ok().content(gson.toJson(new ApexResponse(ApexResponse.Status.OK, ip + " added to the whitelist !"))).build();
	}
	
	@GET
    @Path("/whitelist/remove/{ip}")
    @Produces(ContentType.APPLICATION_JSON)
    public Response remove(Request httpRequest, @PathParam String ip) {
		return this.remove(ip);
	}
	
	public Response remove(String ip){
		Vulkan.getInstance().removeIp(ip);
		Apex.getLogger().info(ip + " removed from the whitelist !");
		return Response.ok().content(gson.toJson(new ApexResponse(ApexResponse.Status.OK, ip + " removed from the whitelist !"))).build();
	}
	
	@GET
    @Path("/ports/close")
    @Produces(ContentType.APPLICATION_JSON)
    public Response close(Request httpRequest) {
		return this.close();
	}
	
	public Response close(){
		Vulkan.getInstance().setListeningState(ListeningState.CLOSE);
		Apex.getLogger().info("Frontend ports are been closed");
		return Response.ok().content(gson.toJson(new ApexResponse(ApexResponse.Status.OK, "Frontend ports are been closed"))).build();
	}
	
	@GET
    @Path("/ports/open")
    @Produces(ContentType.APPLICATION_JSON)
    public Response open(Request httpRequest) {
		return this.open();
	}
	
	public Response open(){
		Vulkan.getInstance().setListeningState(ListeningState.OPEN);
		Apex.getLogger().info("Frontend ports are been opened");
		return Response.ok().content(gson.toJson(new ApexResponse(ApexResponse.Status.OK, "Frontend ports are been opened"))).build();
	}
	
	@GET
    @Path("/ports/status")
    @Produces(ContentType.APPLICATION_JSON)
    public Response pstatus(Request httpRequest) {
		return this.pstatus();
	}
	
	public Response pstatus(){
		return Response.ok().content(gson.toJson(new ApexResponse(ApexResponse.Status.OK, "Status : " + Vulkan.getInstance().getListeningState()))).build();
	}
}

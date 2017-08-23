/*
 * Copyright (c) 2017 "JackWhite20"
 *
 * This file is part of Apex.
 *
 * Apex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.jackwhite20.apex.rest.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import de.jackwhite20.apex.Apex;
import de.jackwhite20.apex.rest.response.ApexResponse;
import de.jackwhite20.apex.rest.response.ApexStatsResponse;
import de.jackwhite20.apex.task.ConnectionsPerSecondTask;
import de.jackwhite20.apex.util.BackendInfo;
import de.jackwhite20.cobra.server.http.Request;
import de.jackwhite20.cobra.server.http.annotation.Path;
import de.jackwhite20.cobra.server.http.annotation.PathParam;
import de.jackwhite20.cobra.server.http.annotation.Produces;
import de.jackwhite20.cobra.server.http.annotation.method.GET;
import de.jackwhite20.cobra.shared.ContentType;
import de.jackwhite20.cobra.shared.Status;
import de.jackwhite20.cobra.shared.http.Response;
import fr.iambluedev.vulkan.rest.ApexMapResponse;
import fr.iambluedev.vulkan.util.FrontendInfo;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;

/**
 * Created by JackWhite20 on 27.06.2016.
 */
@Path("/apex")
public class ApexResource {

    private static final Response STATS_DISABLED;

    private static Logger logger = LoggerFactory.getLogger(ApexResource.class);

    private static Gson gson = new Gson();

    private static GlobalTrafficShapingHandler trafficShapingHandler = Apex.getInstance().getTrafficShapingHandler();

    private static ConnectionsPerSecondTask connectionsPerSecondTask = Apex.getInstance().getConnectionsPerSecondTask();

    static {
        STATS_DISABLED = Response.ok().content(gson.toJson(new ApexStatsResponse(Status.NOT_IMPLEMENTED,
                "Stats are disabled",
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1))).build();
    }

    @GET
    @Path("/add/{name}/{ip}/{port}/{frontend}")
    @Produces(ContentType.APPLICATION_JSON)
    public Response add(Request httpRequest, @PathParam String name, @PathParam String ip, @PathParam String port, @PathParam String frontend) {
    	BackendInfo found = null;
    	if(Apex.getInstance().getFrontendInfo().contains(frontend)){
    		FrontendInfo info = null;
    		for(FrontendInfo info2 : Apex.getInstance().getFrontendInfo()){
    			if(info2.getName().equals(frontend)){
    				info = info2;
    				break;
    			}
    		}
    		synchronized (info) {
	            for (BackendInfo backend : info.getBalancingStrategy().getBackend()) {
	                if (backend.getName().equalsIgnoreCase(name)) {
	                    found = backend;
	                    break;
	                }
	            }
	        }
    		if (found == null) {
                BackendInfo backend = new BackendInfo(name, ip, Integer.valueOf(port));
                info.getBalancingStrategy().addBackend(backend);
                info.getBackendTask().addBackend(backend);

                logger.info("Added backend server {}:{} to the load balancer", ip, port);

                return Response.ok().content(gson.toJson(new ApexResponse(Status.OK,
                        "Successfully added server"))).build();
            } else {
                return Response.ok().content(gson.toJson(new ApexResponse(Status.NOT_MODIFIED,
                        "Server was already added"))).build();
            }
    	}else{
    		return Response.ok().content(gson.toJson(new ApexResponse(Status.NOT_FOUND,
                    "Frontend not found"))).build();
    	}
    }

    @GET
    @Path("/remove/{name}/{frontend}")
    @Produces(ContentType.APPLICATION_JSON)
    public Response remove(Request httpRequest, @PathParam String name, @PathParam String frontend) {
        BackendInfo found = null;
    	if(Apex.getInstance().getFrontendInfo().contains(frontend)){
    		FrontendInfo info = null;
    		for(FrontendInfo info2 : Apex.getInstance().getFrontendInfo()){
    			if(info2.getName().equals(frontend)){
    				info = info2;
    				break;
    			}
    		}
    		synchronized (info) {
	            for (BackendInfo backend : info.getBalancingStrategy().getBackend()) {
	                if (backend.getName().equalsIgnoreCase(name)) {
	                    found = backend;
	                    break;
	                }
	            }
	        }
    		if (found == null) {
                info.getBalancingStrategy().removeBackend(found);
                info.getBackendTask().removeBackend(found);

                logger.info("Removed backend server {} from the load balancer", name);

                return Response.ok().content(gson.toJson(new ApexResponse(Status.OK,
                        "Successfully removed server"))).build();
            } else {
            	 return Response.ok().content(gson.toJson(new ApexResponse(Status.NOT_FOUND,
                         "Server not found"))).build();
            }
    	}else{
    		return Response.ok().content(gson.toJson(new ApexResponse(Status.NOT_FOUND,
                    "Frontend not found"))).build();
    	}
    }

    @GET
    @Path("/list")
    @Produces(ContentType.APPLICATION_JSON)
    public Response list(Request httpRequest) {
    	Map<String, List<Map<String, Object>>> backend = new HashMap<String, List<Map<String, Object>>>();
    	for(FrontendInfo info : Apex.getInstance().getFrontendInfo()){
    		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
    		for(BackendInfo backInfo : info.getBalancingStrategy().getBackend()) {
    			Map<String, Object> tmp = new HashMap<String, Object>();
    			tmp.put("host", backInfo.getHost());
    			tmp.put("port", backInfo.getPort());
    			tmp.put("name", backInfo.getName());
    			tmp.put("time", backInfo.getConnectTime());
    			datas.add(tmp);
    		}
			backend.put(info.getName(), datas);
        }
    	
        if (backend.size() != 0) {
            return Response.ok().content(gson.toJson(new ApexMapResponse(Status.OK, "Map received", backend))).build();
        } else {
            return Response.ok().content(gson.toJson(new ApexResponse(Status.OK, "No Backend up !"))).build();
        }
    }

    @GET
    @Path("/stats")
    @Produces(ContentType.APPLICATION_JSON)
    public Response stats(Request httpRequest) {

        if (trafficShapingHandler != null) {
            TrafficCounter trafficCounter = trafficShapingHandler.trafficCounter();
            int size = 0;
            for(FrontendInfo info : Apex.getInstance().getFrontendInfo()){
            	size += info.getBalancingStrategy().getBackend().size();
            }
            return Response.ok().content(gson.toJson(new ApexStatsResponse(Status.OK,
                    "OK",
                    Apex.getChannelGroup().size(),
                    connectionsPerSecondTask.getPerSecond(),
                    size,
                    trafficCounter.currentReadBytes(),
                    trafficCounter.currentWrittenBytes(),
                    trafficCounter.lastReadThroughput(),
                    trafficCounter.lastWriteThroughput(),
                    trafficCounter.cumulativeReadBytes(),
                    trafficCounter.cumulativeWrittenBytes()))).build();
        } else {
            return STATS_DISABLED;
        }
    }
}

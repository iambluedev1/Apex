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

package de.jackwhite20.apex.udp.pipeline;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jackwhite20.apex.Apex;
import de.jackwhite20.apex.task.ConnectionsPerSecondTask;
import de.jackwhite20.apex.util.BackendInfo;
import de.jackwhite20.apex.util.ChannelUtil;
import de.jackwhite20.apex.util.PipelineUtils;
import fr.iambluedev.vulkan.Vulkan;
import fr.iambluedev.vulkan.backend.DefaultMcBackend;
import fr.iambluedev.vulkan.backend.DefaultWebBackend;
import fr.iambluedev.vulkan.state.ListeningState;
import fr.iambluedev.vulkan.state.WhitelistState;
import fr.iambluedev.vulkan.util.FrontendInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;

/**
 * Created by JackWhite20 on 05.11.2016.
 */
public class DatagramUpstreamHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static Logger logger = LoggerFactory.getLogger(DatagramUpstreamHandler.class);
    private ConnectionsPerSecondTask connectionsPerSecondTask;
    private FrontendInfo frontend;
    
    public DatagramUpstreamHandler(FrontendInfo frontend){
    	this.frontend = frontend;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.connectionsPerSecondTask = Apex.getInstance().getConnectionsPerSecondTask();
        ctx.channel().pipeline().addLast(Apex.getInstance().getTrafficShapingHandler());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket datagramPacket) throws Exception {
        BackendInfo backendCheck = this.frontend.getBalancingStrategy().selectBackend("", 0);
        
        if(Vulkan.getInstance().getListeningState() == ListeningState.CLOSE){
    		if(this.frontend.getPort().equals(80)){
    			backendCheck = new DefaultWebBackend();
        		logger.error("ListeningState is set to close so redirecting to the Default VulkanNetwork Web Backend");
    		}else if(this.frontend.getPort().equals(25565)){
    			backendCheck = new DefaultMcBackend();
        		logger.error("ListeningState is set to close so redirecting to the Default VulkanNetwork Mc Backend");
    		}else{
        		logger.error("ListeningState is set to close.");
        		return;
    		}
    	}
    	
    	if(Vulkan.getInstance().getWhitelistState() == WhitelistState.ON){
			if(!Vulkan.getInstance().getWhitelistedIp().contains(datagramPacket.sender().getHostName())){
				if(this.frontend.getPort().equals(80)){
					backendCheck = new DefaultWebBackend();
		    		logger.error("WhitelistState is set to on so redirecting to the Default VulkanNetwork Web Backend");
				}else if(this.frontend.getPort().equals(25565)){
					backendCheck = new DefaultMcBackend();
	    			logger.error("WhitelistState is set to on so redirecting to the Default VulkanNetwork Mc Backend");
	    		}else{
	        		logger.error("WhitelistState is set to on.");
	        		return;
				}
			}
    	}
    	
    	if (backendCheck == null) {
        	if(this.frontend.getPort().equals(80)){
        		backendCheck = new DefaultWebBackend();
        		logger.error("Unable to select a web backend server for the port (" + this.frontend.getPort() + "). All down? Redirecting to the Default VulkanNetwork Web Backend");
        	}else if(this.frontend.getPort().equals(25565)){
        		backendCheck = new DefaultMcBackend();
    			logger.error("Unable to select a mc backend server for the port (" + this.frontend.getPort() + "). All down? Redirecting to the Default VulkanNetwork Mc Backend");
    		}else{
        		logger.error("Unable to select a backend server for the port (" + this.frontend.getPort() + "). All down?");
        		return;
        	}
        }

    	BackendInfo backendInfo = backendCheck;
    	
        ByteBuf copy = datagramPacket.content().copy().retain();

        Bootstrap bootstrap = new Bootstrap()
                .channel(PipelineUtils.getDatagramChannel())
                .handler(new DatagramDownstreamHandler(ctx.channel(), datagramPacket.sender()))
                .group(ctx.channel().eventLoop());

        ChannelFuture channelFuture = bootstrap.bind(0);

        GlobalTrafficShapingHandler trafficShapingHandler = Apex.getInstance().getTrafficShapingHandler();
        if (trafficShapingHandler != null) {
            channelFuture.channel().pipeline().addFirst(trafficShapingHandler);
        }

        channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {

            Channel channel = channelFuture1.channel();
            if (channelFuture1.isSuccess()) {
                channel.writeAndFlush(new DatagramPacket(copy, new InetSocketAddress(backendInfo.getHost(), backendInfo.getPort())));
            } else {
                ChannelUtil.close(channel);
            }

            copy.release();
        });

        if (this.connectionsPerSecondTask != null) {
            this.connectionsPerSecondTask.inc();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ChannelUtil.close(ctx.channel());

        if (!(cause instanceof IOException)) {
            logger.error(cause.getMessage(), cause);
        }
    }

	public FrontendInfo getFrontend() {
		return this.frontend;
	}
}

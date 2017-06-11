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

package de.jackwhite20.apex.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jackwhite20.apex.Apex;
import de.jackwhite20.apex.tcp.pipeline.initialize.ApexSocketChannelInitializer;
import de.jackwhite20.apex.util.PipelineUtils;
import fr.iambluedev.vulkan.util.FrontendInfo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollMode;

/**
 * Created by JackWhite20 on 26.06.2016.
 */
public class ApexSocket extends Apex {

    private static Logger logger = LoggerFactory.getLogger(ApexSocket.class);

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private FrontendInfo frontend;
    private int backlog;
    
	public ApexSocket(EventLoopGroup bossGroup, EventLoopGroup workerGroup, FrontendInfo frontend, int backlog) {
		this.bossGroup = bossGroup;
		this.workerGroup = workerGroup;
		this.frontend = frontend;
		this.backlog = backlog;
	}

	public static Logger getLogger() {
		return logger;
	}

	public EventLoopGroup getBossGroup() {
		return this.bossGroup;
	}

	public EventLoopGroup getWorkerGroup() {
		return this.workerGroup;
	}

	public FrontendInfo getFrontend() {
		return this.frontend;
	}

	public int getBacklog() {
		return this.backlog;
	}
	
    public Channel bootstrap() throws Exception {
        logger.info("Bootstrapping socket server for frontend : " + this.frontend.getName() + " (" + this.frontend.getIp() + ":" + this.frontend.getPort()+")");
        ServerBootstrap bootstrap = new ServerBootstrap().group(this.bossGroup, this.workerGroup).channel(PipelineUtils.getServerChannel()).childHandler(new ApexSocketChannelInitializer(this.frontend)).childOption(ChannelOption.AUTO_READ, false);
        if (PipelineUtils.isEpoll()) {
            bootstrap.childOption(EpollChannelOption.EPOLL_MODE, EpollMode.LEVEL_TRIGGERED);
            logger.debug("Epoll mode is now level triggered");
        }
        return bootstrap.option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_BACKLOG, this.backlog).bind(this.frontend.getIp(), this.frontend.getPort()).sync().channel();
    }
}

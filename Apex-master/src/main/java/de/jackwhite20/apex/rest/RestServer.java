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

package de.jackwhite20.apex.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jackwhite20.cobra.server.CobraServer;
import de.jackwhite20.cobra.server.CobraServerFactory;

/**
 * Created by JackWhite20 on 27.06.2016.
 */
public class RestServer {

    private static Logger logger = LoggerFactory.getLogger(RestServer.class);
    private CobraServer cobraServer;

    private String ip;
    private Integer port;
    
    public RestServer(String ip, Integer port) {
    	this.ip = ip;
    	this.port = port;
    }

    public void start() {
        cobraServer = CobraServerFactory.create(new ApexRestConfig(this.ip, this.port));
        cobraServer.start();

        logger.info("RESTful API listening on {}:{}", ip, port);
    }

    public void stop() {

        cobraServer.stop();
    }
}

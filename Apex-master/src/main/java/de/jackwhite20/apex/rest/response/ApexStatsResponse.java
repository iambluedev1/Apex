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

package de.jackwhite20.apex.rest.response;

import de.jackwhite20.cobra.shared.Status;

/**
 * Created by JackWhite20 on 30.10.2016.
 */
public class ApexStatsResponse extends ApexResponse {
	
	@SuppressWarnings("unused")
    private int connections;

    @SuppressWarnings("unused")
	private int connectionsPerSecond;
    
    @SuppressWarnings("unused")
    private int onlineBackendServers;
    
    @SuppressWarnings("unused")
    private long currentReadBytes;
    
    @SuppressWarnings("unused")
    private long currentWrittenBytes;
    
    @SuppressWarnings("unused")
    private long lastReadThroughput;
    
    @SuppressWarnings("unused")
    private long lastWriteThroughput;
    
    @SuppressWarnings("unused")
    private long totalReadBytes;
    
    @SuppressWarnings("unused")
    private long totalWrittenBytes;

    public ApexStatsResponse(Status status, String message, int connections, int connectionsPerSecond, int onlineBackendServers, long currentReadBytes, long currentWrittenBytes, long lastReadThroughput, long lastWriteThroughput, long totalReadBytes, long totalWrittenBytes) {

        super(status, message);

        this.connections = connections;
        this.connectionsPerSecond = connectionsPerSecond;
        this.onlineBackendServers = onlineBackendServers;
        this.currentReadBytes = currentReadBytes;
        this.currentWrittenBytes = currentWrittenBytes;
        this.lastReadThroughput = lastReadThroughput;
        this.lastWriteThroughput = lastWriteThroughput;
        this.totalReadBytes = totalReadBytes;
        this.totalWrittenBytes = totalWrittenBytes;
    }
}

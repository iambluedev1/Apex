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

package de.jackwhite20.apex.strategy.impl;

import com.google.common.collect.Maps;
import de.jackwhite20.apex.strategy.BalancingStrategy;
import de.jackwhite20.apex.util.BackendInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by JackWhite20 on 26.06.2016.
 */
public class LeastConnectionBalancingStrategy extends BalancingStrategy {

    private Map<BackendInfo, Integer> connections = Maps.newConcurrentMap();

    public LeastConnectionBalancingStrategy(List<BackendInfo> backend) {
        super(backend);
        for (BackendInfo target : backend) {
        	 this.connections.put(target, 0);
        }
    }

    @Override
    public synchronized BackendInfo selectBackend(String originHost, int originPort) {
        int least = Integer.MAX_VALUE;
        BackendInfo leastBackend = null;
        for (Map.Entry<BackendInfo, Integer> entry :  this.connections.entrySet()) {
            if (entry.getValue() < least) {
                least = entry.getValue();
                leastBackend = entry.getKey();
            }
        }
        this.connections.put(leastBackend,  this.connections.get(leastBackend) + 1);
        return leastBackend;
    }

    @Override
    public void disconnectedFrom(BackendInfo backendInfo) {
        Integer count =  this.connections.get(backendInfo);
        if (count != null) {
        	 this.connections.put(backendInfo, count - 1);
        }
    }

    @Override
    public void removeBackendStrategy(BackendInfo backendInfo) {
    	 this.connections.remove(backendInfo);
    }

    @Override
    public void addBackendStrategy(BackendInfo backendInfo) {
    	 this.connections.put(backendInfo, 0);
    }
}

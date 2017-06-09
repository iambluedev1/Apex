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

package de.jackwhite20.apex.strategy;

import de.jackwhite20.apex.util.BackendInfo;

import java.util.Collections;
import java.util.List;

/**
 * Created by JackWhite20 on 26.06.2016.
 */
public abstract class BalancingStrategy {

    protected final List<BackendInfo> backend;

    private int size;

    public BalancingStrategy(List<BackendInfo> backend) {

        this.backend = Collections.synchronizedList(backend);
        this.size = backend.size();
    }

    public abstract BackendInfo selectBackend(String originHost, int originPort);

    public abstract void disconnectedFrom(BackendInfo backendInfo);

    public abstract void removeBackendStrategy(BackendInfo backendInfo);

    public abstract void addBackendStrategy(BackendInfo backendInfo);

    public synchronized void addBackend(BackendInfo targetData) {

        backend.add(targetData);

        addBackendStrategy(targetData);

        size++;
    }

    public synchronized void removeBackend(BackendInfo targetData) {

        backend.remove(targetData);

        removeBackendStrategy(targetData);

        size--;
    }

    public boolean hasBackend(BackendInfo backendInfo) {

        return backend.contains(backendInfo);
    }

    public int size() {

        return size;
    }

    public List<BackendInfo> getBackend() {

        return backend;
    }
}

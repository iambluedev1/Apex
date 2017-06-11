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

package de.jackwhite20.apex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jackwhite20.apex.util.Mode;
import fr.iambluedev.spartan.api.gson.JSONObject;
import fr.iambluedev.vulkan.Vulkan;

/**
 * Created by JackWhite20 on 26.06.2016.
 */
public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private static Vulkan vulkan;
    
    public static void main(String[] args) {
        logger.info("Starting Apex");
        vulkan = new Vulkan();
        JSONObject jsonObj = (JSONObject) vulkan.getApexConfig().getJsonObject().get("general");
        String modeString = (String) jsonObj.get("mode");
        Mode mode = Mode.of(modeString);
        logger.info("Using mode: " + mode);
        Apex apex = new Apex();
        apex.start(mode);
        apex.console();
    }

	public static Vulkan getVulkan() {
		return vulkan;
	}
}

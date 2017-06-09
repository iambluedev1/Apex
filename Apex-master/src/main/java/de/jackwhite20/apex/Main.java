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

        /*File config = new File("config.cope");
        if (!config.exists()) {
            try {
                Files.copy(Main.class.getClassLoader().getResourceAsStream("config.cope"), config.toPath());
            } catch (IOException e) {
                logger.error("Unable to copy default config! No write permissions?", e);
                return;
            }
        }*/
        vulkan = new Vulkan();
        JSONObject jsonObj = (JSONObject) vulkan.getApexConfig().getJsonObject().get("general");
        String modeString = (String) jsonObj.get("mode");
        Mode mode = Mode.of(modeString);
        if (mode == null) {
            logger.error("Invalid mode '{}', using 'tcp' as default mode", modeString);
            mode = Mode.TCP;
        } else {
            logger.info("Using mode: " + mode);
        }
        Apex apex = ApexFactory.create(mode);
        apex.start(mode);
        apex.console();
       /* try {
            CopeConfig copeConfig = Cope.from(config)
                    .def(new Header("general"), new Key("mode"), new Value("tcp"))
                    .def(new Header("general"), new Key("server"), new Value("0.0.0.0"), new Value("80"))
                    .def(new Header("general"), new Key("backlog"), new Value("100"))
                    .def(new Header("general"), new Key("boss"), new Value(String.valueOf(PipelineUtils.DEFAULT_BOSS_THREADS)))
                    .def(new Header("general"), new Key("worker"), new Value(String.valueOf(PipelineUtils.DEFAULT_WORKER_THREADS)))
                    .def(new Header("general"), new Key("balance"), new Value("RANDOM"))
                    .def(new Header("general"), new Key("timeout"), new Value("60"), new Value("60"))
                    .def(new Header("general"), new Key("probe"), new Value("10000"))
                    .def(new Header("general"), new Key("debug"), new Value("true"))
                    .def(new Header("general"), new Key("stats"), new Value("true"))
                    .build();

            logger.info("Config loaded");
            String modeString = copeConfig.getHeader("general").getKey("mode").next().asString();
            Mode mode = Mode.of(modeString);
            if (mode == null) {
                logger.error("Invalid mode '{}', using 'tcp' as default mode", modeString);

                mode = Mode.TCP;
            } else {
                logger.info("Using mode: " + mode);
            }

            Apex apex = ApexFactory.create(mode, copeConfig);
            apex.start(mode);
            apex.console();
        } catch (CopeException e) {
            logger.error("Unable to load config", e);
        }*/
    }

	public static Vulkan getVulkan() {
		return vulkan;
	}
}

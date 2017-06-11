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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import de.jackwhite20.apex.command.Command;
import de.jackwhite20.apex.command.CommandManager;
import de.jackwhite20.apex.command.impl.DebugCommand;
import de.jackwhite20.apex.command.impl.EndCommand;
import de.jackwhite20.apex.command.impl.HelpCommand;
import de.jackwhite20.apex.command.impl.StatsCommand;
import de.jackwhite20.apex.rest.RestServer;
import de.jackwhite20.apex.strategy.StrategyType;
import de.jackwhite20.apex.task.ConnectionsPerSecondTask;
import de.jackwhite20.apex.util.ApexThreadFactory;
import de.jackwhite20.apex.util.BackendInfo;
import de.jackwhite20.apex.util.FileUtil;
import de.jackwhite20.apex.util.Mode;
import de.jackwhite20.apex.util.PipelineUtils;
import de.jackwhite20.apex.util.ReflectionUtil;
import fr.iambluedev.spartan.api.gson.JSONObject;
import fr.iambluedev.spartan.utils.Callback;
import fr.iambluedev.spartan.utils.RedisJsonMessage;
import fr.iambluedev.vulkan.Vulkan;
import fr.iambluedev.vulkan.command.CloseCommand;
import fr.iambluedev.vulkan.command.OpenCommand;
import fr.iambluedev.vulkan.command.WhitelistCommand;
import fr.iambluedev.vulkan.util.FrontendInfo;
import fr.iambluedev.vulkan.util.MapUtil;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.concurrent.GlobalEventExecutor;
import redis.clients.jedis.Jedis;

/**
 * Created by JackWhite20 on 05.11.2016.
 */
public class Apex {
	
	private static Apex instance;
	
	private static Logger logger = LoggerFactory.getLogger(Apex.class);
	private ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	private static ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("de.jackwhite20.apex");
	 
    private static final Pattern ARGS_PATTERN = Pattern.compile(" ");
    
    private ScheduledExecutorService scheduledExecutorService;
    private Channel serverChannel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private GlobalTrafficShapingHandler trafficShapingHandler;
    private RestServer restServer;
    private CommandManager commandManager;
    private Scanner scanner;
    private ConnectionsPerSecondTask connectionsPerSecondTask;
    
    public Apex() {
        Apex.instance = this;
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ApexThreadFactory("Check Task"));
        this.commandManager = new CommandManager();
    }
    
    public void start(Mode mode) {
    	this.commandManager.addCommand(new HelpCommand("help", "List of available commands", "h"));
        this.commandManager.addCommand(new EndCommand("end", "Stops Apex", "stop", "exit"));
        this.commandManager.addCommand(new DebugCommand("debug", "Turns the debug mode on/off", "d"));
        this.commandManager.addCommand(new StatsCommand("stats", "Shows live stats", "s", "info"));
        
        this.commandManager.addCommand(new CloseCommand("close", "Close the default listened port", "c"));
        this.commandManager.addCommand(new OpenCommand("open", "Open the default listened port", "o"));
        this.commandManager.addCommand(new WhitelistCommand("whitelist", "Manage the whitelist", "w"));
        
        JSONObject jsonObj = (JSONObject) Main.getVulkan().getApexConfig().getJsonObject().get("general");
        
        Integer bossKey = Integer.valueOf(jsonObj.get("boss") + "");
        Integer workerKey = Integer.valueOf(jsonObj.get("worker") + "");
        Integer backlogKey = Integer.valueOf( jsonObj.get("backlog") + "");
        Integer probeKey = Integer.valueOf(jsonObj.get("probe") + "");
        Boolean debugKey = Boolean.valueOf(jsonObj.get("debug") + "");
        Boolean statsKey = Boolean.valueOf(jsonObj.get("stats") + "");

        this.changeDebug(debugKey ? Level.DEBUG : Level.INFO);
        
        Map<String, List<BackendInfo>> backendInfo = new HashMap<String, List<BackendInfo>>();
        
        JSONObject backendObj = (JSONObject) Main.getVulkan().getApexConfig().getJsonObject().get("backend");
        for(Object obj : backendObj.keySet()){
        	JSONObject backend = (JSONObject) ((JSONObject) Main.getVulkan().getApexConfig().getJsonObject().get("backend")).get(obj);
        	BackendInfo info = new BackendInfo((String) obj, (String) backend.get("ip"), Integer.valueOf(backend.get("port") + ""));
        	if(!backendInfo.containsKey((String) backend.get("frontend"))) {
        		List<BackendInfo> back = new ArrayList<BackendInfo>();
        		back.add(info);
        		backendInfo.put((String) backend.get("frontend"), back);
        	}else{
        		backendInfo.put((String) backend.get("frontend"), MapUtil.addAndReturn(backendInfo.get((String) backend.get("frontend")), info));
        	}
        }
        
        List<FrontendInfo> frontendInfo = new ArrayList<FrontendInfo>();
        JSONObject frontendObj = (JSONObject) Main.getVulkan().getApexConfig().getJsonObject().get("frontend");
        for(Object obj : frontendObj.keySet()){
        	JSONObject frontend = (JSONObject) ((JSONObject) Main.getVulkan().getApexConfig().getJsonObject().get("frontend")).get(obj);
        	FrontendInfo info = new FrontendInfo((String) obj, (String) frontend.get("ip"), Integer.valueOf(frontend.get("port") + ""), Mode.of((String) frontend.get("mode")), StrategyType.valueOf((String) frontend.get("balance")), Integer.valueOf(frontend.get("timeout") + ""), backendInfo.get((String) obj));
        	frontendInfo.add(info);
        }
        
        logger.debug("Backlog: {}", backlogKey);
        logger.debug("Boss: {}", bossKey);
        logger.debug("Worker: {}", workerKey);
        logger.debug("Stats: {}", statsKey);
        logger.debug("Probe: {}", probeKey);
        
        for(Entry<String, List<BackendInfo>> backend : backendInfo.entrySet()){
        	logger.debug("Backend ("+ backend.getKey() + "): {}", backend.getValue().stream().map(BackendInfo::getName).collect(Collectors.joining(", ")));
        }
        
        logger.debug("Frontend: {}", frontendInfo.stream().map(FrontendInfo::getName).collect(Collectors.joining(", ")));
        
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);

        if (PipelineUtils.isEpoll()) {
            logger.info("Using high performance epoll event notification mechanism");
        } else {
            logger.info("Using normal select/poll event notification mechanism");
        }

        int bossThreads = bossKey;
        if (bossThreads < PipelineUtils.DEFAULT_THREADS_THRESHOLD) {
            bossThreads = PipelineUtils.DEFAULT_BOSS_THREADS;

            logger.warn("Boss threads needs to be greater or equal than {}. Using default value of {}",  PipelineUtils.DEFAULT_THREADS_THRESHOLD, PipelineUtils.DEFAULT_BOSS_THREADS);
        }

        int workerThreads = workerKey;
        if (workerThreads < PipelineUtils.DEFAULT_THREADS_THRESHOLD) {
            workerThreads = PipelineUtils.DEFAULT_WORKER_THREADS;

            logger.warn("Worker threads needs to be greater or equal than {}. Using default value of {}", PipelineUtils.DEFAULT_THREADS_THRESHOLD, PipelineUtils.DEFAULT_WORKER_THREADS);
        }

        this.bossGroup = PipelineUtils.newEventLoopGroup(bossThreads, new ApexThreadFactory("Apex Boss Thread"));
        this.workerGroup = PipelineUtils.newEventLoopGroup(workerThreads, new ApexThreadFactory("Apex Worker Thread"));

        if (statsKey) {
            connectionsPerSecondTask = new ConnectionsPerSecondTask();
            
            long[] totalBytes = FileUtil.loadStats();
            
            logger.debug("Loaded total read bytes: {}", totalBytes[0]);
            logger.debug("Loaded total written bytes: {}", totalBytes[1]);
            
            this.trafficShapingHandler = new GlobalTrafficShapingHandler(workerGroup, 0, 0);
            
            ReflectionUtil.setAtomicLong(this.trafficShapingHandler.trafficCounter(), "cumulativeReadBytes", totalBytes[0]);
            ReflectionUtil.setAtomicLong(this.trafficShapingHandler.trafficCounter(), "cumulativeWrittenBytes", totalBytes[1]);
            
            logger.debug("Traffic stats collect handler initialized");
        }
        
        try {
        	for(FrontendInfo frontend : frontendInfo){
        		frontend.start(this.bossGroup, this.workerGroup, backlogKey);
    	 	}
            
            int probe = probeKey;
            if (probe < -1 || probe == 0) {
                probe = 10000;
                logger.warn("Probe time value must be -1 to turn it off or greater than 0");
                logger.warn("Using default probe time of 10000 milliseconds (10 seconds)");
            }

            if (probe != -1) {
            	// CONVERT TO FRONTEND
                //backendTask = (mode == Mode.TCP) ? new CheckSocketBackendTask(balancingStrategy) : new CheckDatagramBackendTask(balancingStrategy);
            	//scheduledExecutorService.scheduleAtFixedRate(backendTask, 0, probe, TimeUnit.MILLISECONDS);
                //---
            } else {
            	this.scheduledExecutorService.shutdown();
            }
            
            JSONObject restObj = (JSONObject) Main.getVulkan().getApexConfig().getJsonObject().get("rest");
            this.restServer = new RestServer((String) restObj.get("ip"), Integer.valueOf(restObj.get("port") + ""));
            this.restServer.start();

            for(FrontendInfo frontend : frontendInfo){
            	logger.info("Apex listening (" + frontend.getName() + ") on {}:{}", frontend.getIp(), frontend.getPort());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Vulkan.getInstance().getRedis().get(new Callback<Jedis>() {
			@Override
			public void call(Jedis jedis) {
				jedis.publish("apex", new RedisJsonMessage().setCmd("started").setContent("Apex started !").get());
			}
		});
    }

    public void console() {
    	this. scanner = new Scanner(System.in);
        try {
            String line;
            while ((line = this.scanner.nextLine()) != null) {
                if (!line.isEmpty()) {
                    String[] split = ARGS_PATTERN.split(line);

                    if (split.length == 0) {
                        continue;
                    }

                    String commandName = split[0].toLowerCase();
                    Command command = this.commandManager.findCommand(commandName);
                    if (command != null) {
                        logger.info("Executing command: {}", line);
                        String[] cmdArgs = Arrays.copyOfRange(split, 1, split.length);
                        command.execute(cmdArgs);
                    } else {
                        logger.info("Command not found!");
                    }
                }
            }
        } catch (IllegalStateException ignore) {}
    }

    public void changeDebug(Level level) {
        rootLogger.setLevel(level);
        logger.info("Logger level is now {}", rootLogger.getLevel());
    }

    public void changeDebug() {
        changeDebug((rootLogger.getLevel() == Level.INFO) ? Level.DEBUG : Level.INFO);
    }

    public void stop() {
        logger.info("Apex is going to be stopped");

        this.scanner.close();

        if (this.serverChannel != null) {
        	this.serverChannel.close();
        }

        if (this.connectionsPerSecondTask != null) {
        	this.connectionsPerSecondTask.stop();
        }

        this.scheduledExecutorService.shutdown();

        if (this.trafficShapingHandler != null) {
            FileUtil.saveStats(this.trafficShapingHandler.trafficCounter().cumulativeReadBytes(), this.trafficShapingHandler.trafficCounter().cumulativeWrittenBytes());
        	logger.info("Total bytes stats saved");
			this.trafficShapingHandler.release();
        }

        this.bossGroup.shutdownGracefully();
        this.workerGroup.shutdownGracefully();

        try {
        	this.restServer.stop();
        } catch (Exception e) {
            logger.warn("RESTful API server already stopped");
        }

        logger.info("Apex has been stopped");
    }

    public static CommandManager getCommandManager() {
        return instance.commandManager;
    }
    
    public static Channel getServerChannel() {
        return instance.serverChannel;
    }

    public static ChannelGroup getChannelGroup() {
        return instance.channelGroup;
    }

    public GlobalTrafficShapingHandler getTrafficShapingHandler() {
        return this.trafficShapingHandler;
    }

    public ConnectionsPerSecondTask getConnectionsPerSecondTask() {
        return this.connectionsPerSecondTask;
    }

    public static Apex getInstance() {
        return instance;
    }

	public static Logger getLogger() {
		return logger;
	}
}

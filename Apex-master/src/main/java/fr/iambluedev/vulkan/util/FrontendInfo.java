package fr.iambluedev.vulkan.util;

import java.util.List;

import de.jackwhite20.apex.strategy.BalancingStrategy;
import de.jackwhite20.apex.strategy.BalancingStrategyFactory;
import de.jackwhite20.apex.strategy.StrategyType;
import de.jackwhite20.apex.tcp.ApexSocket;
import de.jackwhite20.apex.udp.ApexDatagram;
import de.jackwhite20.apex.util.BackendInfo;
import de.jackwhite20.apex.util.Mode;
import io.netty.channel.EventLoopGroup;

public class FrontendInfo {

	private String name;
	private String ip;
	private Integer port;
	private Mode mode;
	private StrategyType type;
	private Integer timeout;
	private BalancingStrategy balancingStrategy;
	private List<BackendInfo> backend;
	
	public FrontendInfo(String name, String ip, Integer port, Mode mode, StrategyType type, Integer timeout, List<BackendInfo> info) {
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.mode = mode;
		this.type = type;
		this.timeout = timeout;
		this.backend = info;
		this.balancingStrategy = BalancingStrategyFactory.create(type, info);
	}

	public String getName() {
		return this.name;
	}

	public String getIp() {
		return this.ip;
	}

	public Integer getPort() {
		return this.port;
	}

	public Mode getMode() {
		return this.mode;
	}

	public StrategyType getType() {
		return this.type;
	}

	public Integer getTimeout() {
		return this.timeout;
	}

	public List<BackendInfo> getBackend() {
		return this.backend;
	}

	public void start(EventLoopGroup bossGroup, EventLoopGroup workerGroup, int backlog, int readTimeout, int writeTimeout){
        switch (this.mode) {
            case TCP:
                new ApexSocket();
            case UDP:
                new ApexDatagram();
            default:
            	new ApexSocket();
        }
	}
	
	@Override
	public String toString() {
		return "FrontendInfo [name=" + name + ", ip=" + ip + ", port=" + port + ", mode=" + mode + ", type=" + type
				+ ", timeout=" + timeout + ", balancingStrategy=" + balancingStrategy + ", backend=" + backend + "]";
	}
	
}

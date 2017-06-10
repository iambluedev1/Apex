package fr.iambluedev.vulkan.util;

import de.jackwhite20.apex.strategy.StrategyType;
import de.jackwhite20.apex.util.Mode;

public class FrontendInfo {

	private String name;
	private String ip;
	private Integer port;
	private Mode mode;
	private StrategyType type;
	private Integer timeout;
	
	public FrontendInfo(String name, String ip, Integer port, Mode mode, StrategyType type, Integer timeout) {
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.mode = mode;
		this.type = type;
		this.timeout = timeout;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((mode == null) ? 0 : mode.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((port == null) ? 0 : port.hashCode());
		result = prime * result + ((timeout == null) ? 0 : timeout.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FrontendInfo other = (FrontendInfo) obj;
		if (ip == null) {
			if (other.ip != null) {
				return false;
			}
		} else if (!ip.equals(other.ip)) {
			return false;
		}
		if (mode != other.mode) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (port == null) {
			if (other.port != null) {
				return false;
			}
		} else if (!port.equals(other.port)) {
			return false;
		}
		if (timeout == null) {
			if (other.timeout != null) {
				return false;
			}
		} else if (!timeout.equals(other.timeout)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "FrontendInfo [name=" + name + ", ip=" + ip + ", port=" + port + ", mode=" + mode + ", type=" + type
				+ ", timeout=" + timeout + "]";
	}
	
}

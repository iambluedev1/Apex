package fr.iambluedev.vulkan;

import fr.iambluedev.vulkan.config.ApexConfig;
import fr.iambluedev.vulkan.config.WhiteListConfig;
import fr.iambluedev.vulkan.state.ListeningState;

public class Vulkan {

	private static Vulkan instance;
	private ListeningState listeningState;
	private WhiteListConfig whiteListConfig;
	private ApexConfig apexConfig;
	
	public Vulkan(){
		instance = this;
		this.listeningState = ListeningState.OPEN;
		this.whiteListConfig = new WhiteListConfig();
		this.apexConfig = new ApexConfig();
	}

	public ListeningState getListeningState() {
		return this.listeningState;
	}

	public void setListeningState(ListeningState listeningState) {
		this.listeningState = listeningState;
	}

	public static Vulkan getInstance() {
		return instance;
	}

	public WhiteListConfig getWhiteListConfig() {
		return this.whiteListConfig;
	}

	public ApexConfig getApexConfig() {
		return this.apexConfig;
	}
}

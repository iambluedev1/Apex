package fr.iambluedev.vulkan;

import fr.iambluedev.vulkan.state.ListeningState;

public class Vulkan {

	private ListeningState listeningState;
	private static Vulkan instance;
	
	public Vulkan(){
		instance = this;
		this.setListeningState(ListeningState.OPEN);
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
}

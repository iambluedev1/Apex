package fr.iambluedev.vulkan.command;

import de.jackwhite20.apex.Apex;
import de.jackwhite20.apex.command.Command;
import fr.iambluedev.vulkan.Vulkan;
import fr.iambluedev.vulkan.state.ListeningState;

public class CloseCommand extends Command{
	
	public CloseCommand(String name, String description, String... aliases) {
		super(name, description, aliases);
	}

	@Override
	public boolean execute(String[] args) {
		Vulkan.getInstance().setListeningState(ListeningState.CLOSE);
		Apex.getLogger().info("The default port is now closed !");
		return true;
	}

}

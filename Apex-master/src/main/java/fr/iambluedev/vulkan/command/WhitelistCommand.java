package fr.iambluedev.vulkan.command;

import de.jackwhite20.apex.command.Command;

public class WhitelistCommand extends Command{

	public WhitelistCommand(String name, String description, String[] aliases) {
		super(name, description, aliases);
	}

	@Override
	public boolean execute(String[] args) {
		return true;
	}

}

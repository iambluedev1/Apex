package fr.iambluedev.vulkan.command;

import de.jackwhite20.apex.Apex;
import de.jackwhite20.apex.command.Command;
import fr.iambluedev.vulkan.Vulkan;
import fr.iambluedev.vulkan.state.WhitelistState;

public class WhitelistCommand extends Command{

	public WhitelistCommand(String name, String description, String... aliases) {
		super(name, description, aliases);
	}

	@Override
	public boolean execute(String[] args) {
		if(args.length == 0){
			Apex.getLogger().warn("Please specify arguments !");
			return false;
		}
		
		if(args.length >= 1 && args.length <= 2){
			if(args[0].equalsIgnoreCase("on")){
				Vulkan.getInstance().setWhitelistState(WhitelistState.ON);
				Apex.getLogger().info("Turning on whitelisting !");
			}else if(args[0].equalsIgnoreCase("off")){
				Vulkan.getInstance().setWhitelistState(WhitelistState.OFF);
				Apex.getLogger().info("Turning off whitelisting !");
			}else if(args[0].equalsIgnoreCase("add")){
				
			}else if(args[0].equalsIgnoreCase("remove")){
				
			}else if(args[0].equalsIgnoreCase("status")){
				Apex.getLogger().info("Status : " + Vulkan.getInstance().getWhitelistState());
			}else{
				Apex.getLogger().warn("Please specify valid arguments ! (max 2)");	
			}
		}else{
			Apex.getLogger().warn("Please specify valid arguments ! (max 2)");
			return false;
		}
		return true;
	}

}

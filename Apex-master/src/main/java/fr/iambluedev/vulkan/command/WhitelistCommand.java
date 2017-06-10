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
				if(args.length == 2){
					Vulkan.getInstance().addIp(args[1]);
					Apex.getLogger().info(args[1] + " added to the whitelist !");
				}else{
					Apex.getLogger().warn("Please specify valid arguments ! (max 2)");	
				}
			}else if(args[0].equalsIgnoreCase("remove")){
				if(args.length == 2){
					Vulkan.getInstance().removeIp(args[1]);
					Apex.getLogger().info(args[1] + " removed from the whitelist !");
				}else{
					Apex.getLogger().warn("Please specify valid arguments ! (max 2)");	
				}
			}else if(args[0].equalsIgnoreCase("status")){
				Apex.getLogger().info("Status : " + Vulkan.getInstance().getWhitelistState());
			}else if(args[0].equalsIgnoreCase("list")){
				Apex.getLogger().info("List : " + Vulkan.getInstance().getWhitelistedIp().toString());
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

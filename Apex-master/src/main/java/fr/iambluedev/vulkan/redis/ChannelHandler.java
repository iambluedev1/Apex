package fr.iambluedev.vulkan.redis;

import java.util.Arrays;

import de.jackwhite20.apex.Apex;
import de.jackwhite20.apex.command.Command;
import fr.iambluedev.spartan.api.gson.JSONObject;
import fr.iambluedev.spartan.api.gson.parser.JSONParser;
import fr.iambluedev.spartan.api.gson.parser.ParseException;
import fr.iambluedev.spartan.utils.Callback;
import fr.iambluedev.spartan.utils.RedisJsonMessage;
import fr.iambluedev.vulkan.Vulkan;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class ChannelHandler extends JedisPubSub {
	
	@Override
	public void onMessage(String channel, String message) {
		System.out.println(channel + " " + message);
		if(channel.equals("apex")){
			JSONObject jsonObj = null;
			try {
				jsonObj = (JSONObject) new JSONParser().parse(message);
			} catch (ParseException e) {}
			String cmd = (String) jsonObj.get("cmd");
			String content = (String) jsonObj.get("content");
			String player = "";
			if(cmd.contains(":")){
				String[] parser = cmd.split(":");
				cmd = parser[0];
				player = parser[1];
			}
			final String name = player;
			if(cmd.equals("cmd")){
				if(content.equals("whitelist on") || content.equals("whitelist off")){
					Command command = Apex.getCommandManager().findCommand(content);
                	Apex.getLogger().info("Player " + player + " executed command : " + content);
                    String[] cmdArgs = Arrays.copyOfRange(content.split(" "), 1, content.split(" ").length);
                    command.execute(cmdArgs);
                    new Thread(new Runnable(){
						@Override
						public void run() {
							Vulkan.getInstance().getRedis().get(new Callback<Jedis>() {
								@Override
								public void call(Jedis jedis) {
									jedis.publish("apex", new RedisJsonMessage().setCmd("response:" + name).setContent("Command executed successfuly !").get());
								}
							});
						}
					}).start();
				}else if(content.equals("whitelist list")){
					
				}else if(content.contains("whitelist add")){
					
				}else if(content.contains("whitelist remove")){
					
				}else if(content.equals("whitelist status")){
					
				}else{
					new Thread(new Runnable(){
						@Override
						public void run() {
							Vulkan.getInstance().getRedis().get(new Callback<Jedis>() {
								@Override
								public void call(Jedis jedis) {
									jedis.publish("apex", new RedisJsonMessage().setCmd("response:" + name).setContent("Command not found !").get());
								}
							});
						}
					}).start();
				}
			}
		}
	}
}

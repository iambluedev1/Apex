package fr.iambluedev.vulkan.redis;

import de.jackwhite20.cobra.shared.http.Response;
import fr.iambluedev.spartan.api.gson.JSONObject;
import fr.iambluedev.spartan.api.gson.parser.JSONParser;
import fr.iambluedev.spartan.api.gson.parser.ParseException;
import fr.iambluedev.spartan.utils.Callback;
import fr.iambluedev.spartan.utils.RedisJsonMessage;
import fr.iambluedev.vulkan.Vulkan;
import fr.iambluedev.vulkan.rest.VulkanResource;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class ChannelHandler extends JedisPubSub {
	
	@Override
	public void onMessage(String channel, String message) {
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
				if(content.equals("whitelist on") || content.equals("whitelist off") || content.equals("w on") || content.equals("w off")){
					String split[] = content.split(" ");
					Response resp = new VulkanResource().state(split[1]);
                    new Thread(new Runnable(){
						@Override
						public void run() {
							Vulkan.getInstance().getRedis().get(new Callback<Jedis>() {
								@Override
								public void call(Jedis jedis) {
									jedis.publish("apex", new RedisJsonMessage().setCmd("response:" + name).setContent(resp.body().content()).get());
								}
							});
						}
					}).start();
				}else if(content.equals("whitelist list") || content.equals("w list")){
					Response resp = new VulkanResource().list();
                    new Thread(new Runnable(){
						@Override
						public void run() {
							Vulkan.getInstance().getRedis().get(new Callback<Jedis>() {
								@Override
								public void call(Jedis jedis) {
									jedis.publish("apex", new RedisJsonMessage().setCmd("response:" + name).setContent(resp.body().content()).get());
								}
							});
						}
					}).start();
				}else if(content.contains("whitelist add") || content.contains("w add")){
					String split[] = content.split(" ");
					Response resp = new VulkanResource().add(split[2]);
					new Thread(new Runnable(){
						@Override
						public void run() {
							Vulkan.getInstance().getRedis().get(new Callback<Jedis>() {
								@Override
								public void call(Jedis jedis) {
									jedis.publish("apex", new RedisJsonMessage().setCmd("response:" + name).setContent(resp.body().content()).get());
								}
							});
						}
					}).start();					
				}else if(content.contains("whitelist remove") || content.contains("w remove")){
					String split[] = content.split(" ");
					Response resp = new VulkanResource().remove(split[2]);
					new Thread(new Runnable(){
						@Override
						public void run() {
							Vulkan.getInstance().getRedis().get(new Callback<Jedis>() {
								@Override
								public void call(Jedis jedis) {
									jedis.publish("apex", new RedisJsonMessage().setCmd("response:" + name).setContent(resp.body().content()).get());
								}
							});
						}
					}).start();
				}else if(content.equals("whitelist status") || content.equals("w status")){
					Response resp = new VulkanResource().status();
                    new Thread(new Runnable(){
						@Override
						public void run() {
							Vulkan.getInstance().getRedis().get(new Callback<Jedis>() {
								@Override
								public void call(Jedis jedis) {
									jedis.publish("apex", new RedisJsonMessage().setCmd("response:" + name).setContent(resp.body().content()).get());
								}
							});
						}
					}).start();
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

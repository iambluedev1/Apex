package fr.iambluedev.vulkan.redis;

import com.google.gson.Gson;

import de.jackwhite20.apex.Apex;
import de.jackwhite20.apex.rest.response.ApexResponse;
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
				Apex.getLogger().info("Received command from " + player + " : " + content);
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
				}else if(content.equals("close") || content.equals("c")){
					Response resp = new VulkanResource().close();
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
				}else if(content.equals("pstatus") || content.equals("ps")){
					Response resp = new VulkanResource().pstatus();
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
				}else if(content.equals("open") || content.equals("o")){
					Response resp = new VulkanResource().open();
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
									jedis.publish("apex", new RedisJsonMessage().setCmd("response:" + name).setContent(Response.ok().content(new Gson().toJson(new ApexResponse(ApexResponse.Status.OK, "Command not found !"))).build().body().content()).get());
								}
							});
						}
					}).start();
				}
			}
		}
		if(channel.equalsIgnoreCase("node")){
			JSONObject jsonObj = null;
			try {
				jsonObj = (JSONObject) new JSONParser().parse(message);
			} catch (ParseException e) {}
			String id = (String) jsonObj.get("id");
			String cmd = (String) jsonObj.get("cmd");
			String content = (String) jsonObj.get("content");
			
			if(cmd.equals("hasEnoughtResponse")){
				Apex.getLogger().info("Received hasEnoughtResponse from node : " + id);
			}
		}
	}
}

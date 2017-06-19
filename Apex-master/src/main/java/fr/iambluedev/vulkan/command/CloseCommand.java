package fr.iambluedev.vulkan.command;

import de.jackwhite20.apex.Apex;
import de.jackwhite20.apex.command.Command;
import fr.iambluedev.spartan.utils.Callback;
import fr.iambluedev.spartan.utils.RedisJsonMessage;
import fr.iambluedev.vulkan.Vulkan;
import fr.iambluedev.vulkan.state.ListeningState;
import redis.clients.jedis.Jedis;

public class CloseCommand extends Command{
	
	public CloseCommand(String name, String description, String... aliases) {
		super(name, description, aliases);
	}

	@Override
	public boolean execute(String[] args) {
		Vulkan.getInstance().setListeningState(ListeningState.CLOSE);
		Apex.getLogger().info("The default port is now closed !");
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				Vulkan.getInstance().getRedis().get(new Callback<Jedis>() {
					@Override
					public void call(Jedis jedis) {
						jedis.publish("node", new RedisJsonMessage().setCmd("hasEnoughtForGm").setContent("hg111k").get());
					}
				});
			}
		}).start();
		return true;
	}

}

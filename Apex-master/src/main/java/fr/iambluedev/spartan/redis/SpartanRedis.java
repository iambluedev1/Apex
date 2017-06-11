package fr.iambluedev.spartan.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import fr.iambluedev.spartan.utils.Callback;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

public abstract class SpartanRedis {

	private String host;
	private Integer port;
	private String password;
	private JedisPool jedis;
	
	public SpartanRedis(String host, String password, Integer port){
		this.host = host;
		this.port = port;
		this.password = password;
		this.jedis = new JedisPool(new GenericObjectPoolConfig(), this.host, this.port, 500, this.password);
	}
	
	public SpartanRedis(String host, Integer port){
		this.host = host;
		this.port = port;
		this.jedis = new JedisPool(new GenericObjectPoolConfig(), this.host, this.port, 500);
	}

	public String getHost() {
		return this.host;
	}

	public Integer getPort() {
		return this.port;
	}

	public String getPassword() {
		return this.password;
	}

	public JedisPool getJedis() {
		return this.jedis;
	}
	
	public void get(Callback<Jedis> callback){
		try {
            Jedis jedis = this.jedis.getResource();
            callback.call(jedis);
            jedis.close();
        }catch (JedisConnectionException e) {
            e.printStackTrace();
        }
	}
	
	public void close(){
		this.jedis.destroy();
	}
}

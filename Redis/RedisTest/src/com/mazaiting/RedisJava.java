package com.mazaiting;

import redis.clients.jedis.Jedis;

public class RedisJava {
	public static void main(String[] args) {
		Jedis jedis = new Jedis("127.0.0.1", 6379);
		System.out.println("服务正在运行： " + jedis.ping());
		jedis.set("name", "linghaoyu");
		System.out.println(jedis.get("name"));;
		jedis.close();
	}
}

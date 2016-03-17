package com.ss.redis;

import redis.clients.jedis.ShardedJedis;

public interface RedisClient {
	public abstract ShardedJedis getRedisClient();

	public void returnResource(ShardedJedis shardedJedis);

	public void returnResource(ShardedJedis shardedJedis, boolean broken);
}

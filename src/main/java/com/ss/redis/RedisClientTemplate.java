package com.ss.redis;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ss.httpsession.RedisHttpSession;
import com.ss.httpsession.SeesionSerializer;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Repository
public class RedisClientTemplate implements Serializable{
	private static final Log log = LogFactory.getLog(RedisClientTemplate.class);
	@Autowired
	private ShardedJedisPool shardedJedisPool;

	public void disconnect() {
		ShardedJedis shardedJedis = shardedJedisPool.getResource();
		shardedJedis.disconnect();
	}

	public <T> T execute(RedisCallBack<T> callback) {
		ShardedJedis jedis = null;
		boolean broken = false;
		try {

			jedis = shardedJedisPool.getResource();
			return callback.call(jedis);
		} catch (Exception e) {
			broken = true;
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				if (broken) {
					shardedJedisPool.returnBrokenResource(jedis);
				} else {
					shardedJedisPool.returnResource(jedis);
				}
			}
		}
		return null;
	}

	public String set(final String key, final String value) {
		return this.execute(new RedisCallBack<String>() {
			@Override
			public String call(ShardedJedis jedis) {
				String result = jedis.set(key, value);
				return result;
			}

		});
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public String get(final String key) {
		return this.execute(new RedisCallBack<String>() {
			@Override
			public String call(ShardedJedis jedis) {
				return jedis.get(key);
			}
		});
	}

	public String putSession(final String sessionKey, final RedisHttpSession session, final int timeOut) {
		return this.execute(new RedisCallBack<String>() {
			@Override
			public String call(ShardedJedis jedis) {
				return jedis.setex(sessionKey.getBytes(), timeOut, SeesionSerializer.serialize(session));
			}
		});
	}

	public String setex(final String key, final String value) {
		return this.execute(new RedisCallBack<String>() {
			@Override
			public String call(ShardedJedis jedis) {
				return jedis.setex(key.getBytes(), 1, value.getBytes());
			}
		});
	}
	
	public RedisHttpSession getSession(final String sessionKey) {
		return this.execute(new RedisCallBack<RedisHttpSession>() {
			@Override
			public RedisHttpSession call(ShardedJedis jedis) {
				byte[] sessionByte = jedis.get(sessionKey.getBytes());
				return SeesionSerializer.deserialize(sessionByte);
			}
		});
	}

	public void deleteSession(final String sessionKey) {
		this.execute(new RedisCallBack<String>() {

			@Override
			public String call(ShardedJedis jedis) {
				jedis.del(sessionKey);
				return null;
			}
		});
	}
}
package com.xmsmartcity.maintain.service;

import java.io.IOException;
import java.util.Set;
import org.mybatis.caches.redis.SerializeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * redis 缓存服务帮助。
 * @author chenbinyi
 *
 */

@Service
public class RedisCacheService {

	@Autowired
	private JedisPool jedisPool;

	public void putObject(String key, Object object) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(key.getBytes(), SerializeUtil.serialize(object));
		} catch (Exception ex) {
			handleException(ex, jedisPool, jedis);
		} finally {
			if (jedis != null && jedis.isConnected()) {
				jedis.close();
			}
		}
	}

	public void putObject(String key, Object object, int expirationInSeconds) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.setex(key.getBytes(), expirationInSeconds, SerializeUtil.serialize(object));
		} catch (Exception ex) {
			handleException(ex, jedisPool, jedis);
		} finally {
			if (jedis != null && jedis.isConnected()) {
				jedis.close();
			}
		}
	}

	public Object getObject(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return (Object) SerializeUtil.unserialize(jedis.get(key.getBytes()));
		} catch (Exception ex) {
			handleException(ex, jedisPool, jedis);
		} finally {
			if (jedis != null && jedis.isConnected()) {
				jedis.close();
			}
		}
		return null;
	}

	public void removeObject(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.del(key.getBytes());
		} catch (Exception ex) {
			handleException(ex, jedisPool, jedis);
		} finally {
			if (jedis != null && jedis.isConnected()) {
				jedis.close();
			}
		}
	}

	public boolean exists(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.exists(key.getBytes());
		} catch (Exception ex) {
			handleException(ex, jedisPool, jedis);
		} finally {
			if (jedis != null && jedis.isConnected()) {
				jedis.close();
			}
		}
		return false;
	}

	public long expire(String key, int expirationInSeconds) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.expire(key.getBytes(), expirationInSeconds);
		} catch (Exception ex) {
			handleException(ex, jedisPool, jedis);
		} finally {
			if (jedis != null && jedis.isConnected()) {
				jedis.close();
			}
		}
		return 0L;
	}

	public Set<String> keys(final String pattern) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.keys(pattern);
		} catch (Exception ex) {
			handleException(ex, jedisPool, jedis);
		} finally {
			if (jedis != null && jedis.isConnected()) {
				jedis.close();
			}
		}
		return null;
	}

	/**
	 * 运行时异常，IO异常，销毁jedis对象
	 *
	 * @param ex
	 * @param jedisPool
	 * @param jedis
	 */
	private void handleException(Exception ex, JedisPool jedisPool, Jedis jedis) {
		if (jedis == null) {
			Assert.state(false,"服务连接失败，请联系管理员");
		}
		if (ex instanceof IOException) {
			jedis.close();
		}
	}

}

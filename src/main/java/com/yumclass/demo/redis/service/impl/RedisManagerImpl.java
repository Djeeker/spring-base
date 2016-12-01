package com.yumclass.demo.redis.service.impl;

import com.yumclass.demo.redis.service.RedisManager;
import com.yunclass.demo.util.SerializeUtil;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 项目名称：mini
 *
 * @Description：
 * @Date：2016年10月10日 17:35
 * @Author kongjun
 * @Version 1.0
 */
@Slf4j
@Service
public class RedisManagerImpl<T> implements RedisManager<T> {

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private RedisTemplate<String,T> redisTemplate;

    @Override
    public void save(String key, String field, T object) {
        this.save(key,field,object,7200);
    }

    @Override
    public void save(final String key,final String field,T object, final long expire) {

        final byte[] vbytes = SerializeUtil.serialize(object);
        redisTemplate.execute(new RedisCallback<T>() {
            @Override
            public T doInRedis(RedisConnection connection)
                    throws DataAccessException {
                byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
                connection.hSet(keyBytes,
                        redisTemplate.getStringSerializer().serialize(field),vbytes);
                connection.expire(keyBytes,expire);
                return null;
            }
        });
    }

    @Override
    public Boolean delete(final String key,final String field) {

        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                return connection.hDel(redisTemplate.getStringSerializer().serialize(key),
                        redisTemplate.getStringSerializer().serialize(field)) > 0;
            }
        });
    }

    @Override
    public <V> V get(final String key, final String field, Class<V> clazz) {

        return redisTemplate.execute(new RedisCallback<V>() {
            @Override
            public V doInRedis(RedisConnection connection)
                    throws DataAccessException {
                byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
                byte[] fieldBytes = redisTemplate.getStringSerializer().serialize(field);
                if (connection.hExists(keyBytes,fieldBytes)) {
                    byte[] valuebytes = connection.hGet(keyBytes,fieldBytes);
                    return (V) SerializeUtil.unserialize(valuebytes);
                }
                return null;
            }
        });
    }

    @Override
    public String tryRedisLock(String lockKey,long timeout, TimeUnit unit) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            while (true) {

                String lockVal = String.valueOf(System.currentTimeMillis() + 1);
                String luaScript = "local lock = tonumber(redis.call('SETNX', KEYS[1],ARGV[1]))"
                        + "\nredis.call('PEXPIRE',KEYS[1],ARGV[2])"
                        + "\nreturn lock";
                List<String> keys = new ArrayList<>(1);
                keys.add(lockKey);
                List<String> args = new ArrayList<>(2);
                args.add(lockVal);
                args.add(String.valueOf(unit.toSeconds(timeout)));
                Long result = Long.valueOf(String.valueOf(jedis.eval(luaScript, keys, args)));
                //获得锁
                if (new Long(1).equals(result)) {
                    return lockVal;
                }

                //等待重试
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            log.error("获取锁异常",e);
        } finally {
            if(null != jedis) jedis.close();
        }

        return null;
    }

    @Override
    public void unLockRedis(String lockKey,String lockVal) {

        if(StringUtils.isBlank(lockKey)) return;

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String luaScript = "local val = redis.call('GET', KEYS[1])"
                    + "\nif ARGV[1] == val then"
                    + "\nredis.call('DEL',KEYS[1])"
                    + "\nend";
            List<String> keys = new ArrayList<>(1);
            keys.add(lockKey);
            List<String> args = new ArrayList<>(1);
            args.add(lockVal);
            jedis.eval(luaScript,keys,args);

        } catch (Exception e) {
            log.error("解锁异常",e);
        } finally {
            if(null != jedis) jedis.close();
        }
    }
}

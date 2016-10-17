package com.yumclass.demo.redis.service.impl;

import com.mini.common.constant.DmsConstant;
import com.mini.dms.biz.redis.service.RedisManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@Service("dmsRedisManager")
public class RedisManagerImpl implements RedisManager {

    @Autowired
    private JedisPool jedisPool;

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
                Thread.sleep(DmsConstant.REDIS_GET_LOCK_RETRY_WAIT);
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

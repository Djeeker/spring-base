package com.yumclass.demo.redis.service;

import java.util.concurrent.TimeUnit;

/**
 * 项目名称：mini
 *
 * @Description：
 * @Date：2016年10月10日 17:32
 * @Author kongjun
 * @Version 1.0
 */
public interface RedisManager<T> {

    /**
     * 获取锁:
     * <p>锁的时间如果超时会删除锁,在之后可以不用再次调用<code>unLockRedis()</code></p>
     * <p>如果锁没有超时,获取锁的时间也没有超时,会继续等待</p>
     *
     * @param lockKey
     * @param timeout
     * @param unit
     * @return
     */
    String tryRedisLock(String lockKey, long timeout, TimeUnit unit);

    /**
     * 释放锁
     * @param lockKey
     */
    void unLockRedis(String lockKey, String lockVal);

    /**
     * hset
     * @param key
     * @param field
     * @param object
     */
    void save(String key,String field,T object);

    /**
     * hset
     * @param key
     * @param field
     * @param object
     * @param expire
     */
    void save(String key,String field,T object,long expire);

    /**
     * 删除
     * @param key
     * @param field
     * @return
     */
    Boolean delete(String key,String field);

    /**
     * hget
     * @param key
     * @param field
     * @param clazz
     * @param <V>
     * @return
     */
    <V> V get(String key,String field,Class<V> clazz);
}

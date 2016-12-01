package com.yunclass.demo.redis.lock;


import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.yumclass.demo.redis.config.RedisLock;
import com.yumclass.demo.redis.service.RedisManager;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 项目名称：spring-base
 *
 * @Description：
 * @Date：2016年10月15日 13:28
 * @Author kongjun
 * @Version 1.0
 */
@Slf4j
@Component
@Aspect
public class RedisLockAspect {

    private final static long REDIS_LOCK_TIME_OUT = 5l;//锁的超时时间 秒单位

    @Qualifier("dmsRedisManager")
    @Autowired
    private RedisManager redisManager;

    @Around("@annotation(com.mini.common.config.RedisLock)")
    public Object tryGetRedisLock(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RedisLock redisLock = method.getAnnotation(RedisLock.class);

        //获取锁的方法加入cat监控
        Transaction transaction = null;
        try {
            transaction = Cat.newTransaction("Redis.lock", "redis锁");
        } catch (Exception e) {
            log.error("cat异常", e);
        }

        if(transaction != null) transaction.setStatus(Transaction.SUCCESS);

        String lockVal = redisManager.tryRedisLock(redisLock.lockey(), REDIS_LOCK_TIME_OUT, TimeUnit.SECONDS);
        Cat.logEvent("Redis.lock.isGet",new StringBuffer(redisLock.lockey()).append(":").append(lockVal).toString());
        if(transaction != null) transaction.complete();

        Object resultVal = joinPoint.proceed();
        redisManager.unLockRedis(redisLock.lockey(),lockVal);
        return resultVal;
    }


}

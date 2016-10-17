package com.yumclass.demo.redis.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * 项目名称：mini
 *
 * @Description：
 * @Date：2016年10月14日 13:51
 * @Author kongjun
 * @Version 1.0
 */
@Target({ TYPE, METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {

    /**
     * 锁
     * @return
     */
    String lockey() default "";

}

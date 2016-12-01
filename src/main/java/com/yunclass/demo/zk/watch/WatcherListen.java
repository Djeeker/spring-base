package com.yunclass.demo.zk.watch;

/**
 * 项目名称：spring-base
 *
 * @Description：
 * @Date：2016年09月23日 16:47
 * @Author kongjun
 * @Version 1.0
 */
public interface WatcherListen {

    /**
     * 监听事件之后要做的事情
     */
    void watcherDo(byte[] nodeData);

}

package com.yunclass.demo.zk.service;


import com.yunclass.demo.zk.watch.WatcherListen;

/**
 * 项目名称：mini
 *
 * @Description：
 * @Date：2016年09月23日 16:09
 * @Author kongjun
 * @Version 1.0
 */
public interface ClientService {

    /**
     * 添加监听
     * @param path
     * @param watcherListen
     * @throws Exception
     */
    void addWatcher(String path, final WatcherListen watcherListen) throws Exception;

    /**
     * 为节点设置值,存在就更新,不存在就创建
     * @param path
     * @param data
     * @throws Exception
     */
    void setData(String path, byte[] data) throws Exception;

    /**
     * 判断路径是否不存在
     *
     * @param path
     **/
    boolean checkIsNotExist(String path) throws Exception;

    /**
     * 获取数据
     * @param path
     * @return
     */
    byte[] getNodeData(String path) throws Exception;

}

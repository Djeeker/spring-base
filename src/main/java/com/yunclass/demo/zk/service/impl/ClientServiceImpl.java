package com.yunclass.demo.zk.service.impl;

import com.yunclass.demo.zk.service.ClientService;
import com.yunclass.demo.zk.watch.WatcherListen;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 项目名称：mini
 *
 * @Description：
 * @Date：2016年09月23日 16:09
 * @Author kongjun
 * @Version 1.0
 */
@Slf4j
@Service
public class ClientServiceImpl implements ClientService, InitializingBean {

    @Qualifier("zkAddress")
    @Autowired
    private String zkAddress;
    private CuratorFramework client;

    @Override
    public void addWatcher(String path, final WatcherListen watcherListen) throws Exception {

        //监控节点的变化:连接 目录 是否压缩
        final NodeCache nodeCache = new NodeCache(client, path, false);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                log.info("zk监听路径:{},节点发生的状态:{}", nodeCache.getCurrentData().getPath(), nodeCache.getCurrentData().getStat());
                watcherListen.watcherDo(nodeCache.getCurrentData().getData());
            }
        });

        nodeCache.start();
    }

    @Override
    public void setData(String path, byte[] data) throws Exception {

        if(this.checkIsNotExist(path)){
            ceateNode(path,data);
            return;
        }

        modifyData(path,data);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        try {
            client = CuratorFrameworkFactory.newClient(this.zkAddress, new RetryNTimes(10, 5000));
            client.start();
            log.info("zk连接成功!");

        } catch (Exception e) {
            log.error("zk连接失败!", e);
        }
    }

    /**
     * 判断路径是否不存在
     *
     * @param path
     **/
    @Override
    public boolean checkIsNotExist(String path) throws Exception {
        return client.checkExists().forPath(path) == null;
    }

    @Override
    public byte[] getNodeData(String path) throws Exception {
        return client.getData().forPath(path);
    }

    /**
     * 创建节点
     * @param path
     * @throws Exception
     */
    private void ceateNode(String path,byte[] data) throws Exception {
        client.create().forPath(path,data);
    }

    /**
     * 更新数据
     * @param path
     * @param data
     * @throws Exception
     */
    private void modifyData(String path,byte[] data) throws Exception {
        client.setData().forPath(path,data);
    }

}

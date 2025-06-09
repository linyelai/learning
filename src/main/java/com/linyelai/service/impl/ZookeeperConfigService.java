package com.linyelai.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZookeeperConfigService {

    @Autowired
    private CuratorFramework client;

    public void saveConfig(String path, String value) throws Exception {
        if (client.checkExists().forPath(path) == null) {
            client.create().creatingParentsIfNeeded().forPath(path, value.getBytes());
        } else {
            client.setData().forPath(path, value.getBytes());
        }
    }

    public String getConfig(String path) throws Exception {
        if (client.checkExists().forPath(path) != null) {
            byte[] bytes = client.getData().forPath(path);
            return new String(bytes);
        }
        return null;
    }

    public List<String> getChildren(String path) throws Exception {
        return client.getChildren().forPath(path);
    }

    // 设置监听器，当子节点变化时触发
    public void watchChildren(String path) throws Exception {
        client.getChildren()
                .usingWatcher(new CuratorWatcher() {
                    @Override
                    public void process(WatchedEvent event) throws Exception {
                        System.out.println("Children changed!");
                        // 重新获取子节点
                        List<String> newChildren = curatorClient.getChildren()
                                .usingWatcher(this)
                                .forPath(path);
                        System.out.println("New children: " + newChildren);
                    }
                })
                 .forPath(path);
    }
}
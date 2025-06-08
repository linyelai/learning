package com.linyelai.controller.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZookeeperConfig {

    @Value("${zookeeper.host}")
    private String zookeeperHost;

    @Value("${zookeeper.baseSleepTimeMs}")
    private int baseSleepTimeMs;

    @Value("${zookeeper.maxRetries}")
    private int maxRetries;

    @Bean(destroyMethod = "close")
    public CuratorFramework curatorFramework() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperHost, retryPolicy);
        client.start();
        return client;
    }
}
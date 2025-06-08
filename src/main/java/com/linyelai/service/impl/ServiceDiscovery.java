package com.linyelai.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ServiceDiscovery {

    @Autowired
    private CuratorFramework client;

    private ServiceDiscovery<InstanceDetails> serviceDiscovery;

    @PostConstruct
    public void init() throws Exception {
        String basePath = "/services";
        serviceDiscovery = ServiceDiscoveryBuilder.builder(InstanceDetails.class)
                .basePath(basePath)
                .client(client)
                .serializer(new JsonInstanceSerializer<>(InstanceDetails.class))
                .build();
        serviceDiscovery.start();
    }

    public void registerService(String serviceName, String serviceId, String uri) throws Exception {
        InstanceDetails details = new InstanceDetails(uri);
        ServiceInstance<InstanceDetails> instance = ServiceInstance.<InstanceDetails>builder()
                .name(serviceName)
                .id(serviceId)
                .address(uri)
                .payload(details)
                .build();
        serviceDiscovery.registerService(instance);
    }

    @PreDestroy
    public void close() throws Exception {
        serviceDiscovery.close();
    }
}

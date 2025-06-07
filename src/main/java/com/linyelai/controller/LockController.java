package com.linyelai.controller;

import com.linyelai.controller.response.Response;
import com.linyelai.service.impl.DistributedLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class LockController {

    @Autowired
    private DistributedLockService distributedLockService;

    @PostMapping("/lock")
    public Response lock(@RequestBody String lockKey) {
        Response response = new Response();
        distributedLockService.lock(lockKey);
        return  response;
    }

    /**
     * 释放锁
     * @param lockKey 锁的key
     */
    @PostMapping("/unlock")
    public Response   unlock(@RequestBody String  lockKey) throws InterruptedException {
        Response response = new Response();
        distributedLockService.unlock(lockKey);
        return response;
    }
}

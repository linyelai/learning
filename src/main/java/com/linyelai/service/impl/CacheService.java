package com.linyelai.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 设置缓存
    public void setCache(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    // 获取缓存
    public Object getCache(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 删除缓存
    public Boolean deleteCache(String key) {
        return redisTemplate.delete(key);
    }

    // 设置Hash缓存
    public void setHashCache(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    // 获取Hash缓存
    public Object getHashCache(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }
}

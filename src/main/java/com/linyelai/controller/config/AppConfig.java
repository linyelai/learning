package com.linyelai.controller.config;

import com.linyelai.util.SnowflakeIdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class AppConfig {


    @Bean
    public SnowflakeIdWorker snowflakeIdWorker(){

        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1,1);
        return snowflakeIdWorker;
    }



}

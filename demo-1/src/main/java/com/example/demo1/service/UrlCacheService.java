package com.example.demo1.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class UrlCacheService {

    private final StringRedisTemplate redisTemplate;

    public UrlCacheService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveUrl(String shortCode, String originalUrl){
        redisTemplate.opsForValue().set("url:"+ shortCode,
                originalUrl,
                Duration.ofHours(1)
        );
    }

    public String getUrl(String shortCode){

        return redisTemplate.opsForValue().get("url:" + shortCode);
    }

    public void deleteUrl(String shortCode){
        redisTemplate.delete("url:" + shortCode);
    }
}

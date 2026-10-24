package com.example.demo1.service;

import com.example.demo1.dto.UrlDoa;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
public class UrlService {

    private UrlDoa urlDoa;
    private StringRedisTemplate redisTemplate;
    private UrlCacheService cacheService;
    private String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static SecureRandom RANDOM = new SecureRandom();

    public UrlService(UrlDoa urlDoa, StringRedisTemplate redisTemplate, UrlCacheService cacheService) {
        this.urlDoa = urlDoa;
        this.redisTemplate = redisTemplate;
        this.cacheService = cacheService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String shortUrl(String originalUrl){

        if(originalUrl == null || StringUtil.isNullOrEmpty(originalUrl)){
            throw new RuntimeException("Please provide a valid URL");
        }

        String shortUrl = generateShortCode();

        urlDoa.saveUrl(originalUrl, shortUrl);
        cacheService.saveUrl(shortUrl, originalUrl);

        return shortUrl;

    }

    public String generateShortCode() {
        StringBuilder code = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }

        return code.toString();
    }

    public String getOriginalUrl(String shortUrl) {

        String originalUrl = cacheService.getUrl(shortUrl);

        if (originalUrl != null){
            return originalUrl;
        }
        originalUrl = urlDoa.findOriginalUrlByShortCode(shortUrl);

        if (originalUrl == null) {
            throw new IllegalArgumentException("Short URL not found");
        }
        cacheService.saveUrl(shortUrl, originalUrl);

        return cacheService.getUrl(shortUrl);
    }

    @Transactional
    public void deleteUrl(String shortCode) {
        if(shortCode == null || StringUtil.isNullOrEmpty(shortCode)){
            throw new RuntimeException("Enter a valid shortUrl to delete");
        }
        cacheService.deleteUrl(shortCode);
        urlDoa.deleteUrl(shortCode);

    }
}

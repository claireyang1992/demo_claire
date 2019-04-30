package com.cmit.codechain.busi.provider.config;


import com.google.common.cache.CacheBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager localCacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
/*
        //用户缓存一天
        GuavaCache ONE_DAY = new GuavaCache(
                "ONE_DAY",
                CacheBuilder
                        .newBuilder()
                        .expireAfterWrite(1, TimeUnit.DAYS)
                        .build()
        );

        simpleCacheManager.setCaches(Arrays.asList(ONE_DAY));*/
        return simpleCacheManager;
/*
        GuavaCacheManager cacheManager=new GuavaCacheManager();
        //所有缓存的名字
        List<String> cacheNames = new ArrayList();
        String cacheName="";
        cacheManager.setCacheSpecification("maximumSize=500,expireAfterWrite=30m");
        cacheManager.setCacheNames(cacheNames);
        Cache cache=cacheManager.getCache(cacheName);
        cache.put(cacheName,"");
        return cacheManager;
*/
    }
}

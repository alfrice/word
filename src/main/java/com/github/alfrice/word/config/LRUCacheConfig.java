package com.github.alfrice.word.config;

import com.github.alfrice.word.cache.LRUCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by alice.martin
 * Developer: alice.martin
 * Date: 12/8/18
 * Time: 12:56 PM
 * Description: LRU cache to hold numbers > 999 that are most often used
 */
@Configuration
public class LRUCacheConfig {

    @Value("${cache.size}")
    private int cacheSize;

    @Bean
    LRUCache getLruCache(){
        return new LRUCache(cacheSize);
    }

}

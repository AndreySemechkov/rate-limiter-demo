package com.andrey.home.rateLimiter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class PropertiesConfig {

    @Value( "${app.threshold}" )
    String threshold;
    
    @Value( "${app.ttl}" )
    String ttl;

    @Bean
    public Properties properties() {
        return new Properties(threshold, ttl);
    }
}
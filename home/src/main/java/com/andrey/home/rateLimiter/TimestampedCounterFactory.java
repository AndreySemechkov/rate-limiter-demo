package com.andrey.home.rateLimiter;

import org.springframework.stereotype.Component;

@Component
public class TimestampedCounterFactory {
    
    public TimestampedCounter getTimestampedCounter(Long initialCount) {
        return new TimestampedCounterImpl(initialCount);
    }
}

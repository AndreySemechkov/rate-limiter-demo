package com.andrey.home.rateLimiter;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;


/** 
 * Each counter has a count and a timestamp when it was createdAt to reset it by TTL. 
 */
public class TimestampedCounterImpl implements TimestampedCounter {
  
    private AtomicLong counter;
    private AtomicLong createdAt;

    public TimestampedCounterImpl(Long initialCount) {
        counter = new AtomicLong(initialCount);
        createdAt = new AtomicLong(Instant.now().toEpochMilli());
    }

    public Long getCreatedAt() {
        return createdAt.get();
    }

    public Long get() {
        return counter.get();
    }

    public Long incrementAndGet() {
        return counter.incrementAndGet();
    }

    public void set(Long value) {
        counter.set(value);
    }
}
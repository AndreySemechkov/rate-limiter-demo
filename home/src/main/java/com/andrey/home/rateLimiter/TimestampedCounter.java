package com.andrey.home.rateLimiter;

public interface TimestampedCounter {
        
    /**
    * Returns the current value of the timestamp when the counter was last reset
    */
    public Long getCreatedAt();

    public Long get();

    public Long incrementAndGet();

    public void set(Long value);
}
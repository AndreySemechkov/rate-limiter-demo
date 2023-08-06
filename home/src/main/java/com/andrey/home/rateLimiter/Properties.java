package com.andrey.home.rateLimiter;

import lombok.Data;

@Data
public class Properties {
    Long threshold;
    Long ttl;

    public Properties(String threshold, String ttl) {
        this.threshold = Long.parseLong(threshold, 10);
        this.ttl = Long.parseLong(ttl, 10);
    }
}

package com.andrey.home.rateLimiter;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class UrlVisitsLimiterImpl implements UrlVisitsLimiter {

    private Map<Integer, TimestampedCounter> urlToCounter;
    private TimestampedCounterFactory timestampedCounterFactory;
    private	final Long ttl;
    private	final Long threshold;
    private	final Logger logger;
    private final static int CLEANUP_RATE_MS = 1000; 

    public UrlVisitsLimiterImpl(Logger logger, Properties properties, TimestampedCounterFactory timestampedCounterFactory) {
        this.logger = logger;
        this.ttl = properties.getTtl();
        this.threshold = properties.getThreshold();
        this.timestampedCounterFactory = timestampedCounterFactory;
        this.urlToCounter = new ConcurrentHashMap<>();
    }

    /**
     * Periodically removes expired URLs from the map using a seperate thread.
     **/
    @Scheduled(fixedRate=CLEANUP_RATE_MS)
    private void cleanUpExpiredUrls() {
        long nowTimestamp = Instant.now().toEpochMilli();
        urlToCounter.entrySet().removeIf(entry -> nowTimestamp - entry.getValue().getCreatedAt() > ttl);
    }

    /**
     * Counts the visits per URL, If there is no counter for the url, create one and either way increment it
     * param: url - the url to count and check
     * return: true if the threshold is exceeded and visit blocked
     **/
    private TimestampedCounter incrementAndGet(String url) {
        return urlToCounter.compute(
                            url.hashCode(), 
                            (key, value) -> {    
                                if (value == null) {
                                    // if there is no counter for the url, create one and increment it
                                    value = timestampedCounterFactory.getTimestampedCounter(0L);
                                }
                                value.incrementAndGet();
                                return value;
                            }
        );
    }

    /**
     * Checks if the rate limit visits threshold is exceeded
     */
    private Boolean isUrlBlocked(String url, TimestampedCounter counter) {
        if (counter == null) {
            // if counter TTL has expired, it will be null and the url is not blocked
            return false;
        }
        long count = counter.get();
        Boolean isBlocked = (count >= threshold);
        logger.info(String.format("URL %s is reported, has been requested %d times, %s blocked", url, count, isBlocked ? "is": "is not"));
        return isBlocked;
    }

    /**
     * Counts the visits per URL, returns true if the threshold is exceeded
     * param: url - the url to count and check
     * return: true if the threshold is exceeded and visit blocked
     **/
    @Override
    public Boolean incrementAndCheck(String url) {
        TimestampedCounter counter = incrementAndGet(url);
        return isUrlBlocked(url, counter);
    }

    @Override
    public Long get(String url) {
        return urlToCounter.get(url.hashCode()).get();
    }

    @Override
    public void put(String url, Long value){
        urlToCounter.put(url.hashCode(), timestampedCounterFactory.getTimestampedCounter(value));
    }
}

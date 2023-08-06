package com.andrey.home.rateLimiter;


public interface UrlVisitsLimiter {
    

    /**
     * Increments the visits counter for the given url and returns wheter this visit is blocked.
     * @param url
     * @return true if the visit is blocked, false otherwise
     */
    public Boolean incrementAndCheck(String url);

    public Long get(String url);

    public void put(String url, Long count);
}

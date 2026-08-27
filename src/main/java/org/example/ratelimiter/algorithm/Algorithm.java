package org.example.ratelimiter.algorithm;

public interface Algorithm {
    void setLimits(String userId, int windowSeconds, int maxRequests);

    boolean allowRequest(String userId, long timestamp);
}

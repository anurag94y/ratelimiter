package org.example.ratelimiter.algorithm;

import org.example.ratelimiter.entity.UserConfig;

import java.util.ArrayDeque;
import java.util.Deque;

public interface Algortihm {
    void setLimits(String userId, int windowSeconds, int maxRequests);

    boolean allowRequest(String userId, long timestamp);
}

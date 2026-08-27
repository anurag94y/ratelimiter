package org.example.ratelimiter.algorithm;

import org.example.ratelimiter.entity.UserConfig;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SlidingWindow implements Algorithm {

    Map<String, Deque<Long>> userRequestMap;
    Map<String, UserConfig> userConfigMap;

    public SlidingWindow() {
        this.userRequestMap = new ConcurrentHashMap<>();
        this.userConfigMap = new ConcurrentHashMap<>();
    }

    public void setLimits(String userId, int windowSeconds, int maxRequests) {
        userConfigMap.put(userId, new UserConfig(windowSeconds, maxRequests));
    }

    public boolean allowRequest(String userId, long timestamp) {
        UserConfig userConfig = userConfigMap.get(userId);
        if (userConfig == null) {
            return false;
        }

        synchronized (userConfig) {
            Deque<Long> requests = userRequestMap.computeIfAbsent(userId, v -> new ArrayDeque<>());

            long currentTimestamp = timestamp - userConfig.windowSeconds();
            while (!requests.isEmpty() && requests.peek() <= currentTimestamp) {
                requests.poll();
            }
            if (requests.size() >= userConfig.maxRequests()) {
                return false;
            }
            requests.add(timestamp);
            return true;
        }
    }
}

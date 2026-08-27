package org.example.ratelimiter.algorithm;

import org.example.ratelimiter.entity.UserConfig;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class SlidingWindow implements Algortihm {

    Map<String, Deque<Long>> userRequestMap ;
    Map<String, UserConfig> userConfigMap;

    public SlidingWindow() {
        this.userRequestMap = new HashMap<>();
        this.userConfigMap = new HashMap<>();
    }

    public void setLimits(String userId, int windowSeconds, int maxRequests) {
        userConfigMap.put(userId, new UserConfig(windowSeconds, maxRequests));
    }

    public boolean allowRequest(String userId, long timestamp) {
        UserConfig userConfig = userConfigMap.get(userId);
        if (userConfig == null) {
            return false;
        }
        Deque<Long> requests = userRequestMap.computeIfAbsent(userId, v -> new ArrayDeque<>());
        if (requests.isEmpty()) {
            requests.add(timestamp);
            userRequestMap.put(userId, requests);
            return true;
        } else {
            long currentTimestamp = timestamp - userConfig.windowSeconds();
            while (!requests.isEmpty() && requests.peek() <= currentTimestamp) {
                requests.poll();
            }
            if (requests.size() < userConfig.maxRequests()) {
                requests.add(timestamp);
                userRequestMap.put(userId, requests);
                return true;
            }
        }
        return false;
    }
}

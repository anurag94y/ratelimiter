package org.example.ratelimiter.algorithm;

import org.example.ratelimiter.entity.UserConfig;
import org.example.ratelimiter.entity.UserWindow;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FixedSizeWindow implements Algorithm {
    Map<String, UserWindow> userRequestMap ;
    Map<String, UserConfig> userConfigMap;
    int fixWindowSeconds;
    int fixMaxRequests;

    public FixedSizeWindow(int fixWindowSeconds, int fixMaxRequests) {
        this.userRequestMap = new ConcurrentHashMap<>();
        this.userConfigMap = new ConcurrentHashMap<>();
        this.fixWindowSeconds = fixWindowSeconds;
        this.fixMaxRequests = fixMaxRequests;
    }

    @Override
    public void setLimits(String userId, int windowSeconds, int maxRequests) {
        userConfigMap.put(userId, new UserConfig(fixWindowSeconds, fixMaxRequests));
    }

    @Override
    public boolean allowRequest(String userId, long timestamp) {
        UserConfig userConfig = userConfigMap.get(userId);
        if (userConfig == null) {
            return false;
        }

        synchronized (userConfig) {
            userRequestMap.computeIfAbsent(userId, v -> new UserWindow(new Long[fixWindowSeconds], new int[fixWindowSeconds]));
            UserWindow userWindow = userRequestMap.get(userId);
            int index = (int) timestamp % fixWindowSeconds;
            long expiredTimeStamp = timestamp - fixWindowSeconds;
            for (int i = 0; i < fixWindowSeconds; i++) {
                if (userWindow.getTimestamps()[i] != null && userWindow.getTimestamps()[i] <= expiredTimeStamp) {
                    userWindow.getTimestamps()[i] = null;
                    userWindow.setTotalRequests(userWindow.getTotalRequests() - userWindow.getRequests()[i]);
                    userWindow.getRequests()[i] = 0;
                }
            }
            int count = userWindow.getTotalRequests();
            if (count >= fixMaxRequests) {
                return false;
            }
            if (userWindow.getTimestamps()[index] != null && userWindow.getTimestamps()[index] == timestamp) {
                userWindow.getRequests()[index]++;
            } else {
                userWindow.getTimestamps()[index] = timestamp;
                userWindow.getRequests()[index] = 1;
            }
            userWindow.setTotalRequests(userWindow.getTotalRequests() + 1);
            return true;
        }
    }
}

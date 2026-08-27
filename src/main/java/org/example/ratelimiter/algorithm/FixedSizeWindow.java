package org.example.ratelimiter.algorithm;

import org.example.ratelimiter.entity.UserConfig;
import org.example.ratelimiter.entity.UserWindow;

import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class FixedSizeWindow implements Algortihm {
    Map<String, UserWindow> userRequestMap ;
    Map<String, UserConfig> userConfigMap;
    int fixWindowSeconds;
    int fixMaxRequests;

    public FixedSizeWindow(int fixWindowSeconds, int fixMaxRequests) {
        this.userRequestMap = new HashMap<>();
        this.userConfigMap = new HashMap<>();
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
        userRequestMap.computeIfAbsent(userId, v -> new UserWindow(new long[fixWindowSeconds], new int[fixWindowSeconds]));
        UserWindow userWindow = userRequestMap.get(userId);
        int index = (int) timestamp % fixWindowSeconds;
        long expiredTimeStamp = timestamp - fixWindowSeconds;
        for (int i = 0; i < fixWindowSeconds; i++) {
            if (userWindow.timestamps()[i] != -1 && userWindow.timestamps()[i] <= expiredTimeStamp) {
                userWindow.timestamps()[i] = -1;
                userWindow.requests()[index] = 0;
            }
        }
        int count = Arrays.stream(userWindow.requests()).sum();
        if (count >= fixMaxRequests) {
            return false;
        }
        if (userWindow.timestamps()[index] == timestamp) {
            userWindow.requests()[index]++;
        } else {
            userWindow.timestamps()[index] = timestamp;
            userWindow.requests()[index] = 1;
        }
        return true;
    }
}

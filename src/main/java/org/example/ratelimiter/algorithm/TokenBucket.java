package org.example.ratelimiter.algorithm;

import org.example.ratelimiter.entity.TokenConfig;
import org.example.ratelimiter.entity.UserConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenBucket implements Algorithm {
    Map<String, TokenConfig> tokenBucketMap;

    public TokenBucket() {
        this.tokenBucketMap = new ConcurrentHashMap<>();
    }

    @Override
    public void setLimits(String userId, int refillRate, int capacity) {
        tokenBucketMap.put(userId, new TokenConfig(capacity, 0, capacity, refillRate));
    }

    @Override
    public boolean allowRequest(String userId, long timestamp) {
        TokenConfig tokenConfig = tokenBucketMap.get(userId);
        if (tokenConfig == null) {
            return false;
        }
        synchronized (tokenConfig) {
            int elapsedTime = (int) (timestamp - tokenConfig.getLastTimestamp());
            int additionalToken = elapsedTime * tokenConfig.getRefillRate();
            if (additionalToken >= 0) {
                tokenConfig.setTokens(Math.min(tokenConfig.getCapacity(), tokenConfig.getTokens() + additionalToken));
                tokenConfig.setLastTimestamp(timestamp);
            }
            if (tokenConfig.getTokens() >= 1) {
                tokenConfig.setTokens(tokenConfig.getTokens() - 1);
                return true;
            }
            return false;
        }
    }
}

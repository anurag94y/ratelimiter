package org.example.ratelimiter.entity;

public class TokenConfig {

    private int tokens;
    private long lastTimestamp;
    private int capacity;
    private int refillRate;

    public TokenConfig(int tokens, long lastTimestamp, int capacity, int refillRate) {
        this.tokens = tokens;
        this.lastTimestamp = lastTimestamp;
        this.capacity = capacity;
        this.refillRate = refillRate;
    }

    public int getRefillRate() {
        return refillRate;
    }

    public long getLastTimestamp() {
        return lastTimestamp;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getTokens() {
        return tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public void setLastTimestamp(long lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }

}

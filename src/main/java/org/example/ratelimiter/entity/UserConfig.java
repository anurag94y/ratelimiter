package org.example.ratelimiter.entity;

public record UserConfig(int windowSeconds, int maxRequests) {
}

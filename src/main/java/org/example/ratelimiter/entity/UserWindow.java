package org.example.ratelimiter.entity;

public record UserWindow(long[] timestamps, int[] requests) {
}

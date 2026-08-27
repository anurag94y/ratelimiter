package org.example.ratelimiter.entity;

public class UserWindow {
    private Long[] timestamps;
    private int[] requests;
    private int totalRequests;
    public UserWindow(Long[] timestamps, int[] requests) {
        this.timestamps = timestamps;
        this.requests = requests;
        this.totalRequests = 0;
    }

    public Long[] getTimestamps() {
        return timestamps;
    }

    public int[] getRequests() {
        return requests;
    }

    public int getTotalRequests() {
        return totalRequests;
    }

    public void setTotalRequests(int totalRequests) {
        this.totalRequests = totalRequests;
    }

}

package org.example.ratelimiter;

import org.example.ratelimiter.algorithm.SlidingWindow;
import org.example.ratelimiter.entity.UserConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class RatelimiterApplication {
    private static SlidingWindow slidingWindow;

    public static void main(String[] args) {
        SpringApplication.run(RatelimiterApplication.class, args);
        slidingWindow = new SlidingWindow();
        slidingWindow.setLimits("U1", 60, 2);
        System.out.println(slidingWindow.allowRequest("U1", 1));
        System.out.println(slidingWindow.allowRequest("U1", 30));
        System.out.println(slidingWindow.allowRequest("U1", 50));
        System.out.println(slidingWindow.allowRequest("U1", 61));
    }

}

package org.example.ratelimiter;

import org.example.ratelimiter.algorithm.FixedSizeWindow;
import org.example.ratelimiter.algorithm.SlidingWindow;
import org.example.ratelimiter.entity.UserConfig;
import org.example.ratelimiter.entity.UserWindow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class RatelimiterApplication {
    private static SlidingWindow slidingWindow;
    private static FixedSizeWindow fixedSizeWindow;

    public static void main(String[] args) {
        SpringApplication.run(RatelimiterApplication.class, args);
        slidingWindow = new SlidingWindow();
        slidingWindow.setLimits("U1", 60, 2);
        System.out.println(slidingWindow.allowRequest("U1", 1));
        System.out.println(slidingWindow.allowRequest("U1", 30));
        System.out.println(slidingWindow.allowRequest("U1", 50));
        System.out.println(slidingWindow.allowRequest("U1", 61));
        fixedSizeWindow = new FixedSizeWindow(3, 2);
        fixedSizeWindow.setLimits("U1", 3, 2);
        for (int i = 0; i < 10; i++) {
            System.out.println("fixedSizeWindow:" + i + " " + fixedSizeWindow.allowRequest("U1", i));
        }
    }

}

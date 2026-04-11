package com.kt.leave;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LeaveSchedulerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeaveSchedulerApplication.class, args);
    }
}

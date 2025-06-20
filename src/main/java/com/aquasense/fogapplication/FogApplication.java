package com.aquasense.fogapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FogApplication {

    public static void main(String[] args) {
        SpringApplication.run(FogApplication.class, args);
    }

}

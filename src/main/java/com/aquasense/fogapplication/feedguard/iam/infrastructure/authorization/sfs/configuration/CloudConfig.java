package com.aquasense.fogapplication.feedguard.iam.infrastructure.authorization.sfs.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableRetry
public class CloudConfig {
    @Bean
    public RestTemplate resTemplate() {
        return new RestTemplate();
    }
}

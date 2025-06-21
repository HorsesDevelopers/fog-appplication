package com.aquasense.fogapplication.feedguard.data.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class CloudAuthService {
    private static final Logger log = LoggerFactory.getLogger(CloudAuthService.class);

    @Value("${cloud.backend.url}")
    private String cloudUrl;

    @Value("${cloud.backend.auth.username}")
    private String username;

    @Value("${cloud.backend.auth.password}")
    private String password;

    private String authToken;
    private final RestTemplate restTemplate = new RestTemplate();

    public String getAuthToken() {
        if (authToken == null) {
            authenticate();
        }
        return authToken;
    }

    private void authenticate() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> credentials = new HashMap<>();
            credentials.put("username", username);
            credentials.put("password", password);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(credentials, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    cloudUrl + "/api/v1/authentication/sign-in",
                    request,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                authToken = (String) response.getBody().get("token");
                log.info("Autenticación con backend exitosa");
            }
        } catch (Exception e) {
            log.error("Error autenticando con backend cloud", e);
        }
    }
}
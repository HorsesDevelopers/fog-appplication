package com.aquasense.fogapplication.feedguard.iam.infrastructure.authorization.sfs.services;

import com.aquasense.fogapplication.feedguard.om.interfaces.rest.resources.PondRecordDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CloudSyncService {
    @Value("${cloud.backend.url}")
    private String cloudUrl;

    private final RestTemplate restTemplate;
    private final CloudAuthService cloudAuthService;

    public CloudSyncService(RestTemplate restTemplate, CloudAuthService cloudAuthService) {
        this.restTemplate = restTemplate;
        this.cloudAuthService = cloudAuthService;
    }

    public void sendPondRecordToCloud(PondRecordDTO resource) {
        String endpoint = cloudUrl + "/api/v1/pond_records";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String token = cloudAuthService.getAuthToken();
            if (token != null && !token.isEmpty()) {
                headers.setBearerAuth(token);
            }
            HttpEntity<PondRecordDTO> request = new HttpEntity<>(resource, headers);
            restTemplate.postForEntity(endpoint, request, Void.class);
        } catch (Exception e) {
            // Loguea el error para depuración
            e.printStackTrace();
        }
    }
}

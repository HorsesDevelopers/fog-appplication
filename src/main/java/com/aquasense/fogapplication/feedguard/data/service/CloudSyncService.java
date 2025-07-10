package com.aquasense.fogapplication.feedguard.data.service;

import com.aquasense.fogapplication.feedguard.data.DeviceData;
import com.aquasense.fogapplication.feedguard.data.dto.CloudSensorDataDTO;
import com.aquasense.fogapplication.feedguard.data.service.CloudAuthService;


// import com.aquasense.fogapplication.feedguard.infrastructure.persistence.jpa.repositories.DeviceDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudSyncService {

    @Value("${cloud.backend.url}")
    private String cloudBackendUrl;

    @Value("${cloud.backend.sync.endpoint}")
    private String syncEndpoint;

    private final CloudAuthService cloudAuthService;
   // private final DeviceDataRepository deviceDataRepository;
    private final RestTemplate restTemplate;

    /*@Scheduled(fixedDelayString = "${cloud.backend.sync.interval}")
    public void syncWithCloud() {
        try {
            List<DeviceData> unsyncedData = deviceDataRepository.findByCloudSyncedFalse();

            if (unsyncedData.isEmpty()) {
                log.info("There is no new data to sync with the cloud");
                return;
            }

            // Transformar a DTOs para el cloud
            List<CloudSensorDataDTO> dataToSync = unsyncedData.stream()
                    .map(data -> new CloudSensorDataDTO(
                            data.getDeviceId(),  // Usar deviceId en vez de id
                            data.getType(),
                            data.getValue()
                    ))
                    .collect(Collectors.toList());

            // Registrar lo que se está enviando para depuración
            log.info("Sending {} logs to the cloud. First register: deviceId={}, type={}, value={}",
                    dataToSync.size(),
                    dataToSync.get(0).getDeviceId(),
                    dataToSync.get(0).getType(),
                    dataToSync.get(0).getValue());

            boolean success = sendDataToCloud(dataToSync);

            if (success) {
                // Marcar datos como sincronizados
                unsyncedData.forEach(data -> {
                    data.setCloudSynced(true);
                    deviceDataRepository.save(data);
                });
                log.info("Sincronizados {} registros con la nube", unsyncedData.size());
            }
        } catch (Exception e) {
            log.error("Error al sincronizar datos con la nube", e);
        }
    }*/

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000))
    private boolean sendDataToCloud(List<CloudSensorDataDTO> data) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String token = cloudAuthService.getAuthToken();
        if (token != null) {
            headers.setBearerAuth(token);
        } else {
            log.warn("No se pudo obtener token de autenticación");
            return false;
        }

        String url = cloudBackendUrl + syncEndpoint;
        HttpEntity<List<CloudSensorDataDTO>> entity = new HttpEntity<>(data, headers);

        try {
            log.debug("Enviando datos a: {}", url);
            // Cambiar Map.class por String.class para coincidir con la respuesta del backend
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Datos enviados correctamente al cloud");
                return true;
            } else {
                log.warn("Error al sincronizar: código {}", response.getStatusCode());
                return false;
            }
        } catch (Exception e) {
            log.error("Error en la comunicación con el cloud: {}", e.getMessage());
            return false;
        }
    }
}

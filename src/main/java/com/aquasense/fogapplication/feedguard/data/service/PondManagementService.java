package com.aquasense.fogapplication.feedguard.data.service;

import com.aquasense.fogapplication.feedguard.data.DeviceData;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.Base64;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class PondManagementService {
    private final DeviceCommunicationService communicationService;
    @Value("${cloud.backend.url}")
    private String cloudBackendUrl;

    @Value("${cloud.backend.auth.username}")
    private String username;

    @Value("${cloud.backend.auth.password}")
    private String password;

    @Value("${cloud.backend.auth.token}")
    private String token;

    private final Map<String, Map<String, DeviceData>> pondDeviceData = new ConcurrentHashMap<>();

    @PostConstruct
    public void initialize() {
        // Registramos algunos estanques para ejemplo
        registerPond("pond1", new String[]{"sensor1", "sensor2", "feeder1"});
        registerPond("pond2", new String[]{"sensor3", "sensor4", "feeder2"});
    }

    public void registerPond(String pondId, String[] deviceIds) {
        Map<String, DeviceData> devices = new ConcurrentHashMap<>();
        pondDeviceData.put(pondId, devices);

        for (String deviceId : deviceIds) {
            communicationService.registerDeviceDataHandler(deviceId, data -> {
                devices.put(deviceId, data);
                log.info("Datos actualizados para estanque {}, dispositivo {}", pondId, deviceId);
            });
        }
        log.info("Estanque registrado: {} con {} dispositivos", pondId, deviceIds.length);
    }

    public Set<String> getAllPondIds() {
        return pondDeviceData.keySet();
    }

    public Map<String, DeviceData> getPondDevices(String pondId) {
        return pondDeviceData.getOrDefault(pondId, Map.of());
    }

    public DeviceData getDeviceData(String pondId, String deviceId) {
        Map<String, DeviceData> devices = pondDeviceData.get(pondId);
        if (devices == null) {
            return null;
        }
        return devices.get(deviceId);
    }

    public boolean sendFeederCommand(String pondId, String feederId, String command, String value) {
        if (!pondDeviceData.containsKey(pondId)) {
            log.warn("Estanque no encontrado: {}", pondId);
            return false;
        }

        communicationService.sendControlCommand(feederId, command, value);
        log.info("Comando enviado al alimentador {} del estanque {}", feederId, pondId);
        return true;
    }

    @Scheduled(fixedDelayString = "${cloud.backend.sync.interval}")
    public void syncWithCloud() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Aplicar autenticación según lo configurado
            if (token != null && !token.isEmpty()) {
                // Autenticación con token Bearer
                headers.setBearerAuth(token);
                log.info("Usando autenticación con token Bearer");
            } else if (username != null && !username.isEmpty() && password != null) {
                // Autenticación básica usuario/contraseña
                headers.setBasicAuth(username, password);
                log.info("Usando autenticación básica");
            }

            // Crear entidad HTTP con los datos y los encabezados
            HttpEntity<Map<String, Map<String, DeviceData>>> entity =
                    new HttpEntity<>(pondDeviceData, headers);

            // Enviar al backend
            ResponseEntity<String> response = restTemplate.postForEntity(
                    cloudBackendUrl + "/sync", entity, String.class);

            log.info("Datos sincronizados con la nube. Estado: {}", response.getStatusCode());
        } catch (Exception e) {
            log.error("Error al sincronizar datos con la nube", e);
        }
    }
}
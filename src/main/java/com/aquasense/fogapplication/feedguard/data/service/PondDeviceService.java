package com.aquasense.fogapplication.feedguard.data.service;

import com.aquasense.fogapplication.feedguard.data.DeviceData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PondDeviceService {

    @Autowired
    private DeviceCommunicationService communicationService;

    public void processDeviceData(String pondId, String deviceId, DeviceData data) {
        data.setDeviceId(deviceId);
        String topic = String.format("sensors/%s/data", deviceId);
        String payload = convertToJson(data);
        communicationService.sendMessage(topic, payload);
        log.info("Processed data from pond {} device {}", pondId, deviceId);
    }

    private String convertToJson(DeviceData data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(data);
        } catch (Exception e) {
            log.error("Error converting to JSON: {}", e.getMessage());
            throw new RuntimeException("Error converting data to JSON");
        }
    }
}

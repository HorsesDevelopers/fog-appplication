package com.aquasense.fogapplication.feedguard.interfaces;

import com.aquasense.fogapplication.feedguard.data.DeviceData;
import com.aquasense.fogapplication.feedguard.data.service.PondDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fog1/v1/ponds")
@Slf4j
public class PondDeviceController {

    @Autowired
    private PondDeviceService pondDeviceService;

    @PostMapping("/{pondId}/devices/{deviceId}/data")
    public ResponseEntity<String> receiveDeviceData(
            @PathVariable String pondId,
            @PathVariable String deviceId,
            @RequestBody DeviceData deviceData
    ) {
        try {
            pondDeviceService.processDeviceData(pondId, deviceId, deviceData);
            return ResponseEntity.ok("Data processed successfully");
        } catch (Exception e) {
            log.error("Error processing device data: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing device data");
        }
    }

    @GetMapping("/{pondId}/devices/{deviceId}/status")
    public ResponseEntity<String> getDeviceStatus(
            @PathVariable String pondId,
            @PathVariable String deviceId) {
        return ResponseEntity.ok("Device status check endpoint");
    }
}

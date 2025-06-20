package com.aquasense.fogapplication.feedguard.interfaces;

import com.aquasense.fogapplication.feedguard.data.DeviceData;
import com.aquasense.fogapplication.feedguard.data.service.PondManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/fog1/v1/ponds")
@RequiredArgsConstructor
public class PondDeviceController {

    private final PondManagementService pondManagementService;

    @GetMapping
    public Set<String> getAllPonds() {
        return pondManagementService.getAllPondIds();
    }

    @GetMapping("/{pondId}/devices")
    public Map<String, DeviceData> getPondDevices(@PathVariable String pondId) {
        return pondManagementService.getPondDevices(pondId);
    }

    @GetMapping("/{pondId}/devices/{deviceId}")
    public ResponseEntity<DeviceData> getDeviceData(
            @PathVariable String pondId,
            @PathVariable String deviceId) {

        DeviceData deviceData = pondManagementService.getDeviceData(pondId, deviceId);
        if (deviceData == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deviceData);
    }

    @PostMapping("/{pondId}/feeders/{feederId}/control")
    public ResponseEntity<String> controlFeeder(
            @PathVariable String pondId,
            @PathVariable String feederId,
            @RequestParam String command,
            @RequestParam String value) {

        boolean success = pondManagementService.sendFeederCommand(pondId, feederId, command, value);
        if (!success) {
            return ResponseEntity.badRequest().body("Comando no pudo ser ejecutado");
        }
        return ResponseEntity.ok("Comando enviado exitosamente");
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerPond(
            @RequestParam String pondId,
            @RequestParam String[] deviceIds) {

        pondManagementService.registerPond(pondId, deviceIds);
        return ResponseEntity.ok("Estanque registrado correctamente");
    }
}
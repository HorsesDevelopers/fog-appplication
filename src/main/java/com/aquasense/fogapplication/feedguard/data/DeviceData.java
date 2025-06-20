package com.aquasense.fogapplication.feedguard.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class DeviceData {

    private String deviceId;

    private String type;

    private Float value;

    private String status;

    DeviceData(String deviceId, String type, Float value, String status) {
        this.deviceId = deviceId;
        this.type = type;
        this.value = value;
        this.status = status;
    }
}

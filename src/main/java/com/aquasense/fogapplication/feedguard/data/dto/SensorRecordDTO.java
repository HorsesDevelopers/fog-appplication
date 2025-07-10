package com.aquasense.fogapplication.feedguard.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SensorRecordDTO {
    private String device_id;
    private String sensor_type;
    private String value;
    private String timestamp;
    private String status;
}
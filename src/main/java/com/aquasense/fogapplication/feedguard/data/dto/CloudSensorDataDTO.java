package com.aquasense.fogapplication.feedguard.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CloudSensorDataDTO {

    private String deviceId;
    private String type;
    private Float value;
}

package com.aquasense.fogapplication.feedguard.data.dto;

import lombok.Data;

@Data
public class EdgeSensorPushDTO {
    private String device_id;
    private Float temperature;
    private Integer turbidity;
    private Float pH;
    private String timestamp;
    private String status;
}
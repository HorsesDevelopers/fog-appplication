package com.aquasense.fogapplication.feedguard.data.dto;

import lombok.Data;

@Data
public class EdgePondDataDTO {
    private Long id;
    private String device_id;
    private String record_type;
    private Float value;
    private String created_at;
}
package com.aquasense.fogapplication.feedguard.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "device_data")
@Getter
@Setter
@NoArgsConstructor
public class DeviceData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceId;
    private String type;

    @Column(name = "sensor_value")
    private Float value;

    private String status;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "cloud_synced")
    private boolean cloudSynced;

    public DeviceData(String deviceId, String type, Float value, String status) {
        this.deviceId = deviceId;
        this.type = type;
        this.value = value;
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.cloudSynced = false;
    }
}
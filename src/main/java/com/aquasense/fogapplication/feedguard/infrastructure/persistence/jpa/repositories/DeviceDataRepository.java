package com.aquasense.fogapplication.feedguard.infrastructure.persistence.jpa.repositories;

import com.aquasense.fogapplication.feedguard.data.DeviceData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceDataRepository extends JpaRepository<DeviceData, Long> {

    List<DeviceData> findByCloudSyncedFalse();
    List<DeviceData> findByDeviceId(String deviceId);
}

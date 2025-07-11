package com.aquasense.fogapplication.feedguard.om.infrastructure.persistence.jpa.repositories;

import com.aquasense.fogapplication.feedguard.om.domain.model.aggregates.PondRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PondRecordRepository extends JpaRepository<PondRecord, Long> {
}

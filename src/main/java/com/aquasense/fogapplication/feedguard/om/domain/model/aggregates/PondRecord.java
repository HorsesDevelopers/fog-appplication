package com.aquasense.fogapplication.feedguard.om.domain.model.aggregates;

import com.aquasense.fogapplication.feedguard.om.domain.model.valueobjects.RecordType;
import com.aquasense.fogapplication.feedguard.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Entity;

@Entity
public class PondRecord extends AuditableAbstractAggregateRoot<PondRecord> {

    String pondId;

    float value;

    RecordType recordType;

    public PondRecord() {}

    public PondRecord(String pondId, float value, RecordType recordType) {
        this.pondId = pondId;
        this.value = value;
        this.recordType = recordType;
    }
}

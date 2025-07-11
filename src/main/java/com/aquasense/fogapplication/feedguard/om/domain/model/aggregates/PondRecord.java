package com.aquasense.fogapplication.feedguard.om.domain.model.aggregates;

import com.aquasense.fogapplication.feedguard.om.domain.model.valueobjects.RecordType;
import com.aquasense.fogapplication.feedguard.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PondRecord extends AuditableAbstractAggregateRoot<PondRecord> {

    Long pondId;

    float value;

    RecordType recordType;

    public PondRecord() {}

    public PondRecord(Long pondId, float value, RecordType recordType) {
        this.pondId = pondId;
        this.value = value;
        this.recordType = recordType;
    }
}

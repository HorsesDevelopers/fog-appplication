package com.aquasense.fogapplication.feedguard.om.interfaces.rest.resources;

import java.util.Date;

public record PondRecordDTO(
        Long pondId,
        float value,
        String recordType,
        Date timestamp
        ) {
}

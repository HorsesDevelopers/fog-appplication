package com.aquasense.fogapplication.feedguard.om.domain.services;

import com.aquasense.fogapplication.feedguard.om.domain.model.aggregates.PondRecord;
import com.aquasense.fogapplication.feedguard.om.domain.model.commands.CreatePondRecordCommand;

import java.util.List;

public interface PondRecordCommandService {
    List<PondRecord> handle(CreatePondRecordCommand command);
}

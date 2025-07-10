package com.aquasense.fogapplication.feedguard.om.domain.services;

import com.aquasense.fogapplication.feedguard.om.domain.model.commands.CreatePondRecordCommand;

public interface PondRecordCommandService {
    void handle(CreatePondRecordCommand command);
}

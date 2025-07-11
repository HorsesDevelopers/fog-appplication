package com.aquasense.fogapplication.feedguard.om.interfaces.rest.transform;

import com.aquasense.fogapplication.feedguard.om.domain.model.commands.CreatePondRecordCommand;
import com.aquasense.fogapplication.feedguard.om.interfaces.rest.resources.ReceivePondRecordCommandResource;

public class ReceivePondRecordCommandFromResourceAssembler {
    public static CreatePondRecordCommand toCommandFromResource(
            ReceivePondRecordCommandResource resource) {
        return new CreatePondRecordCommand(
                resource.pondId(),
                resource.temp(),
                resource.ph(),
                resource.turbidity());
    }
}

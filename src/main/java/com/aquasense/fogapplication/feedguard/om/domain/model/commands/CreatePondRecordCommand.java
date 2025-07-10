package com.aquasense.fogapplication.feedguard.om.domain.model.commands;

public record CreatePondRecordCommand(
    String pondId,
    Float temp,
    Float pH,
    Float turbidity
) {
}

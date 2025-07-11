package com.aquasense.fogapplication.feedguard.om.interfaces.rest.resources;

public record ReceivePondRecordCommandResource(
    Long pondId,
    float temp,
    float ph,
    float turbidity
) {
}

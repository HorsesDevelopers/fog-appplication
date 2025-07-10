package com.aquasense.fogapplication.feedguard.om.interfaces.rest.resources;

public record ReceivePondRecordCommandResource(
    String pondId,
    float temp,
    float ph,
    float turbidity
) {
}

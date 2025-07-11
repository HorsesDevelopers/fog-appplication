package com.aquasense.fogapplication.feedguard.sdp.application.internal.outboundservices;

import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class MqttFeedService {
    private final MqttPahoMessageHandler mqttHandler;

    public MqttFeedService(MqttPahoMessageHandler mqttHandler) {
        this.mqttHandler = mqttHandler;
    }

    public void sendFeedCommand(Long pondId, String duration) {
        String payload = "{\"pondId\":" + pondId + ",\"duration\":\"" + duration + "\"}";
        Message<String> message = MessageBuilder.withPayload(payload).build();
        mqttHandler.handleMessage(message);
    }
}
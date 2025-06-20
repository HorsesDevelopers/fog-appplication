package com.aquasense.fogapplication.feedguard.data.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeviceCommunicationService {

    @Value("${cloud.backend.url}")
    private String cloudBackendUrl;


    @Autowired
    private MqttPahoMessageHandler mqttHandler;

    public void sendMessage(String topic, String payload) {
        Message<String> message = MessageBuilder.withPayload(payload)
                .setHeader("mqtt_topic", topic)
                .build();
        mqttHandler.handleMessage(message);
    }
}

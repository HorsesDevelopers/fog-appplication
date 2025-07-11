package com.aquasense.fogapplication.feedguard.sdp.infrastructure.mqtt.sfs.configiration;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;

@Configuration
public class MqttConfig {
    @Bean
    public DefaultMqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[] { "tcp://localhost:1883" }); // Cambia por tu broker
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MqttPahoMessageHandler mqttHandler(DefaultMqttPahoClientFactory factory) {
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler("fogPublisher", factory);
        handler.setAsync(true);
        handler.setDefaultTopic("feed/command");
        return handler;
    }
}
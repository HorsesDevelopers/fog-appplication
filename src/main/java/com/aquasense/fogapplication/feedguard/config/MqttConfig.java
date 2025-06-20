package com.aquasense.fogapplication.feedguard.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;

@Configuration
@Slf4j
public class MqttConfig {

    @Value("${edge.devices.connection.url}")
    private String brokerUrl;

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[] {brokerUrl});
        options.setCleanSession(true);
        return options;
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(mqttConnectOptions());
        return factory;
    }

    @Bean
    public MqttPahoMessageHandler mqttHandler() {  // Changed method name and return type
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("fogClient", mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("default/topic");
        messageHandler.setConverter(new DefaultPahoMessageConverter());
        return messageHandler;
    }

}

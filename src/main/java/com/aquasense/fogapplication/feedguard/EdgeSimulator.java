package com.aquasense.fogapplication.feedguard;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Random;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class EdgeSimulator implements CommandLineRunner {

    @Value("${edge.devices.connection.url:tcp://localhost:1883}")
    private String mqttBrokerUrl;

    private MqttClient mqttClient;
    private final Random random = new Random();

    @Override
    public void run(String... args) {
        connectMqtt();
    }

    private void connectMqtt() {
        try {
            String clientId = "edge-simulator-" + System.currentTimeMillis();
            mqttClient = new MqttClient(mqttBrokerUrl, clientId);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);

            mqttClient.connect(options);
            log.info("Conectado al broker MQTT: {}", mqttBrokerUrl);
        } catch (MqttException e) {
            log.error("Error conectando al broker MQTT", e);
        }
    }

    @Scheduled(fixedRate = 5000)
    public void sendSensorData() {
        if (mqttClient == null || !mqttClient.isConnected()) {
            return;
        }

        try {
            String[] sensors = {"sensor1", "sensor2", "sensor3"};
            String[] types = {"temperature", "oxygen", "ph"};

            for (String sensor : sensors) {
                String type = types[random.nextInt(types.length)];
                float value = 20 + random.nextFloat() * 10;
                String status = value > 25 ? "WARNING" : "OK";

                String payload = String.format("%s,%s,%.2f,%s", sensor, type, value, status);
                String topic = "sensors/" + sensor + "/data";

                mqttClient.publish(topic, new MqttMessage(payload.getBytes()));
                log.info("Datos enviados: {} - {}", topic, payload);
            }
        } catch (MqttException e) {
            log.error("Error enviando datos", e);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(EdgeSimulator.class, args);
    }
}
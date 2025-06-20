package com.aquasense.fogapplication.feedguard.data.service;

import com.aquasense.fogapplication.feedguard.data.DeviceData;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Service
@Slf4j
public class DeviceCommunicationService {
    @Value("${edge.devices.connection.url}")
    private String mqttBrokerUrl;

    @Value("${edge.devices.topics.sensor-data}")
    private String sensorDataTopic;

    @Value("${edge.devices.topics.feeder-control}")
    private String feederControlTopic;

    @Value("${device.connection.timeout}")
    private int connectionTimeout;

    private MqttClient mqttClient;
    private Map<String, Consumer<DeviceData>> dataHandlers = new HashMap<>();

    @PostConstruct
    public void initialize() {
        try {
            String clientId = "fog-application-" + System.currentTimeMillis();
            mqttClient = new MqttClient(mqttBrokerUrl, clientId);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setConnectionTimeout(connectionTimeout);
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);

            mqttClient.connect(options);
            subscribeToTopics();
            log.info("Conectado al broker MQTT: {}", mqttBrokerUrl);
        } catch (MqttException e) {
            log.error("Error al conectar con el broker MQTT", e);
        }
    }

    private void subscribeToTopics() throws MqttException {
        mqttClient.subscribe(sensorDataTopic, 1, (topic, message) -> {
            String deviceId = extractDeviceIdFromTopic(topic);
            DeviceData data = parseDeviceData(message);
            log.info("Datos recibidos del dispositivo {}", deviceId);

            if (dataHandlers.containsKey(deviceId)) {
                dataHandlers.get(deviceId).accept(data);
            }
        });
    }

    private String extractDeviceIdFromTopic(String topic) {
        String[] parts = topic.split("/");
        if (parts.length >= 2) {
            return parts[1];
        }
        return "unknown";
    }

    private DeviceData parseDeviceData(MqttMessage message) {
        String payload = new String(message.getPayload());
        String[] parts = payload.split(",");
        String deviceId = parts[0];
        String type = parts[1];
        Float value = Float.parseFloat(parts[2]);
        String status = parts[3];

        return new DeviceData(deviceId, type, value, status);
    }

    public void registerDeviceDataHandler(String deviceId, Consumer<DeviceData> handler) {
        dataHandlers.put(deviceId, handler);
        log.info("Registrado manejador para dispositivo: {}", deviceId);
    }

    public void sendControlCommand(String deviceId, String command, String value) {
        String topic = feederControlTopic.replace("+", deviceId);
        try {
            String payload = command + ":" + value;
            mqttClient.publish(topic, new MqttMessage(payload.getBytes()));
            log.info("Comando enviado al dispositivo {}: {}", deviceId, payload);
        } catch (MqttException e) {
            log.error("Error al enviar comando al dispositivo: " + deviceId, e);
        }
    }

    @PreDestroy
    public void cleanup() {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                mqttClient.disconnect();
                log.info("Desconectado del broker MQTT");
            }
        } catch (MqttException e) {
            log.error("Error al desconectar del broker MQTT", e);
        }
    }
}
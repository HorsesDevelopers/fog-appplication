package com.aquasense.fogapplication.feedguard.sdp.interfaces;

import com.aquasense.fogapplication.feedguard.sdp.application.internal.outboundservices.MqttFeedService;
import com.aquasense.fogapplication.feedguard.sdp.interfaces.resources.FeedCommandResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feed")
public class FeedController {
    private final MqttFeedService mqttFeedService;

    public FeedController(MqttFeedService mqttFeedService) {
        this.mqttFeedService = mqttFeedService;
    }

    @PostMapping
    public ResponseEntity<?> sendFeedCommand(@RequestBody FeedCommandResource command) {
        mqttFeedService.sendFeedCommand(command.pondId(), command.duration());
        return ResponseEntity.ok().build();
    }
}
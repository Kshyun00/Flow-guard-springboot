package com.skala.process_monitoring_ai.domain.notification.controller;

import com.skala.process_monitoring_ai.domain.notification.dto.AlertEmailRequest;
import com.skala.process_monitoring_ai.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<Void> sendAlert(@RequestBody AlertEmailRequest request) {
        notificationService.sendAnomalyEmail(request);
        return ResponseEntity.ok().build();
    }
}

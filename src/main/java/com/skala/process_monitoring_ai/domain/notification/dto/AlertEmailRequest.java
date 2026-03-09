package com.skala.process_monitoring_ai.domain.notification.dto;

import java.util.List;

public record AlertEmailRequest(
        Long eventId,
        String faultSummary,
        String estimatedCause,
        String recommendedAction,
        List<String> recipients
) {}

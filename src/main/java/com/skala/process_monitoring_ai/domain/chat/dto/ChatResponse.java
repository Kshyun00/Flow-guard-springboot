package com.skala.process_monitoring_ai.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChatResponse(
        String answer,
        String sessionId
) {}

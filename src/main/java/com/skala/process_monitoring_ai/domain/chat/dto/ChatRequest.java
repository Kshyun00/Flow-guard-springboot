package com.skala.process_monitoring_ai.domain.chat.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(
        @NotBlank(message = "질문 내용은 필수입니다.")
        String message
) {}

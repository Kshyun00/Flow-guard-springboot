package com.skala.process_monitoring_ai.domain.inference.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record InferenceRequest(
        @NotNull List<List<Double>> values
) {}

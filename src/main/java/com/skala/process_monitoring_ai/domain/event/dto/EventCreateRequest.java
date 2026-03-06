package com.skala.process_monitoring_ai.domain.event.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public record EventCreateRequest(
        @NotNull String timestamp,
        @NotNull @JsonProperty("anomaly_score") Double anomalyScore,
        @NotNull @JsonProperty("is_anomaly") Boolean isAnomaly,
        @JsonProperty("fault_type") Integer faultType,
        @JsonProperty("top_sensors") List<Map<String, Object>> topSensors,
        @JsonProperty("sensor_values") List<Double> sensorValues
) {}

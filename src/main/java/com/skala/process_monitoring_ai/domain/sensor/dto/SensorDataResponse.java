package com.skala.process_monitoring_ai.domain.sensor.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SensorDataResponse(
        String sensorId,
        String timestamp,
        Map<String, Double> metrics,   // 온도, 압력, 유량 등 계측 수치
        boolean anomaly,               // FastAPI 이상 탐지 결과
        Double anomalyScore            // 이상 점수
) {}

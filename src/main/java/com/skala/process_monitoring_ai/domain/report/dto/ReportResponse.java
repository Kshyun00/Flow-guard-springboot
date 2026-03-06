package com.skala.process_monitoring_ai.domain.report.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ReportResponse(
        String reportId,
        String title,
        String createdAt,
        String summary,
        List<AnomalyDetail> anomalies
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AnomalyDetail(
            String sensorId,
            String detectedAt,
            String description,
            Double anomalyScore
    ) {}
}

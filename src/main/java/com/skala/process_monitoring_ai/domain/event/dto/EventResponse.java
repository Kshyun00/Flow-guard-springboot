package com.skala.process_monitoring_ai.domain.event.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EventResponse(
        @JsonProperty("event_id") Long eventId,
        String timestamp,
        @JsonProperty("anomaly_score") Double anomalyScore,
        @JsonProperty("fault_type") Integer faultType,
        @JsonProperty("is_anomaly") Boolean isAnomaly,
        @JsonProperty("report_id") Long reportId
) {}

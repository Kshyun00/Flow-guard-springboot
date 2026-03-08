package com.skala.process_monitoring_ai.domain.simulation.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.skala.process_monitoring_ai.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimulationService {

    private final WebClient fastApiClient;

    public JsonNode startSimulation(int faultType, double interval, String scenario) {
        try {
            return fastApiClient.post()
                    .uri(uri -> {
                        var builder = uri.path("/ai/simulation/start")
                                .queryParam("fault_type", faultType)
                                .queryParam("interval", interval);
                        if (scenario != null && !scenario.isEmpty()) {
                            builder.queryParam("scenario", scenario);
                        }
                        return builder.build();
                    })
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("시뮬레이션 시작 실패 - status: {}, body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new BusinessException("시뮬레이션 시작에 실패했습니다.", HttpStatus.BAD_GATEWAY);
        }
    }

    public JsonNode stopSimulation() {
        try {
            return fastApiClient.post()
                    .uri("/ai/simulation/stop")
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("시뮬레이션 중지 실패 - status: {}", e.getStatusCode());
            throw new BusinessException("시뮬레이션 중지에 실패했습니다.", HttpStatus.BAD_GATEWAY);
        }
    }

    public JsonNode getStatus() {
        try {
            return fastApiClient.get()
                    .uri("/ai/simulation/status")
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("시뮬레이션 상태 조회 실패 - status: {}", e.getStatusCode());
            throw new BusinessException("시뮬레이션 상태를 조회할 수 없습니다.", HttpStatus.BAD_GATEWAY);
        }
    }

    public JsonNode getRealtimeSensors() {
        try {
            return fastApiClient.get()
                    .uri("/ai/sensors/realtime")
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("센서 실시간 조회 실패 - status: {}", e.getStatusCode());
            throw new BusinessException("센서 상태를 조회할 수 없습니다.", HttpStatus.BAD_GATEWAY);
        }
    }
}

package com.skala.process_monitoring_ai.domain.health.service;

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
public class HealthService {

    private final WebClient fastApiClient;

    public JsonNode getHealth() {
        try {
            return fastApiClient.get()
                    .uri("/health")
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("FastAPI 헬스체크 실패 - status: {}", e.getStatusCode());
            throw new BusinessException("AI 서비스 상태를 확인할 수 없습니다.", HttpStatus.BAD_GATEWAY);
        }
    }
}

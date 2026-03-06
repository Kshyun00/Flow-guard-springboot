package com.skala.process_monitoring_ai.domain.dashboard.service;

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
public class DashboardService {

    private final WebClient fastApiClient;

    public JsonNode getStats(String period) {
        try {
            return fastApiClient.get()
                    .uri(uri -> uri.path("/ai/dashboard/stats")
                            .queryParam("period", period)
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("FastAPI 대시보드 통계 조회 실패 - status: {}", e.getStatusCode());
            throw new BusinessException("대시보드 통계를 불러올 수 없습니다.", HttpStatus.BAD_GATEWAY);
        }
    }
}

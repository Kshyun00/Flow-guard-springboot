package com.skala.process_monitoring_ai.domain.report.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.skala.process_monitoring_ai.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final WebClient fastApiClient;

    public JsonNode generateReport(Map<String, Object> request) {
        try {
            return fastApiClient.post()
                    .uri("/ai/report")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("FastAPI 리포트 생성 실패 - status: {}, body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new BusinessException("리포트 생성에 실패했습니다.", HttpStatus.BAD_GATEWAY);
        }
    }

    public JsonNode getReports(int page, int size) {
        try {
            return fastApiClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/reports")
                            .queryParam("page", page)
                            .queryParam("size", size)
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("FastAPI 보고서 목록 조회 실패 - status: {}", e.getStatusCode());
            throw new BusinessException("보고서 목록을 불러올 수 없습니다.", HttpStatus.BAD_GATEWAY);
        } catch (Exception e) {
            log.error("FastAPI 보고서 연결 오류: {}", e.getMessage());
            throw new BusinessException("보고서 서비스에 연결할 수 없습니다.", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    public JsonNode getReport(String reportId) {
        try {
            return fastApiClient.get()
                    .uri("/reports/{reportId}", reportId)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw BusinessException.notFound("보고서를 찾을 수 없습니다.");
        } catch (WebClientResponseException e) {
            log.error("FastAPI 보고서 조회 실패 - status: {}", e.getStatusCode());
            throw new BusinessException("보고서를 불러올 수 없습니다.", HttpStatus.BAD_GATEWAY);
        } catch (Exception e) {
            log.error("FastAPI 보고서 연결 오류: {}", e.getMessage());
            throw new BusinessException("보고서 서비스에 연결할 수 없습니다.", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}

package com.skala.process_monitoring_ai.domain.inference.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.skala.process_monitoring_ai.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class InferenceService {

    private final WebClient fastApiClient;

    public JsonNode getSampleData(int faultType, int rows) {
        try {
            return fastApiClient.get()
                    .uri(uri -> uri.path("/ai/inference/sample")
                            .queryParam("fault_type", faultType)
                            .queryParam("rows", rows)
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("FastAPI 샘플데이터 조회 실패 - status: {}", e.getStatusCode());
            throw new BusinessException("샘플 데이터를 불러올 수 없습니다.", HttpStatus.BAD_GATEWAY);
        }
    }

    public JsonNode runInference(List<List<Double>> values) {
        try {
            return fastApiClient.post()
                    .uri("/ai/inference")
                    .bodyValue(Map.of("values", values))
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("FastAPI 추론 실패 - status: {}, body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            if (e.getStatusCode().value() == 503) {
                throw new BusinessException("모델이 로드되지 않았습니다.", HttpStatus.SERVICE_UNAVAILABLE);
            }
            throw new BusinessException("추론 요청에 실패했습니다.", HttpStatus.BAD_GATEWAY);
        }
    }
}

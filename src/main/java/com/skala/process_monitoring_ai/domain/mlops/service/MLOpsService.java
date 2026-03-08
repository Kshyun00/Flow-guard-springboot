package com.skala.process_monitoring_ai.domain.mlops.service;

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
public class MLOpsService {

    private final WebClient fastApiClient;

    // ===== 드리프트 =====

    public JsonNode getDriftStatus() {
        try {
            return fastApiClient.get()
                    .uri("/ai/mlops/drift/status")
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("드리프트 상태 조회 실패 - status: {}", e.getStatusCode());
            throw new BusinessException("드리프트 상태를 조회할 수 없습니다.", HttpStatus.BAD_GATEWAY);
        }
    }

    public JsonNode getDriftResult() {
        try {
            return fastApiClient.get()
                    .uri("/ai/mlops/drift/result")
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("드리프트 결과 조회 실패 - status: {}", e.getStatusCode());
            throw new BusinessException("드리프트 결과를 조회할 수 없습니다.", HttpStatus.BAD_GATEWAY);
        }
    }

    public JsonNode getDriftLogs(int page, int size) {
        try {
            return fastApiClient.get()
                    .uri(uri -> uri.path("/ai/mlops/drift/logs")
                            .queryParam("page", page)
                            .queryParam("size", size)
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("드리프트 로그 조회 실패 - status: {}", e.getStatusCode());
            throw new BusinessException("드리프트 로그를 조회할 수 없습니다.", HttpStatus.BAD_GATEWAY);
        }
    }

    public JsonNode acknowledgeDrift(int driftId) {
        try {
            return fastApiClient.post()
                    .uri("/ai/mlops/drift/acknowledge/" + driftId)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("드리프트 확인 처리 실패 - status: {}", e.getStatusCode());
            throw new BusinessException("드리프트 확인 처리에 실패했습니다.", HttpStatus.BAD_GATEWAY);
        }
    }

    // ===== 모델 =====

    public JsonNode getModels() {
        try {
            return fastApiClient.get()
                    .uri("/ai/mlops/models")
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("모델 목록 조회 실패 - status: {}", e.getStatusCode());
            throw new BusinessException("모델 목록을 조회할 수 없습니다.", HttpStatus.BAD_GATEWAY);
        }
    }

    public JsonNode getActiveModel() {
        try {
            return fastApiClient.get()
                    .uri("/ai/mlops/models/active")
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("활성 모델 조회 실패 - status: {}", e.getStatusCode());
            throw new BusinessException("활성 모델을 조회할 수 없습니다.", HttpStatus.BAD_GATEWAY);
        }
    }

    public JsonNode deployModel(int versionId) {
        try {
            return fastApiClient.post()
                    .uri("/ai/mlops/models/" + versionId + "/deploy")
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("모델 배포 실패 - status: {}", e.getStatusCode());
            throw new BusinessException("모델 배포에 실패했습니다.", HttpStatus.BAD_GATEWAY);
        }
    }

    public JsonNode rollbackModel(int versionId) {
        try {
            return fastApiClient.post()
                    .uri("/ai/mlops/models/" + versionId + "/rollback")
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("모델 롤백 실패 - status: {}", e.getStatusCode());
            throw new BusinessException("모델 롤백에 실패했습니다.", HttpStatus.BAD_GATEWAY);
        }
    }

    // ===== 재학습 =====

    public JsonNode requestRetrain(Integer driftLogId, String description) {
        try {
            String uri = "/ai/mlops/retrain/request";
            if (driftLogId != null || description != null) {
                StringBuilder sb = new StringBuilder(uri).append("?");
                if (driftLogId != null) sb.append("drift_log_id=").append(driftLogId).append("&");
                if (description != null) sb.append("description=").append(description);
                uri = sb.toString();
            }
            return fastApiClient.post()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("재학습 요청 실패 - status: {}", e.getStatusCode());
            throw new BusinessException("재학습 요청에 실패했습니다.", HttpStatus.BAD_GATEWAY);
        }
    }

    public JsonNode getRetrainJobs(int page, int size) {
        try {
            return fastApiClient.get()
                    .uri(uri -> uri.path("/ai/mlops/retrain/jobs")
                            .queryParam("page", page)
                            .queryParam("size", size)
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("재학습 작업 목록 조회 실패 - status: {}", e.getStatusCode());
            throw new BusinessException("재학습 작업 목록을 조회할 수 없습니다.", HttpStatus.BAD_GATEWAY);
        }
    }

    public JsonNode approveRetrain(int jobId) {
        try {
            return fastApiClient.post()
                    .uri("/ai/mlops/retrain/" + jobId + "/approve")
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("재학습 승인 실패 - status: {}", e.getStatusCode());
            throw new BusinessException("재학습 승인에 실패했습니다.", HttpStatus.BAD_GATEWAY);
        }
    }

    public JsonNode rejectRetrain(int jobId) {
        try {
            return fastApiClient.post()
                    .uri("/ai/mlops/retrain/" + jobId + "/reject")
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("재학습 거절 실패 - status: {}", e.getStatusCode());
            throw new BusinessException("재학습 거절에 실패했습니다.", HttpStatus.BAD_GATEWAY);
        }
    }

    public JsonNode getRetrainStatus(int jobId) {
        try {
            return fastApiClient.get()
                    .uri("/ai/mlops/retrain/" + jobId + "/status")
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("재학습 상태 조회 실패 - status: {}", e.getStatusCode());
            throw new BusinessException("재학습 상태를 조회할 수 없습니다.", HttpStatus.BAD_GATEWAY);
        }
    }

    // ===== 성능 =====

    public JsonNode getPerformance(int limit) {
        try {
            return fastApiClient.get()
                    .uri(uri -> uri.path("/ai/mlops/performance")
                            .queryParam("limit", limit)
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("성능 데이터 조회 실패 - status: {}", e.getStatusCode());
            throw new BusinessException("성능 데이터를 조회할 수 없습니다.", HttpStatus.BAD_GATEWAY);
        }
    }
}

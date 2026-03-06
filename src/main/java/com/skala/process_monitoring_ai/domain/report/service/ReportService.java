package com.skala.process_monitoring_ai.domain.report.service;

import com.skala.process_monitoring_ai.domain.report.dto.ReportResponse;
import com.skala.process_monitoring_ai.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final WebClient fastApiClient;

    public List<ReportResponse> getReports() {
        try {
            return fastApiClient.get()
                    .uri("/reports")
                    .retrieve()
                    .bodyToFlux(ReportResponse.class)
                    .collectList()
                    .block();
        } catch (WebClientResponseException e) {
            log.error("FastAPI 보고서 목록 조회 실패 - status: {}", e.getStatusCode());
            throw new BusinessException("보고서 목록을 불러올 수 없습니다.", HttpStatus.BAD_GATEWAY);
        } catch (Exception e) {
            log.error("FastAPI 보고서 연결 오류: {}", e.getMessage());
            throw new BusinessException("보고서 서비스에 연결할 수 없습니다.", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    public ReportResponse getReport(String reportId) {
        try {
            return fastApiClient.get()
                    .uri("/reports/{reportId}", reportId)
                    .retrieve()
                    .bodyToMono(ReportResponse.class)
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

package com.skala.process_monitoring_ai.domain.report.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.skala.process_monitoring_ai.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final WebClient fastApiClient;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.host:}")
    private String smtpHost;

    @Value("${spring.mail.username:}")
    private String smtpUser;

    public JsonNode generateReport(Map<String, Object> request) {
        try {
            JsonNode response = fastApiClient.post()
                    .uri("/ai/report")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
            sendNotificationEmail(response);
            return response;
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

    private void sendNotificationEmail(JsonNode reportResponse) {
        if (reportResponse == null || smtpHost == null || smtpHost.isBlank()) {
            return;
        }

        try {
            JsonNode reportData = reportResponse.path("data");
            if (reportData.isMissingNode() || reportData.isNull()) {
                return;
            }

            JsonNode settingResponse = fastApiClient.get()
                    .uri("/ai/notifications/settings")
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
            JsonNode settings = settingResponse == null ? null : settingResponse.path("data");
            if (settings == null || settings.isMissingNode() || settings.isNull()) {
                return;
            }

            if (!settings.path("enabled").asBoolean(true)) {
                return;
            }

            List<String> recipients = parseRecipients(settings.path("email_recipients"));
            if (recipients.isEmpty()) {
                return;
            }

            String eventId = reportData.path("event_id").asText("unknown");
            String subject = "[FlowWatch] 이상 탐지 - 이벤트 #" + eventId;
            String body = buildEmailBody(reportData);

            SimpleMailMessage message = new SimpleMailMessage();
            if (smtpUser != null && !smtpUser.isBlank()) {
                message.setFrom(smtpUser);
            }
            message.setTo(recipients.toArray(String[]::new));
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception e) {
            log.warn("이메일 알림 발송 실패(리포트 생성은 유지): {}", e.getMessage());
        }
    }

    private List<String> parseRecipients(JsonNode recipientsNode) {
        List<String> recipients = new ArrayList<>();
        if (recipientsNode == null || !recipientsNode.isArray()) {
            return recipients;
        }

        recipientsNode.forEach(node -> {
            if (node != null && node.isTextual()) {
                String email = node.asText().trim();
                if (!email.isBlank()) {
                    recipients.add(email);
                }
            }
        });
        return recipients;
    }

    private String buildEmailBody(JsonNode reportData) {
        return "[FlowWatch 이상 탐지 알림]\n"
                + "이벤트 ID: " + reportData.path("event_id").asText("unknown") + "\n"
                + "결함 요약: " + reportData.path("fault_summary").asText("") + "\n"
                + "추정 원인: " + reportData.path("estimated_cause").asText("") + "\n"
                + "권장 조치: " + reportData.path("recommended_action").asText("");
    }
}

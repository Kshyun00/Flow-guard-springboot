package com.skala.process_monitoring_ai.domain.notification.service;

import com.skala.process_monitoring_ai.domain.notification.dto.AlertEmailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String from;

    public void sendAnomalyEmail(AlertEmailRequest request) {
        if (request.recipients() == null || request.recipients().isEmpty()) {
            log.warn("이메일 수신자가 없어 발송을 건너뜁니다.");
            return;
        }

        try {
            String subject = "[FlowWatch] 이상 탐지 - 이벤트 #" + request.eventId();
            String body = buildBody(request);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(request.recipients().toArray(String[]::new));
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            log.info("이상 탐지 이메일 발송 완료: eventId={}, recipients={}", request.eventId(), request.recipients());
        } catch (Exception e) {
            log.error("이메일 발송 실패: eventId={}, error={}", request.eventId(), e.getMessage());
        }
    }

    private String buildBody(AlertEmailRequest request) {
        return "[FlowWatch 이상 탐지 알림]\n"
                + "이벤트 ID: " + request.eventId() + "\n"
                + "결함 요약: " + request.faultSummary() + "\n"
                + "추정 원인: " + request.estimatedCause() + "\n"
                + "권장 조치: " + request.recommendedAction();
    }
}

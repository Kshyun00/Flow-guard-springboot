package com.skala.process_monitoring_ai.domain.chat.controller;

import com.skala.process_monitoring_ai.domain.chat.dto.ChatRequest;
import com.skala.process_monitoring_ai.domain.chat.dto.ChatResponse;
import com.skala.process_monitoring_ai.domain.chat.service.ChatService;
import com.skala.process_monitoring_ai.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ApiResponse<ChatResponse>> chat(
            @Valid @RequestBody ChatRequest request,
            @AuthenticationPrincipal String email) {
        ChatResponse response = chatService.chat(request, email);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

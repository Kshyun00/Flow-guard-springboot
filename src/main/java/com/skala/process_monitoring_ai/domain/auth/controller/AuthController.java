package com.skala.process_monitoring_ai.domain.auth.controller;

import com.skala.process_monitoring_ai.domain.auth.dto.LoginRequest;
import com.skala.process_monitoring_ai.domain.auth.dto.SignupRequest;
import com.skala.process_monitoring_ai.domain.auth.dto.TokenResponse;
import com.skala.process_monitoring_ai.domain.auth.service.AuthService;
import com.skala.process_monitoring_ai.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("회원가입이 완료되었습니다.", null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse token = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(token));
    }
}

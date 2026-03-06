package com.skala.process_monitoring_ai.domain.user.controller;

import com.skala.process_monitoring_ai.domain.user.dto.UserResponse;
import com.skala.process_monitoring_ai.domain.user.service.UserService;
import com.skala.process_monitoring_ai.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyInfo(
            @AuthenticationPrincipal String email) {
        UserResponse response = userService.getMyInfo(email);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

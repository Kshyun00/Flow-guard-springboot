package com.skala.process_monitoring_ai.domain.auth.service;

import com.skala.process_monitoring_ai.domain.auth.dto.LoginRequest;
import com.skala.process_monitoring_ai.domain.auth.dto.SignupRequest;
import com.skala.process_monitoring_ai.domain.auth.dto.TokenResponse;
import com.skala.process_monitoring_ai.domain.user.entity.User;
import com.skala.process_monitoring_ai.domain.user.repository.UserRepository;
import com.skala.process_monitoring_ai.global.exception.BusinessException;
import com.skala.process_monitoring_ai.global.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public void signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw BusinessException.conflict("이미 사용 중인 이메일입니다.");
        }

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .name(request.name())
                .role(User.Role.USER)
                .build();

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> BusinessException.unauthorized("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw BusinessException.unauthorized("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        String accessToken = jwtProvider.createAccessToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtProvider.createRefreshToken(user.getEmail());

        return TokenResponse.of(accessToken, refreshToken);
    }
}

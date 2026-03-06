package com.skala.process_monitoring_ai.domain.user.service;

import com.skala.process_monitoring_ai.domain.user.dto.UserResponse;
import com.skala.process_monitoring_ai.domain.user.entity.User;
import com.skala.process_monitoring_ai.domain.user.repository.UserRepository;
import com.skala.process_monitoring_ai.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponse getMyInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> BusinessException.notFound("사용자를 찾을 수 없습니다."));
        return UserResponse.from(user);
    }
}

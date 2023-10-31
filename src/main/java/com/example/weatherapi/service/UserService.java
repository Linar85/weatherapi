package com.example.weatherapi.service;


import com.example.weatherapi.dto.ApiKeyDto;
import com.example.weatherapi.entity.ApiKey;
import com.example.weatherapi.entity.User;
import com.example.weatherapi.entity.UserRole;
import com.example.weatherapi.repository.UserDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public Mono<User> register(User user){
        return userDao.save(user.toBuilder()
                .password(passwordEncoder.encode(user.getPassword()))
                .role(UserRole.USER)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build()
        ).doOnSuccess(us -> {
            log.info("created" + us);
        });
    }

    public Mono<User> getUserById(Long id) {
        return userDao.findById(id);
    }

    public Mono<User> getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public Mono<ApiKey> getApiKey() {
        return null;
    }
}

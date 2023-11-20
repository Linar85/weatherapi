package com.example.weatherapi.service;


import com.example.weatherapi.dto.UserDto;
import com.example.weatherapi.entity.ApiKey;
import com.example.weatherapi.entity.RateLimiter;
import com.example.weatherapi.entity.User;
import com.example.weatherapi.entity.UserRole;
import com.example.weatherapi.mapper.UserMapper;
import com.example.weatherapi.repository.RateLimiterDao;
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
    private final RateLimiterDao rateLimiterDao;
    private final UserMapper userMapper;

    public Mono<UserDto> register(User user) {
        return userDao.save(user.toBuilder()
                .password(passwordEncoder.encode(user.getPassword()))
                .role(UserRole.USER)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build()
        ).map(userMapper::map).doOnSuccess(us -> {
            log.info("created" + us);
            rateLimiterDao.save(RateLimiter.builder()
                    .bucketCapacity(us.getRole().bucketCapacity)
                    .refillGreedyTokens(us.getRole().refillGreedyTokens)
                    .refillGreedyDurationSeconds(us.getRole().refillGreedyDurationSeconds)
                    .userId(us.getId())
                    .build()).doOnSuccess(rate -> log.info("recorded" + rate)).subscribe();
        });
    }

    public Mono<User> getUserById(Long id) {
        return userDao.findById(id);
    }

    public Mono<User> getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }
}

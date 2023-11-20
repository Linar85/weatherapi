package com.example.weatherapi.service;

import com.example.weatherapi.entity.RateLimiter;
import com.example.weatherapi.entity.User;
import com.example.weatherapi.entity.UserRole;
import com.example.weatherapi.mapper.UserMapper;
import com.example.weatherapi.repository.RateLimiterDao;
import com.example.weatherapi.repository.UserDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;
    @Mock
    UserDao userDao;
    @Mock
    RateLimiterDao rateLimiterDao;
    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    @Spy
    PasswordEncoder passwordEncoder;

    User user = User.builder()
            .username("user")
            .password("password")
            .firstName("firstname")
            .lastName("lastName")
            .role(UserRole.USER)
            .enabled(true)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    RateLimiter rateLimiter = RateLimiter.builder()
            .userId(1L)
            .bucketCapacity(2)
            .refillGreedyTokens(2)
            .refillGreedyDurationSeconds(2)
            .build();

    @Test
    void register() {

        Mockito.when(userDao.save(any(User.class))).thenReturn(Mono.just(user));
        Mockito.when(rateLimiterDao.save(any(RateLimiter.class))).thenReturn(Mono.just(rateLimiter));
        Mockito.when(passwordEncoder.encode(anyString())).thenReturn("password");

        StepVerifier.create(userService.register(user))
                .consumeNextWith(userDto -> Assertions.assertEquals(userMapper.map(user).getUsername(), userDto.getUsername()))
                .verifyComplete();

    }

    @Test
    void getUserById() {

        Mockito.when(userDao.findById(anyLong())).thenReturn(Mono.just(user));
        StepVerifier.create(userService.getUserById(1L))
                .consumeNextWith(us -> Assertions.assertEquals(us.getUsername(), user.getUsername()))
                .verifyComplete();
    }

    @Test
    void getUserByUsername() {
        Mockito.when(userDao.findByUsername("user")).thenReturn(Mono.just(user));
        StepVerifier.create(userService.getUserByUsername("user"))
                .consumeNextWith(us -> Assertions.assertEquals(us.getUsername(), user.getUsername()))
                .verifyComplete();
    }
}
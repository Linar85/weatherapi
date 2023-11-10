package com.example.weatherapi.controller;

import com.example.weatherapi.WeatherapiApplication;
import com.example.weatherapi.dto.UserDto;
import com.example.weatherapi.entity.User;
import com.example.weatherapi.mapper.ApiKeyMapper;
import com.example.weatherapi.mapper.UserMapper;
import com.example.weatherapi.repository.ApiKeyRedisDao;
import com.example.weatherapi.repository.RateLimiterDao;
import com.example.weatherapi.repository.UserDao;
import com.example.weatherapi.security.SecurityService;
import com.example.weatherapi.service.ApiKeyService;
import com.example.weatherapi.service.UserService;
import com.example.weatherapi.utils.RateBuckets;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = AuthController.class, excludeAutoConfiguration = ReactiveSecurityAutoConfiguration.class)
class AuthControllerTest {

    @MockBean
    SecurityService securityService;
    @MockBean
    UserService userService;
    @MockBean
    UserMapper userMapper;
    @MockBean
    ApiKeyService apiKeyService;
    @MockBean
    ApiKeyMapper apiKeyMapper;
    @MockBean
//    ApiKeyRedisDao apiKeyRedisDao;
//    @MockBean
//    RateBuckets rateBuckets;
//    @MockBean
//    RateLimiterDao rateLimiterDao;
//    @SpyBean
//    PasswordEncoder passwordEncoder;
//    @MockBean
//    UserDao userDao;
    @Autowired
    private WebTestClient webClient;

    @Test
    void register() {

        UserDto userDto = new UserDto();
        userDto.setUsername("testUser");
        userDto.setPassword("testPassword");

        User user = User.builder()
                .username("testUser")
                .password("testPassword")
                .build();


        Mockito.when(userMapper.map(any(UserDto.class))).thenReturn(user);
        Mockito.when(userMapper.map(any(User.class))).thenReturn(userDto);
        Mockito.when(userService.register(any(User.class))).thenReturn(Mono.just(user));

        webClient.post()
                .uri("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class);


    }

    @Test
    void login() {
    }

    @Test
    void getApiKey() {
    }

    @Test
    void getUserInfo() {
    }
}
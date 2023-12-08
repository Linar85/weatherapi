//package com.example.weatherapi.controller;
//
//import com.example.weatherapi.dto.UserDto;
//import com.example.weatherapi.entity.User;
//import com.example.weatherapi.entity.UserRole;
//import com.example.weatherapi.mapper.ApiKeyMapper;
//import com.example.weatherapi.mapper.UserMapper;
//import com.example.weatherapi.security.SecurityService;
//import com.example.weatherapi.service.ApiKeyService;
//import com.example.weatherapi.service.UserService;
//import org.junit.Ignore;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.reactive.server.WebTestClient;
//import reactor.core.publisher.Mono;
//
//import java.time.LocalDateTime;
//
//import static org.mockito.ArgumentMatchers.any;
//
//@ExtendWith(SpringExtension.class)
////@WebFluxTest(controllers = AuthController.class, excludeAutoConfiguration = ReactiveSecurityAutoConfiguration.class)
//@WebFluxTest(controllers = AuthController.class)
//@Ignore
//class AuthControllerTest {
//
//    @Autowired
//    private WebTestClient webClient;
//
//    @MockBean
//    SecurityService securityService;
//    @MockBean
//    UserService userService;
//    @MockBean
//    UserMapper userMapper;
//    @MockBean
//    ApiKeyService apiKeyService;
//    @MockBean
//    ApiKeyMapper apiKeyMapper;
//
//
//    @BeforeEach
//    public void setup() {
////        this.webClient = WebTestClient
////                .bindToApplicationContext(this.context)
////                // add Spring Security test Support
////                .apply(springSecurity())
////                .configureClient()
////                .filter(basicAuthentication("testUser", "testPassword"))
////                .build();
//
////        webClient = WebTestClient.bindToController(authController)
////                .webFilter(new SecurityContextServerWebExchangeWebFilter())
////                .webFilter(apiKeyFilter)
////                .apply(springSecurity())
////                .build();
//    }
//
//    @Test
//    @WithMockUser
//    void register() {
//
//        UserDto userDto = new UserDto();
//        userDto.setUsername("testUser");
//        userDto.setPassword("testPassword");
//        userDto.setFirstName("testFirstName");
//        userDto.setLastName("testLastName");
//
//        User user = User.builder()
//                .id(1L)
//                .username("testUser")
//                .role(UserRole.USER)
//                .password("testPassword")
//                .firstName("testFirstName")
//                .lastName("testLastName")
//                .enabled(true)
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//
//        Mockito.when(userService.register(any(User.class))).thenReturn(Mono.just(userDto));
//
//        webClient.post()
//                .uri("/api/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(userDto)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(UserDto.class);
//
//
//    }
//
////    @Test
////    void login() {
////    }
////
////    @Test
////    void getApiKey() {
////    }
////
////    @Test
////    void getUserInfo() {
////    }
//}
package com.example.weatherapi.controller;

import com.example.weatherapi.dto.ApiKeyDto;
import com.example.weatherapi.dto.AuthRqDto;
import com.example.weatherapi.dto.AuthRsDto;
import com.example.weatherapi.dto.UserDto;
import com.example.weatherapi.entity.User;
import com.example.weatherapi.mapper.ApiKeyMapper;
import com.example.weatherapi.mapper.UserMapper;
import com.example.weatherapi.security.CustomPrincipal;
import com.example.weatherapi.security.SecurityService;
import com.example.weatherapi.service.ApiKeyService;
import com.example.weatherapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final SecurityService securityService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final ApiKeyService apiKeyService;
    private final ApiKeyMapper apiKeyMapper;


    @PostMapping("/register")
    public Mono<UserDto> register(@RequestBody UserDto userDto) {
        User user = userMapper.map(userDto);
        return userService.register(user);
    }

    @PostMapping("/login")
    public Mono<AuthRsDto> login(@RequestBody AuthRqDto authRqDto) {
        return securityService.authenticate(authRqDto.getUsername(), authRqDto.getPassword())
                .flatMap(tokenDetails -> Mono.just(
                        AuthRsDto.builder()
                                .userId(tokenDetails.getUserId())
                                .token(tokenDetails.getToken())
                                .expiresAt(tokenDetails.getExpiresAt())
                                .issuedAt(tokenDetails.getIssuedAt())
                                .build()
                ));
    }

    @PostMapping("/get-api-key")
    public Mono<ApiKeyDto> getApiKey(Authentication authentication) {
        return apiKeyService.apiKeyGenerate(authentication).map(apiKeyMapper::map);
    }

    @GetMapping("/info")
    public Mono<UserDto> getUserInfo(Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        return userService.getUserById(customPrincipal.getId())
                .map(userMapper::map);
    }
}

//package com.example.weatherapi.controller;
//
//import com.example.weatherapi.dto.StationDto;
//import com.example.weatherapi.entity.Station;
//import com.example.weatherapi.filters.ApiKeyFilter;
//import com.example.weatherapi.mapper.StationMapper;
//import com.example.weatherapi.service.StationService;
//import com.example.weatherapi.utils.DataUpdater;
//import com.redis.testcontainers.RedisContainer;
//import org.junit.Ignore;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.reactive.server.WebTestClient;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.utility.DockerImageName;
//import reactor.core.publisher.Flux;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@WithMockUser
//@AutoConfigureWebTestClient(timeout = "10000")
//@Testcontainers(disabledWithoutDocker = true)
//@Ignore
//class WeatherControllerTest {
//
//    @MockBean
//    DataUpdater dataUpdater;
//    @MockBean
//    StationService stationService;
//    @Autowired
//    StationMapper stationMapper;
//    @Autowired
//    ApiKeyFilter apiKeyFilter;
//    @Autowired
//    WebTestClient webTestClient;
////    @MockBean
////    ServerWebExchange serverWebExchange;
////    @MockBean
////    WebFilterChain webFilterChain;
//    @Container
//    private static final RedisContainer REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:5.0.3-alpine")).withExposedPorts(6379);
//
//    @DynamicPropertySource
//    private static void registerRedisProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.redis.host", REDIS_CONTAINER::getHost);
//        registry.add("spring.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
//    }
//
//    @Test
//    void getStations() {
//
//        Mockito.when(stationService.findAvailableStations()).thenReturn(Flux.just(new Station()));
////        Mockito.when(apiKeyFilter.filter(serverWebExchange, webFilterChain)).thenReturn(Mono.when());
//
//        webTestClient.get()
//                .uri("/api/stations")
//                .header("X-API-key")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(StationDto.class)
//                .returnResult();
//
//    }
//
//    @Test
//    void findWeathersOnStation() {
//    }
//}
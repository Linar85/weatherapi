package com.example.weatherapi.service;

import com.example.weatherapi.entity.Station;
import com.example.weatherapi.entity.Weather;
import com.example.weatherapi.mapper.StationMapper;
import com.example.weatherapi.repository.StationDao;
import com.example.weatherapi.repository.StationRedisDao;
import com.example.weatherapi.repository.WeatherDao;
import com.example.weatherapi.repository.WeatherRedisDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @InjectMocks
    StationService stationService;
    @Mock
    StationDao stationDao;
    @Mock
    WeatherDao weatherDao;
    @Mock
    StationRedisDao stationRedisDao;
    @Mock
    WeatherRedisDao weatherRedisDao;
    @Spy
    private StationMapper stationMapper = Mappers.getMapper(StationMapper.class);

    @Test
    void findAvailableStations() {
        Station station1 = Station.builder()
                .id("1")
                .stationCode("testCode")
                .build();
        Station station2 = Station.builder()
                .id("2")
                .stationCode("testCode2")
                .build();

        Mockito.when(stationRedisDao.findAll()).thenReturn(Flux.just(station1));
        Mockito.when(stationDao.findAll()).thenReturn(Flux.just(station2));

        StepVerifier.create(stationService.findAvailableStations())
                .consumeNextWith(st -> {
                    Assertions.assertEquals(st, station1);
                    Assertions.assertNotEquals(st, station2);
                })
                .verifyComplete();
    }

    @Test
    void checkThatInsideMethodCalled() {
        Station station = Station.builder()
                .id("1")
                .stationCode("testCode")
                .build();

        Mockito.when(stationRedisDao.findAll()).thenReturn(Flux.empty());
        Mockito.when(stationDao.findAll()).thenReturn(Flux.just(station));
        Mockito.when(stationRedisDao.save(any(Station.class))).thenReturn(Mono.when());

        StepVerifier.create(stationService.findAvailableStations())
                .consumeNextWith(st -> Assertions.assertEquals(st, station))
                .verifyComplete();
    }

    @Test
    void findWeathersOnStationByStationCodeWhereDataFromRedis() {
        Station station1 = Station.builder()
                .id("1")
                .stationCode("testCode1")
                .build();
        Station station2 = Station.builder()
                .id("2")
                .stationCode("testCode2")
                .build();
        Weather weather1 = Weather.builder()
                .stationCode("testCode1")
                .createdAt(LocalDateTime.now())
                .build();
        Weather weather2 = Weather.builder()
                .stationCode("testCode2")
                .createdAt(LocalDateTime.now())
                .build();

        Mockito.when(stationRedisDao.findByStationCode(anyString())).thenReturn(Mono.just(station1));
        Mockito.when(weatherRedisDao.findByStationCode(anyString())).thenReturn(Flux.just(weather1));
        Mockito.when(stationDao.findByStationCode(anyString())).thenReturn(Mono.just(station2));
        Mockito.when(weatherDao.findAllByStationCode(anyString())).thenReturn(Flux.just(weather2));
        Mockito.when(weatherRedisDao.saveWeather(any(Weather.class))).thenReturn(Mono.when());

        StepVerifier.create(stationService.findLastWeathersOnStationByStationCode("testCode1"))
                .consumeNextWith(st -> {
                    Assertions.assertEquals(stationMapper.map(station1), st);
                    Assertions.assertEquals(weather1, st.getWeather());
                    Assertions.assertNotEquals(stationMapper.map(station2), st);
                })
                .verifyComplete();
    }

    @Test
    void findWeathersOnStationByStationCodeWhereDataFromDb() {
        Station station1 = Station.builder()
                .id("1")
                .stationCode("testCode1")
                .build();
        Station station2 = Station.builder()
                .id("2")
                .stationCode("testCode2")
                .build();
        Weather weather2 = Weather.builder()
                .stationCode("testCode2")
                .createdAt(LocalDateTime.now())
                .build();

        Mockito.when(stationRedisDao.findByStationCode(anyString())).thenReturn(Mono.empty());
        Mockito.when(weatherRedisDao.findByStationCode(anyString())).thenReturn(Flux.empty());
        Mockito.when(stationDao.findByStationCode(anyString())).thenReturn(Mono.just(station2));
        Mockito.when(weatherDao.findAllByStationCode(anyString())).thenReturn(Flux.just(weather2));
        Mockito.when(weatherRedisDao.saveWeather(any(Weather.class))).thenReturn(Mono.when());
        Mockito.when(stationRedisDao.save(any(Station.class))).thenReturn(Mono.when());

        StepVerifier.create(stationService.findLastWeathersOnStationByStationCode("testCode1"))
                .consumeNextWith(st -> {
                    Assertions.assertEquals(stationMapper.map(station2), st);
                    Assertions.assertEquals(weather2, st.getWeather());
                    Assertions.assertNotEquals(stationMapper.map(station1), st);
                })
                .verifyComplete();
    }
}
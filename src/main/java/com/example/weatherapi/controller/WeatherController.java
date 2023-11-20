package com.example.weatherapi.controller;

import com.example.weatherapi.dto.StationDto;
import com.example.weatherapi.mapper.StationMapper;
import com.example.weatherapi.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WeatherController {

    private final StationService stationService;
    private final StationMapper stationMapper;

    @GetMapping("/stations")
    public Flux<StationDto> getStations() {
        return stationService.findAvailableStations().map(stationMapper::map);
    }

    @GetMapping("/stations/{station-code}")
    public Mono<StationDto> findWeathersOnStation(@PathVariable("station-code") String stationId) {
        return stationService.findWeathersOnStationByStationCode(stationId);
    }
}

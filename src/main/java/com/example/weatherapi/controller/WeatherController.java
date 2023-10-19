package com.example.weatherapi.controller;

import com.example.weatherapi.dto.StationDto;
import com.example.weatherapi.dto.WeatherDto;
import com.example.weatherapi.mapper.WeatherMapper;
import com.example.weatherapi.repository.StationDao;
import com.example.weatherapi.repository.WeatherDao;
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


    private final WeatherDao weatherDao;
    private final StationService stationService;
    private final WeatherMapper weatherMapper;
    private final StationDao stationDao;


    @GetMapping("/stations")
    public Flux<StationDto> getStations() {
        return stationService.findAvailableStations();
    }

    @GetMapping("/weather/{station-code}")
    public Flux<WeatherDto> getWeather(@PathVariable("station-code") String stationCode) {
        return weatherDao.findAllByStationCode(stationCode)
                .flatMap(weather -> stationDao.findById(weather.getStationCode())
                        .map(station -> {
                            weather.setStation(station);
                            return weather;
                        }).map(weatherMapper::map));
    }

    @GetMapping("/stations/{station-code}")
    public Mono<StationDto> findWeathersOnStation(@PathVariable("station-code") String stationId) {
        return stationService.findWeathersOnStationByStationCode(stationId);
    }
}

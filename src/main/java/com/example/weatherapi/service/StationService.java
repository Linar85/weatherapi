package com.example.weatherapi.service;

import com.example.weatherapi.dto.StationDto;
import com.example.weatherapi.entity.Station;
import com.example.weatherapi.entity.Weather;
import com.example.weatherapi.mapper.StationMapper;
import com.example.weatherapi.repository.StationDao;
import com.example.weatherapi.repository.WeatherDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StationService {

    private final StationDao stationDao;
    private final WeatherDao weatherDao;
    private final StationMapper stationMapper;

    public Flux<StationDto> findAvailableStations() {
        return stationDao.findAll().map(stationMapper::map);
    }

    public Mono<StationDto> findWeathersOnStationByStationCode(String stationCode) {
        return Mono.zip(stationDao.findByStationCode(stationCode), weatherDao.findAllByStationCode(stationCode).collectList())
                .map(tuples -> {
                    Station station = tuples.getT1();
                    List<Weather> weathers = tuples.getT2();
                    station.setWeathers(weathers);
                    return stationMapper.map(station);
                });
    }
}

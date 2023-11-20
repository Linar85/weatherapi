package com.example.weatherapi.service;

import com.example.weatherapi.dto.StationDto;
import com.example.weatherapi.entity.Station;
import com.example.weatherapi.entity.Weather;
import com.example.weatherapi.mapper.StationMapper;
import com.example.weatherapi.repository.StationDao;
import com.example.weatherapi.repository.StationRedisDao;
import com.example.weatherapi.repository.WeatherDao;
import com.example.weatherapi.repository.WeatherRedisDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StationService {

    private final StationDao stationDao;
    private final WeatherDao weatherDao;
    private final StationMapper stationMapper;
    private final StationRedisDao stationRedisDao;
    private final WeatherRedisDao weatherRedisDao;

    /**
     * идем в редис, если там нету данных, идем в БД, полученные данные пишем в редис затем отдаем их
     */
    public Flux<Station> findAvailableStations() {
        return stationRedisDao.findAll().switchIfEmpty(
                stationDao.findAll().publishOn(Schedulers.boundedElastic())
                        .map(st -> {
                            stationRedisDao.save(st).subscribe();
                            return st;
                        }));
    }

    public Mono<StationDto> findWeathersOnStationByStationCode(String stationCode) {
        return Mono.zip(stationRedisDao.findByStationCode(stationCode)
                                .switchIfEmpty(
                                        stationDao.findByStationCode(stationCode)
                                                .publishOn(Schedulers.boundedElastic())
                                                .map(st -> {
                                                    stationRedisDao.save(st).subscribe();
                                                    return st;
                                                })),
                        weatherRedisDao.findByStationCode(stationCode)
                                .switchIfEmpty(weatherDao.findAllByStationCode(stationCode))
                                .publishOn(Schedulers.boundedElastic())
                                .map(weather -> {
                                    weatherRedisDao.saveWeather(weather).subscribe();
                                    return weather;
                                })
                                .collectList())
                .map(tuples -> {
                    Station station = tuples.getT1();
                    List<Weather> weathers = tuples.getT2();
                    station.setWeathers(weathers);
                    return stationMapper.map(station);
                });
    }
}

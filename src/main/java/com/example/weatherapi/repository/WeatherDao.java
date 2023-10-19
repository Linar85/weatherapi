package com.example.weatherapi.repository;

import com.example.weatherapi.entity.Weather;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface WeatherDao extends R2dbcRepository<Weather, String> {

    Flux<Weather> findAllByStationCode(String stationId);
}

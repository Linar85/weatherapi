package com.example.weatherapi.repository;

import com.example.weatherapi.entity.Station;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface StationDao extends R2dbcRepository<Station, String> {

     Mono<Station> findByStationCode(String stationCode);
}
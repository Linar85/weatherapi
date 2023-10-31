package com.example.weatherapi.utils;

import com.example.weatherapi.entity.Station;
import com.example.weatherapi.entity.Weather;
import com.example.weatherapi.repository.StationDao;
import com.example.weatherapi.repository.StationRedisDao;
import com.example.weatherapi.repository.WeatherDao;
import com.example.weatherapi.repository.WeatherRedisDao;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.instancio.Instancio;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import static org.instancio.Select.field;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataUpdater {

    private final WeatherDao weatherDao;
    private final WeatherRedisDao weatherRedisDao;
    private final StationDao stationDao;
    private final StationRedisDao stationRedisDao;

    @Scheduled(fixedRate = 5000)
    public Flux<Long> weatherUpdate() {
        weatherRedisDao.evict().subscribe();

        Weather weather = Instancio.of(Weather.class)
                .generate(field("tempC"), gen -> gen.ints().range(-40, 45))
                .generate(field("windKph"), gen -> gen.ints().range(0, 20))
                .generate(field("windDir"), gen -> gen.oneOf("S", "SE", "E", "NE", "N", "NW", "W", "SW"))
                .generate(field("cloudOkt"), gen -> gen.ints().range(0, 8))
                .generate(field("cloudType"), gen -> gen.oneOf("Cb", "Cu", "St", "Cs", "As", "Ns", "Ci", "Cc", "Ac", "Sc"))
                .generate(field("conditionsText"), gen -> gen.oneOf("rain", "sun", "snow", "dust", "cloud"))
                .generate(field("conditionCode"), gen -> gen.ints().range(1, 5))
                .generate(field("stationCode"), gen -> gen.oneOf("OKT", "BLR", "STR", "UFA", "KZN", "SBY"))
                .ignore(field("station"))
                .ignore(field("id"))
                .create();

        weatherDao.save(weather).subscribe();
        log.info("TABLE Weather ON Postgres UPDATED");
        log.info("KEY Weather ON Redis UPDATED");
        weatherDao.findAll().flatMap(weatherRedisDao::save).subscribe();
        return Flux.just(1L);
    }

    @PostConstruct
    public Flux<Long> stationUpdate() {
        stationRedisDao.evict().subscribe();
        stationDao.saveAll(stationList()).subscribe();
        log.info("TABLE Stations ON Postgres UPDATED");
        log.info("KEY Stations ON Redis UPDATED");
        stationList().flatMap(stationRedisDao::save).subscribe();
        return Flux.just(1L);
    }

    private Flux<Station> stationList() {

        Station stationUfa = new Station();
        stationUfa.setStationCode("UFA");
        stationUfa.setName("Ufa");
        stationUfa.setCountry("RF");

        Station stationOkt = new Station();
        stationOkt.setStationCode("OKT");
        stationOkt.setName("Oktyabrsk");
        stationOkt.setCountry("RF");

        Station stationBlr = new Station();
        stationBlr.setStationCode("BLR");
        stationBlr.setName("Beloretsk");
        stationBlr.setCountry("RF");

        Station stationSby = new Station();
        stationSby.setStationCode("SBY");
        stationSby.setName("Sibay");
        stationSby.setCountry("RF");

        Station stationStr = new Station();
        stationStr.setStationCode("STR");
        stationStr.setName("Sterlitamak");
        stationStr.setCountry("RF");

        Station stationKzn = new Station();
        stationKzn.setStationCode("KZN");
        stationKzn.setName("Kazan");
        stationKzn.setCountry("RF");

        return Flux.just(stationBlr, stationKzn, stationOkt, stationUfa, stationStr, stationSby);
    }


}

package com.example.weatherapi.utils;

import com.example.weatherapi.entity.Station;
import com.example.weatherapi.entity.Weather;
import com.example.weatherapi.repository.StationDao;
import com.example.weatherapi.repository.StationRedisDao;
import com.example.weatherapi.repository.WeatherDao;
import com.example.weatherapi.repository.WeatherRedisDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.instancio.Instancio;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.instancio.Select.field;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataUpdater {

    private final WeatherDao weatherDao;
    private final WeatherRedisDao weatherRedisDao;
    private final StationDao stationDao;
    private final StationRedisDao stationRedisDao;
    private static List<String> stationCodes = new ArrayList<>();

    @Scheduled(initialDelay = 5000, fixedRate = 3 * 60 * 1000)
//    @Scheduled(cron = "0 0 0,3,6,9,12,15,18,21 * * *")
    public Mono<Void> weatherUpdate() {

        weatherRedisDao.evict().subscribe();

        for (String stationCode : stationCodes) {
            Weather weather = Instancio.of(Weather.class)
                    .generate(field("tempC"), gen -> gen.ints().range(-40, 45))
                    .generate(field("windKph"), gen -> gen.ints().range(0, 20))
                    .generate(field("windDir"), gen -> gen.oneOf("S", "SE", "E", "NE", "N", "NW", "W", "SW"))
                    .generate(field("cloudOkt"), gen -> gen.ints().range(0, 8))
                    .generate(field("cloudType"), gen -> gen.oneOf("Cb", "Cu", "St", "Cs", "As", "Ns", "Ci", "Cc", "Ac", "Sc"))
                    .generate(field("conditionsText"), gen -> gen.oneOf("rain", "sun", "snow", "dust", "cloud"))
                    .generate(field("conditionCode"), gen -> gen.ints().range(1, 5))
                    .set(field("createdAt"), LocalDateTime.now())
                    .set(field("stationCode"), stationCode)
                    .ignore(field("station"))
                    .ignore(field("id"))
                    .create();

            Mono.just(weather).flatMap(wth -> weatherDao.save(weather).then(weatherRedisDao.saveWeather(wth))).subscribe();
        }
        return Mono.when();
    }

    @EventListener(ApplicationReadyEvent.class)
    public Mono<Void> stationUpdate() {
        return stationList().flatMap(st -> stationDao.save(st)
                        .then(stationRedisDao.save(st))
                        .doOnSuccess(l -> {
                            log.info("TABLE Stations ON Postgres UPDATED");
                            log.info("KEY Stations ON Redis UPDATED");
                        }))
                .then();
    }

    private Flux<Station> stationList() {

        List<Station> stationList = new ArrayList<Station>();

        for (int i = 0; i < 500; i++) {
            Station station = Instancio.of(Station.class)
                    .generate(field("stationCode"), gen -> gen.text().pattern("#C#C#C#C#C"))
                    .generate(field("name"), gen -> gen.text().pattern("#C#c#c#c#c#c"))
                    .generate(field("country"), gen -> gen.text().pattern("#C#C"))
                    .ignore(field("weather"))
                    .ignore(field("id"))
                    .create();

            stationCodes.add(station.getStationCode());
            stationList.add(station);
        }
        return Flux.fromIterable(stationList);
    }
}

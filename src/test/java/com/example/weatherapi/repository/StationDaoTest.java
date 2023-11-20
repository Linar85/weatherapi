package com.example.weatherapi.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;
import reactor.test.StepVerifier;

import static java.lang.String.format;

@DataR2dbcTest
@ExtendWith(SpringExtension.class)
@Testcontainers
class StationDaoTest {

    @Autowired
    StationDao stationDao;

    @Container
    public static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("weatherapi")
            .withUsername("postgres")
            .withPassword("1234")
            .withInitScript("scripts/initTest.sql");

    @BeforeAll
    static void runContainer() {
        Startables.deepStart(postgreSQLContainer);
    }

    @DynamicPropertySource
    private static void setDatasourceProperties(DynamicPropertyRegistry registry) {

        // R2DBC DataSource Example
        registry.add("spring.r2dbc.url", () ->
                format("r2dbc:pool:postgresql://%s:%d/%s",
                        postgreSQLContainer.getHost(),
                        postgreSQLContainer.getFirstMappedPort(),
                        postgreSQLContainer.getDatabaseName()));
        registry.add("spring.r2dbc.username", postgreSQLContainer::getUsername);
        registry.add("spring.r2dbc.password", postgreSQLContainer::getPassword);
    }
    @Test
    void findByWrongStationCode() {

        StepVerifier.create(stationDao.findByStationCode("qwe"))
                .expectNextCount(0L)
                .verifyComplete();
    }
    @Test
    void findByStationCode() {

        StepVerifier.create(stationDao.findByStationCode("OKT"))
//                .expectNextCount(1L)
                .expectNextMatches(x -> x.getStationCode().equals("OKT")
                        && x.getCountry().equals("RF"))
                .verifyComplete();
    }
    @Test
    void findByCountByStationCode() {

        StepVerifier.create(stationDao.findByStationCode("OKT"))
                .expectNextCount(1L)
                .verifyComplete();
    }
}
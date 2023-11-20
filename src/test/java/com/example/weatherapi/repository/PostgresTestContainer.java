package com.example.weatherapi.repository;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static java.lang.String.format;

public abstract class PostgresTestContainer {

    public static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("weatherapi")
            .withUsername("postgres")
            .withPassword("1234");

    static {
        postgreSQLContainer.start();
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
}

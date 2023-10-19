package com.example.weatherapi.repository;

import com.example.weatherapi.entity.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface UserDao extends R2dbcRepository<User, Long> {

    //idea просит Mono
    User findByUsername(String username);

}

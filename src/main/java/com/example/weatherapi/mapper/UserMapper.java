package com.example.weatherapi.mapper;

import com.example.weatherapi.dto.UserDto;
import com.example.weatherapi.entity.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto map(User entity);

    @InheritInverseConfiguration
    User map(UserDto dto);
}

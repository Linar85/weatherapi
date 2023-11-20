-- truncate table weather_api.stations cascade ;
-- truncate table weather_api.weather cascade ;
-- truncate table weather_api.apikeys cascade ;
truncate table weather_api.users cascade ;
drop table if exists weather_api.weather;
drop table if exists weather_api.stations;
drop table if exists weather_api.ratelimits;
drop table if exists weather_api.apikeys;
drop table if exists weather_api.users cascade ;


create table if not exists weather_api.users
(
    id       serial primary key,
    username varchar(64) not null unique,
    password varchar(2048),
    role varchar(32) not null,
    first_name varchar(64) not null,
    last_name varchar(64) not null,
    enabled boolean not null default false,
    created_at  timestamp,
    updated_at  timestamp
    );

create table if not exists weather_api.apikeys
(
    id      serial primary key,
    user_Id  integer,
    api_key varchar(256) unique,
    created timestamp,
    constraint fk_user foreign key (user_Id) references weather_api.users (id)
    );

create table if not exists weather_api.ratelimits
(
    id         serial primary key,
    bucket_capacity integer,
    refill_greedy_tokens integer,
    refill_greedy_duration_seconds integer,
    user_id integer unique,
    constraint fk_user foreign key (user_id) references weather_api.users (id)
    );



create table if not exists weather_api.stations
(
    id      VARCHAR(36) unique primary key DEFAULT gen_random_uuid(),
    name    varchar(256),
    station_code varchar(3) unique,
    country varchar(256)
    );

create table if not exists weather_api.weather
(
    id              VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid(),
    temp_c          integer,
    wind_kph        integer,
    wind_dir        varchar(2),
    cloud_okt       varchar(3),
    cloud_type      varchar(2),
    conditions_text varchar(256),
    condition_code  integer,
    station_code    varchar(3),
    created_at      timestamp,
    constraint fk_weather foreign key (station_code) references weather_api.stations (station_code)
    );
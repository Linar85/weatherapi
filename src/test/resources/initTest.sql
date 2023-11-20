
drop table if exists weather;
drop table if exists stations;
drop table if exists ratelimits;
drop table if exists apikeys;
drop table if exists users cascade ;


create table if not exists users
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

create table if not exists apikeys
(
    id      serial primary key,
    user_Id  integer,
    api_key varchar(256) unique,
    created timestamp,
    constraint fk_user foreign key (user_Id) references users (id)
    );

create table if not exists ratelimits
(
    id         serial primary key,
    bucket_capacity integer,
    refill_greedy_tokens integer,
    refill_greedy_duration_seconds integer,
    user_id integer unique,
    constraint fk_user foreign key (user_id) references users (id)
    );



create table if not exists stations
(
    id      VARCHAR(36) unique primary key DEFAULT gen_random_uuid(),
    name    varchar(256),
    station_code varchar(3) unique,
    country varchar(256)
    );

create table if not exists weather
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
    constraint fk_weather foreign key (station_code) references stations (station_code)
    );


INSERT INTO stations (name, station_code, country) VALUES ('Ufa', 'UFA', 'RF');
INSERT INTO stations (name, station_code, country) VALUES ('Kazan', 'KZN', 'RF');
INSERT INTO stations (name, station_code, country) VALUES ('Sterlitamak', 'STR', 'RF');
INSERT INTO stations (name, station_code, country) VALUES ('Sibay', 'SBY', 'RF');
INSERT INTO stations (name, station_code, country) VALUES ('Beloretsk', 'BLR', 'RF');
INSERT INTO stations (name, station_code, country) VALUES ('Oktyabrsk', 'OKT', 'RF');

INSERT INTO weather (temp_c, wind_kph, wind_dir, cloud_okt, cloud_type, conditions_text, condition_code, station_code) VALUES (2, 5, 'NE', 6, 'QQ', 'rain', 1, 'UFA');
INSERT INTO weather (temp_c, wind_kph, wind_dir, cloud_okt, cloud_type, conditions_text, condition_code, station_code) VALUES (12, 6, 'S', 6, 'QQ', 'sun', 1, 'UFA');
INSERT INTO weather (temp_c, wind_kph, wind_dir, cloud_okt, cloud_type, conditions_text, condition_code, station_code) VALUES (3, 4, 'S', 3, 'WW', 'sun', 2, 'KZN');
INSERT INTO weather (temp_c, wind_kph, wind_dir, cloud_okt, cloud_type, conditions_text, condition_code, station_code) VALUES (4, 0, 'SE', 1, 'EE', 'snow', 3, 'SBY');
INSERT INTO weather (temp_c, wind_kph, wind_dir, cloud_okt, cloud_type, conditions_text, condition_code, station_code) VALUES (5, 20, 'W', 9, 'RR', 'cloud', 4, 'BLR');
INSERT INTO weather (temp_c, wind_kph, wind_dir, cloud_okt, cloud_type, conditions_text, condition_code, station_code) VALUES (6, 7, 'W', 6, 'TT', 'dust', 5, 'OKT');
INSERT INTO weather (temp_c, wind_kph, wind_dir, cloud_okt, cloud_type, conditions_text, condition_code, station_code) VALUES (7, 9, 'E', 2, 'FF', 'rain', 1, 'STR');

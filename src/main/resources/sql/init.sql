drop table if exists weather_api.weather;
drop table if exists weather_api.stations;
drop table if exists weather_api.ratelimits;
drop table if exists weather_api.apikeys;
drop table if exists weather_api.users;


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
    userId  integer,
    api_key varchar(256) unique,
    created timestamp,
    updated timestamp,
    constraint fk_user foreign key (userId) references weather_api.users (id)
);

create table if not exists weather_api.ratelimits
(
    id         serial primary key,
    api_key_id varchar(256),
    rate_limit integer,
    constraint fk_apikey foreign key (api_key_id) references weather_api.apikeys (api_key)
);



create table if not exists weather_api.stations
(
    id      VARCHAR(36)  DEFAULT gen_random_uuid(),
    name    varchar(256),
    station_code varchar(3) unique PRIMARY KEY ,
    country varchar(256)
);

create table if not exists weather_api.weather
(
    id              VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid(),
    temp_c          real,
    wind_kph        integer,
    wind_dir        varchar(2),
    cloud_okt       varchar(3),
    cloud_type      varchar(2),
    conditions_text varchar(256),
    condition_code  integer,
    station_code    varchar(3),
    constraint fk_weather foreign key (station_code) references weather_api.stations (station_code)
);

INSERT INTO weather_api.stations (name, station_code, country) VALUES ('Ufa', 'UFA', 'RF');
INSERT INTO weather_api.stations (name, station_code, country) VALUES ('Kazan', 'KZN', 'RF');
INSERT INTO weather_api.stations (name, station_code, country) VALUES ('Sterlitamak', 'STR', 'RF');
INSERT INTO weather_api.stations (name, station_code, country) VALUES ('Sibay', 'SBY', 'RF');
INSERT INTO weather_api.stations (name, station_code, country) VALUES ('Beloretsk', 'BLR', 'RF');
INSERT INTO weather_api.stations (name, station_code, country) VALUES ('Oktyabrsk', 'OKT', 'RF');

INSERT INTO weatherapi.weather_api.weather (temp_c, wind_kph, wind_dir, cloud_okt, cloud_type, conditions_text, condition_code, station_code) VALUES (2, 5, 'NE', 6, 'QQ', 'rain', 1, 'UFA');
INSERT INTO weatherapi.weather_api.weather (temp_c, wind_kph, wind_dir, cloud_okt, cloud_type, conditions_text, condition_code, station_code) VALUES (12, 6, 'S', 6, 'QQ', 'sun', 1, 'UFA');
INSERT INTO weatherapi.weather_api.weather (temp_c, wind_kph, wind_dir, cloud_okt, cloud_type, conditions_text, condition_code, station_code) VALUES (3, 4, 'S', 3, 'WW', 'sun', 2, 'KZN');
INSERT INTO weatherapi.weather_api.weather (temp_c, wind_kph, wind_dir, cloud_okt, cloud_type, conditions_text, condition_code, station_code) VALUES (4, 0, 'SE', 1, 'EE', 'snow', 3, 'SBY');
INSERT INTO weatherapi.weather_api.weather (temp_c, wind_kph, wind_dir, cloud_okt, cloud_type, conditions_text, condition_code, station_code) VALUES (5, 20, 'W', 9, 'RR', 'cloud', 4, 'BLR');
INSERT INTO weatherapi.weather_api.weather (temp_c, wind_kph, wind_dir, cloud_okt, cloud_type, conditions_text, condition_code, station_code) VALUES (6, 7, 'W', 6, 'TT', 'dust', 5, 'OKT');
INSERT INTO weatherapi.weather_api.weather (temp_c, wind_kph, wind_dir, cloud_okt, cloud_type, conditions_text, condition_code, station_code) VALUES (7, 9, 'E', 2, 'FF', 'rain', 1, 'STR');

select * from weather_api.weather where station_code='UFA';
select * from weather_api.stations;



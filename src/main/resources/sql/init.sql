create table if not exists users
(
    id       serial primary key,
    password varchar(2048),
    created  timestamp
);

create table if not exists apikeys
(
    id      serial primary key,
    userId  integer,
    api_key varchar(256),
    created timestamp,
    updated timestamp,
    constraint fk_user foreign key (userId) references users (id)
);

create table if not exists ratelimits
(
    id         serial primary key,
    api_key_id varchar(256),
    rate_limit integer,
    constraint fk_apikey foreign key (api_key_id) references apikeys (api_key)
);

create table if not exists weather
(
    id              serial primary key,
    temp_c          real,
    wind_kph        integer,
    wind_dir        varchar(2),
    cloud_okt       varchar(3),
    cloud_type      varchar(2),
    conditions_text varchar(256),
    condition_code  integer
);

create table if not exists stations
(
    id      serial primary key,
    name    varchar(256),
    country varchar(256),
    weather serial,
    constraint fk_weather foreign key (weather) references weather (id)
);





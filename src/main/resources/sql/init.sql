-- create table if not exists users
-- (
--     id       serial primary key,
--     password varchar(2048),
--     created  timestamp
-- );
--
-- create table if not exists apikeys
-- (
--     id      serial primary key,
--     userId  integer,
--     api_key varchar(256),
--     created timestamp,
--     updated timestamp,
--     constraint fk_user foreign key (userId) references users (id)
-- );
--
-- create table if not exists ratelimits
-- (
--     id         serial primary key,
--     api_key_id varchar(256),
--     rate_limit integer,
--     constraint fk_apikey foreign key (api_key_id) references apikeys (api_key)
-- );
--
-- create table if not exists weather
-- (
--     id              serial primary key,
--     temp_c          real,
--     wind_kph        integer,
--     wind_dir        varchar(2),
--     cloud_okt       integer,
--     cloud_type      varchar(2),
--     conditions_text varchar(256),
--     condition_code  integer
-- );
--
-- create table if not exists stations
-- (
--     id      serial primary key,
--     name    varchar(256),
--     country varchar(256),
--     weather serial,
--     constraint fk_weather foreign key (weather) references weather (id)
-- );

INSERT INTO weatherapi.weather_api.weather (id, temp_c, wind_kph, wind_dir, cloud_okt, cloud_type, conditions_text, condition_code) VALUES (2, 5, 'NE', 6, 'QQ', 'rain', 1);
INSERT INTO weatherapi.weather_api.weather (id, temp_c, wind_kph, wind_dir, cloud_okt, cloud_type, conditions_text, condition_code) VALUES (3, 4, 'S', 3, 'WW', 'sun', 2);
INSERT INTO weatherapi.weather_api.weather (id, temp_c, wind_kph, wind_dir, cloud_okt, cloud_type, conditions_text, condition_code) VALUES (4, 0, 'SE', 1, 'EE', 'snow', 3);
INSERT INTO weatherapi.weather_api.weather (id, temp_c, wind_kph, wind_dir, cloud_okt, cloud_type, conditions_text, condition_code) VALUES (5, 20, 'W', 9, 'RR', 'cloud', 4);
INSERT INTO weatherapi.weather_api.weather (id, temp_c, wind_kph, wind_dir, cloud_okt, cloud_type, conditions_text, condition_code) VALUES (6, 7, 'W', 6, 'TT', 'dust', 5);
INSERT INTO weatherapi.weather_api.weather (id, temp_c, wind_kph, wind_dir, cloud_okt, cloud_type, conditions_text, condition_code) VALUES (7, 9, 'E', 2, 'FF', 'rain', 1);




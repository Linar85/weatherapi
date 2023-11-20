
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

INSERT INTO users (username, password, role, first_name, last_name) VALUES ('user2', 'userPassword2', 'USER', 'firstName2', 'lastName2');
INSERT INTO users (username, password, role, first_name, last_name) VALUES ('user1', 'userPassword1', 'ADMIN', 'firstName1', 'lastName1');

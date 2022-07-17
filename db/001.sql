create table role (
    id serial primary key,
    role varchar not null unique,
);

create table person_room (
    person_id int references person(id),
    room_id int references room(id)
);

create table person (
    id serial primary key,
    person_name varchar not null unique,
    created timestamp,
    role_id references role(id),
);

create table room (
    id serial primary key,
    room_name varchar not null unique,
    created timestamp,
);

create table message (
    id serial primary key,
    description varchar,
    created timestamp,
    person_id references person(id),
    room_id references room(id)
)
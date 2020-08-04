create sequence usr_sequence start 1 increment 50;

create sequence rate_sequence start 1 increment 50;

create sequence conversion_sequence start 1 increment 50;

create table usr (
    id bigint,
    username varchar(255) not null,
    password varchar(255) not null,
    primary key (id),
    constraint username_unique unique (username)
);

create table currency (
    id varchar(7),
    num_code varchar(3),
    char_code varchar(3),
    name varchar(255),
    primary key (id)
);

create table rate (
    id bigint,
    nominal integer,
    value numeric(19, 2),
    date date,
    currency_id varchar(7),
    primary key (id),
    constraint currency_id_fk foreign key (currency_id) references currency,
    constraint rate_unique unique (currency_id, date)
);

create table conversion (
    id bigint,
    amount numeric(19, 2),
    converted_amount numeric(19, 2),
    date date,
    source_currency_id varchar(7),
    target_currency_id varchar(7),
    user_id bigint,
    primary key (id),
    constraint source_currency_fk foreign key (source_currency_id) references currency,
    constraint target_currency_fk foreign key (target_currency_id) references currency,
    constraint user_fk foreign key (user_id) references usr
);

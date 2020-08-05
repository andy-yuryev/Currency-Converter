create table usr (
    id bigint primary key generated always as identity,
    username varchar(255) not null,
    password varchar(255) not null,
    constraint username_unique unique (username)
);

create table currency (
    id varchar(7) primary key,
    num_code varchar(3) not null,
    char_code varchar(3) not null,
    name varchar(255) not null
);

create table rate (
    id bigint primary key generated always as identity,
    nominal integer not null,
    value numeric(19, 2) not null,
    date date not null,
    currency_id varchar(7) not null,
    constraint rate_unique unique (currency_id, date),
    constraint currency_id_fk foreign key (currency_id) references currency
);

create table conversion (
    id bigint primary key generated always as identity,
    amount numeric(19, 2) not null,
    converted_amount numeric(19, 2) not null,
    date date not null,
    source_currency_id varchar(7) not null,
    target_currency_id varchar(7) not null,
    user_id bigint not null,
    constraint source_currency_fk foreign key (source_currency_id) references currency,
    constraint target_currency_fk foreign key (target_currency_id) references currency,
    constraint user_fk foreign key (user_id) references usr
);

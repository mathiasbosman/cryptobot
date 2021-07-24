create table "cryptos"
(
    "id"               uuid        not null default random_uuid(),
    constraint cryptos_pk primary key ("id"),
    "code"             varchar(10) not null,
    constraint cryptos_code_uIndex unique ("code"),
    "re_buy_at"        float8,
    "profit_threshold" float8,
    "stop_threshold"   float8
);
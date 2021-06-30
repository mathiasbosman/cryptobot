create table "cryptos"
(
    "id"              uuid        not null default gen_random_uuid(),
    constraint cryptos_pk primary key ("id"),
    "code"            varchar(10) not null,
    constraint cryptos_code_uidex unique ("code"),
    "rebuy_at"        float8,
    "profit_treshold" float8
);
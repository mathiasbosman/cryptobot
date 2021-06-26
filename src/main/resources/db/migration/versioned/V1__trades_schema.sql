create table "trades"
(
    "id"          uuid        not null,
    constraint orders_pk primary key ("id"),
    "order_id"    varchar(50) not null,
    "market_code" varchar(50) not null,
    "order_type"  varchar(50) not null,
    "order_side"  varchar(50) not null,
    "taker"       bool,
    "amount"      float8,
    "price"       float8,
    "fee_paid"    float8,
    "timestamp"   timestamp
);
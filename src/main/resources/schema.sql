-- Corporation Information Entity
drop table if exists corp_info;
create table corp_info
(
    stock_code varchar(6)  not null unique,
    corp_name  varchar(30) not null,
    constraint pk_corp_info primary key (
                                         stock_code,
                                         corp_name
        )
);
-- Daily Market Amount Entity
drop table if exists daily_market_amt;
create table daily_market_amt
(
    post_date  varchar(8) not null,
    stock_code varchar(6) not null,
    high       bigint,
    low        bigint,
    open       bigint,
    close      bigint,
    volume     bigint,
    constraint pk_daily_market_amt primary key (
                                                post_date,
                                                stock_code
        )
);
create unique index uk_daily_market_amt_01
    on daily_market_amt (
                         stock_code,
                         post_date
        );
create table countries
(
    code varchar(2) primary key,
    name         text not null,
    slug         text not null
);

create table covid_cases
(
    id           bigserial primary key,
    date         date not null,
    new_cases    int4 not null,
    country_code varchar(2) references countries (code)
);

alter sequence covid_cases_id_seq increment by 50;

create index idx_covid_cases_date on covid_cases (date);
create index idx_covid_cases_country_code on covid_cases (country_code);
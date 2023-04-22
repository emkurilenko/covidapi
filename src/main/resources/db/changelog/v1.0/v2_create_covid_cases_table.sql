create table covid_cases
(
    id           bigserial primary key,
    date         date not null,
    new_cases    int4 not null,
    country_code varchar(2) references countries (code)
);

create index idx_covid_cases_date on covid_cases (date);
create index idx_covid_cases_country_code on covid_cases (country_code);
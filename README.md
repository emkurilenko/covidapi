## Task:

Develop and implement an API that allows you to find for a specified list of countries:  The
maximum/minimum number of new cases per day during the selected day/period. Retrieve data through
the API: https://covid19api.com/

Restrictions:

It is necessary to minimize the number of requests to the source website during the daily use of the
application.

Use Java 17+, Spring Boot/Micronaut.

## Solution

Implemented 2 endpoints

1. Get available countries (com.kurilenko.covidapi.controller.CountryController)

GET: /api/country

2. Get max and min statistic for countries (com.kurilenko.covidapi.controller.CovidStatsController)

GET: /api/statistics/new-cases

Implemented 3 jobs 
1. For filling db with countries from API (auto run once after start application)
2. For filling db with new covid cases for countries which not data existed 
3. For filling with daily summary new cases

# Start application

In terminal run command:
`docker-compose up -d`

After that 
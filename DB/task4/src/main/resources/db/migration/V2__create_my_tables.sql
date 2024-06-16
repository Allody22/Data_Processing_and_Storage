CREATE TABLE price_full_one_race_analysis
(
    flight_uiq                          BIGINT UNIQUE NOT NULL,
    flight_no                           VARCHAR(255), -- Добавлено это поле
    departure_time                      TIMESTAMPTZ,
    arrival_time                        TIMESTAMPTZ,
    arrival_airport_name                VARCHAR(255),
    departure_airport_name              VARCHAR(255),
    departure_city                      VARCHAR(255),
    arrival_city                        VARCHAR(255),
    total_price                         NUMERIC,      -- Суммарная цена за все билеты на рейс
    total_seats_number                  INTEGER,      -- Количество мест всего
    sold_seats_number                   INTEGER,      -- Количество уже проданных мест
    average_price_for_one_seat          NUMERIC,      -- Суммарная цена за все билеты на рейс
    aircraft_code                       VARCHAR(255),
    total_seats_business                INTEGER,      -- Всего мест бизнес
    total_seats_economy                 INTEGER,      -- Всего мест эконом
    total_seats_comfort                 INTEGER,      -- Всего мест комфорт
    sold_seats_business                 INTEGER,      -- Количество уже проданных мест бизнес
    sold_seats_comfort                  INTEGER,      -- Количество уже проданных мест комфорт
    sold_seats_economy                  INTEGER,      -- Количество уже проданных эконом
    average_price_for_one_comfort_seat  INTEGER,
    average_price_for_one_business_seat INTEGER,
    average_price_for_one_economy_seat  INTEGER,

    PRIMARY KEY (flight_uiq)                          -- Установлено flight_uiq как первичный ключ
);


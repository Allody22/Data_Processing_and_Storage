CREATE TABLE price_full_one_race_analysis
(
    flight_uiq                 BIGINT UNIQUE NOT NULL, -- Изменено с VARCHAR(255) на BIGINT
    flight_no                  VARCHAR(255),           -- Добавлено это поле
    departure_time             TIMESTAMPTZ,
    arrival_time               TIMESTAMPTZ,
    arrival_airport_name       VARCHAR(255),
    departure_airport_name     VARCHAR(255),
    departure_city             VARCHAR(255),
    arrival_city               VARCHAR(255),
    total_price                NUMERIC,                -- Суммарная цена за все билеты на рейс
    total_seats_number         INTEGER,                -- Количество мест
    sold_seats_number          INTEGER,                -- Количество мест
    average_price_for_one_seat NUMERIC,                -- Суммарная цена за все билеты на рейс
    aircraft_code              VARCHAR(255),
    PRIMARY KEY (flight_uiq)                           -- Установлено flight_uiq как первичный ключ
);


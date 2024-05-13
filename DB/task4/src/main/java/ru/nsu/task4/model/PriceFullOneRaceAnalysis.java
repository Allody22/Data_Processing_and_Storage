package ru.nsu.task4.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "price_full_one_race_analysis")
@Data
public class PriceFullOneRaceAnalysis {

    @Id
    @Column(name = "flight_uiq", unique = true)
    private Long flightUid;

    @Column(name = "flight_no")
    private String flightNumber;

    @Column(name = "departure_time")
    private OffsetDateTime departureTime;

    @Column(name = "arrival_time")
    private OffsetDateTime arrivalTime;

    @Column(name = "arrival_airport_name")
    private String arrivalAirportName;

    @Column(name = "departure_airport_name")
    private String departureAirportName;

    @Column(name = "departure_city")
    private String departureCity;

    @Column(name = "arrival_city")
    private String arrivalCity;

    @Column(name = "total_price")
    private BigDecimal totalPrice; // Суммарная цена за все билеты на рейс

    @Column(name = "total_seats_number")
    private Integer totalSeatsNumber; // Количество мест

    @Column(name = "sold_seats_number")
    private Integer soldSeatsNumber; // Количество купленных мест

    @Column(name = "average_price_for_one_seat")
    private BigDecimal averagePriceForOneSeat; //средняя цена за одно место

    @Column(name = "average_price_for_one_economy_seat")
    private BigDecimal averagePriceForOneEconomySeat; //средняя цена за одно эконом место

    @Column(name = "average_price_for_one_business_seat")
    private BigDecimal averagePriceForOneBusinessSeat; //средняя цена за одно бизнес место

    @Column(name = "average_price_for_one_comfort_seat")
    private BigDecimal averagePriceForOneComfortSeat; //средняя цена за одно место

    @Column(name = "aircraft_code")
    private String aircraftCode; // код самолёта

    @Column(name = "total_seats_business")
    private Integer totalSeatsBusiness; // Количество всего мест бизнес

    @Column(name = "total_seats_economy")
    private Integer totalSeatsEconomy; // Количество всего эконом мест

    @Column(name = "total_seats_comfort")
    private Integer totalSeatsComfort; // Количество всего мест комфорт

    @Column(name = "sold_seats_business")
    private Integer soldSeatsBusiness; // Количество купленных мест бизнес

    @Column(name = "sold_seats_economy")
    private Integer soldSeatsEconomy; // Количество купленных эконом мест

    @Column(name = "sold_seats_comfort")
    private Integer soldSeatsComfort; // Количество купленных мест комфорт

}

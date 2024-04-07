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

    @Column(name = "departure_city")
    private String departureCity;

    @Column(name = "arrival_city")
    private String arrivalCity;

    @Column(name = "total_price")
    private BigDecimal totalPrice; // Суммарная цена за все билеты на рейс

    @Column(name = "total_seats_number")
    private Integer totalSeatsNumber; // Количество мест

    @Column(name = "sold_seats_number")
    private Integer soldSeatsNumber; // Количество мест

    @Column(name = "average_price_for_one_seat")
    private BigDecimal averagePriceForOneSeat; //средняя цена за одно место

    @Column(name = "aircraft_code")
    private String aircraftCode; //aircraftCode

}

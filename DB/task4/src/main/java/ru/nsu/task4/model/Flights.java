package ru.nsu.task4.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.OffsetDateTime;


@Entity
@Table(name = "flights")
@Data
public class Flights {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flights_flight_id_seq")
    @SequenceGenerator(name = "flights_flight_id_seq", sequenceName = "flights_flight_id_seq", allocationSize = 1)
    @Column(name = "flight_id", nullable = false)
    private Long flightId;

    @Column(name = "flight_no", nullable = false, length = 6)
    private String flightNo;

    @Column(name = "scheduled_departure", nullable = false)
    private OffsetDateTime scheduledDeparture;

    @Column(name = "scheduled_arrival", nullable = false)
    private OffsetDateTime scheduledArrival;

    @ManyToOne
    @JoinColumn(name = "departure_airport", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private Airport departureAirport;

    @ManyToOne
    @JoinColumn(name = "arrival_airport", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private Airport arrivalAirport;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "aircraft_code", nullable = false, length = 3)
    private String aircraftCode;

    @Column(name = "actual_departure")
    private OffsetDateTime actualDeparture;

    @Column(name = "actual_arrival")
    private OffsetDateTime actualArrival;
}

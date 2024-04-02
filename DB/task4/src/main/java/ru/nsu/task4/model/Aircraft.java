package ru.nsu.task4.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "aircrafts_data")
@Data
public class Aircraft {
    @Id
    @Column(name = "aircraft_code")
    private String aircraftCode; // Aircraft code, IATA

    @Column(name = "model", columnDefinition = "jsonb")
    private String model; // JSON String for Aircraft model

    @Column(name = "range")
    private Integer range; // Maximal flying distance, km
}

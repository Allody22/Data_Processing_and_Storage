package ru.nsu.task4.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "airports_data")
@Data
public class Airport {

    @Id
    @Column(name = "airport_code")
    private String airportCode;

    @Column(name = "airport_name", columnDefinition = "jsonb")
    private String airportName; // JSON-строка

    @Column(name = "city", columnDefinition = "jsonb")
    private String city; // JSON-строка

    @Column(name = "timezone")
    private String timezone; //Airport time zone
}

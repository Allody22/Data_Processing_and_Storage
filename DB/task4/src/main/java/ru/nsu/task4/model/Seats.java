package ru.nsu.task4.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "seats")
@Data
public class Seats {

    @Id
    @Column(name = "seat_no", nullable = false, length = 5)
    private String seatNo;

    @ManyToOne
    @JoinColumn(name = "aircraft_code", nullable = false)
    private Aircraft aircraft;

    @Column(name = "fare_conditions", nullable = false, length = 11)
    private String fareCondition;

    public void setAircraft(Aircraft aircraft) {
        if (aircraft != null && aircraft.getAircraftCode() != null) {
            aircraft.setAircraftCode(aircraft.getAircraftCode().trim());
        }
        this.aircraft = aircraft;
    }
}

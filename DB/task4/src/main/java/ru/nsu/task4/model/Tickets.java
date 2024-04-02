package ru.nsu.task4.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "tickets")
@Data
public class Tickets {

    @Id
    @Column(name = "ticket_no", nullable = false, length = 14)
    private String aircraftCode;

    @Column(name = "passenger_id", nullable = false, length = 21)
    private String passengerId;

    @Column(name = "passenger_name", nullable = false)
    private String passengerName;

    @Column(name = "contact_data", columnDefinition = "jsonb")
    private String contactData;

    @ManyToOne
    @JoinColumn(name = "book_ref", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private Bookings booking;
}

package ru.nsu.task4.model;

import lombok.Data;
import ru.nsu.task4.model.ids.TicketFlightId;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "ticket_flights")
@Data
public class TicketFlights implements Serializable {

    @EmbeddedId
    private TicketFlightId id;

    @MapsId("ticketNo")
    @ManyToOne
    @JoinColumn(name = "ticket_no", nullable = false)
    private Tickets ticket;

    @MapsId("flightId")
    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flights flight;

    @Column(name = "fare_conditions", nullable = false, length = 11)
    private String fareCondition; //Travel class

    @Column(name = "amount", nullable = false)
    private BigDecimal amount; // Цена на билет на поездку (Travel cost)
}

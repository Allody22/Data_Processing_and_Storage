package ru.nsu.task4.model;

import lombok.Data;
import ru.nsu.task4.model.ids.BoardingPassId;

import javax.persistence.*;

@Entity
@Table(name = "boarding_passes")
@Data
@IdClass(BoardingPassId.class) // Указываем класс идентификатора
public class BoardingPass {

    @Id
    @Column(name = "ticket_no")
    private String ticketNo; // Должен совпадать с типом и именем в BoardingPassId

    @Id
    @Column(name = "flight_id")
    private Long flightId; // Должен совпадать с типом и именем в BoardingPassId

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ticket_no", referencedColumnName = "ticket_no", insertable = false, updatable = false),
            @JoinColumn(name = "flight_id", referencedColumnName = "flight_id", insertable = false, updatable = false)
    })
    private TicketFlights ticketFlight;

    @Column(name = "boarding_no", nullable = false)
    private Integer boardingNo; //Boarding pass number

    @Column(name = "seat_no", nullable = false, length = 4)
    private String seatNo; //Seat number
}

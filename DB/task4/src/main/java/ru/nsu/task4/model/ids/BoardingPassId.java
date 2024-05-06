package ru.nsu.task4.model.ids;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class BoardingPassId implements Serializable {
    private String ticketNo; // Тип должен совпадать с типом в TicketFlights и Tickets
    private Long flightId; // Тип должен совпадать с типом в TicketFlights и Flights

    public BoardingPassId() {
    }

    public BoardingPassId(String ticketNo, Long flightId) {
        this.ticketNo = ticketNo;
        this.flightId = flightId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardingPassId)) return false;
        BoardingPassId that = (BoardingPassId) o;
        return Objects.equals(ticketNo, that.ticketNo) &&
                Objects.equals(flightId, that.flightId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketNo, flightId);
    }
}

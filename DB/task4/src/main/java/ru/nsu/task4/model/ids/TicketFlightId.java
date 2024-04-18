package ru.nsu.task4.model.ids;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketFlightId implements Serializable {
    private String ticketNo;
    private Long flightId;

}

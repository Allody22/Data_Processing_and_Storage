package ru.nsu.task4.payloads.requests;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class BookingRaceRequest {

    @Schema(description = "Список айди рейсов, на которые записался человек.", example = "123321", required = true)
    private List<Long> flightId;

    @Schema(description = "Класс билета (Business, Economy или Comfort).", example = "Economy", required = true)
    private String fareCondition;

    @Schema(description = "Айди пассажира.", example = "8149 604011", required = true)
    private String passengerId;

    @Schema(description = "ФИО пассажира.", example = "TAMARA ZAYCEVA", required = true)
    private String passengerName;

    @Schema(description = "Контакты пассажира.", example = "{\"email\": \"t_petrova1970@postgrespro.ru\", \"phone\": \"+70886117503\"}")
    private JsonNode passengerContact;
}

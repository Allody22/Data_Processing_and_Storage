package ru.nsu.task4.payloads.requests;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookingRaceRequest {

    @Schema(description = "Айди рейса.", example = "123321", required = true)
    private Long flightId;

    @Schema(description = "Класс билета (Business, Economy или Comfort).", example = "Economy", required = true)
    private String fareCondition;

    @Schema(description = "Номер места. Может быть как просто число так и комбинация число + строка.", example = "13B", required = true)
    private String seatNumber;

    @Schema(description = "Цена бронирования.", example = "12343", required = true)
    private BigDecimal price;

    @Schema(description = "Айди пассажира.", example = "8149 604011", required = true)
    private String passengerId;

    @Schema(description = "ФИО пассажира.", example = "TAMARA ZAYCEVA", required = true)
    private String passengerName;

    @Schema(description = "Контакты пассажира.", example = "{\"email\": \"t_petrova1970@postgrespro.ru\", \"phone\": \"+70886117503\"}")
    private JsonNode passengerContact;
}

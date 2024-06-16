package ru.nsu.task4.payloads.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingResponse {

    @Schema(description = "Сгенерированный номер билета.", example = "12321 321312")
    private String ticketNumber;

    @Schema(description = "Сущность бронирования.", example = "2312 df")
    private String bookingRef;
}

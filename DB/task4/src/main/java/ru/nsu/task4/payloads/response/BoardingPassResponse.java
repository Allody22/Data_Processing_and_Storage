package ru.nsu.task4.payloads.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardingPassResponse {

    @Schema(description = "Номер посадочного талона.", example = "123 dgd")
    private String boardingPassNumber;

    @Schema(description = "Класс билета (бизнес, эконом или комфорт).", example = "Economy")
    private String fareCondition;

    @Schema(description = "Номер места.", example = "421")
    private String seatNumber;
}

package ru.nsu.task4.payloads.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class RouteSearchRequest {

    @Schema(description = "Пункт отправления (город или аэропорт).", example = "Барнаул",required = true)
    private String from;

    @Schema(description = "Пункт прибытия (город или аэропорт).", example = "НГУ",required = true)
    private String to;

    @Schema(description = "Дата вылета.", example = "2024-04-07",required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date departureDate;

    @Schema(description = "Класс билета (эконом, бизнес или комфорт).", example = "Economy")
    private String bookingClass;

    @Schema(description = "Максимально количество пересадок. При 0 прямой рейс.", example = "0",required = true)
    private Integer maxConnections;
}

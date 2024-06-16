package ru.nsu.task4.payloads.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartureFlights {

    @Schema(description = "День недели.", example = "Monday")
    private String dayOfWeek;

    @Schema(description = "Дата прилёта.", example = "2024-04-07")
    private String timeOfArrival;

    @Schema(description = "Айди поездки.", example = "123321")
    private String flightNo;

    @Schema(description = "Пункт прибытия на желаемом пользователе языке" +
            "(откуда самолёт полетел из интересующего нас аэропорта).", example = "Барнаул")
    private String originName;

    private String airportCode;
}

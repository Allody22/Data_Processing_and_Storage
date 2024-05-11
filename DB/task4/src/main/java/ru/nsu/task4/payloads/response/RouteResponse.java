package ru.nsu.task4.payloads.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteResponse {
    private List<FlightSegment> flights; // Список всех рейсов в маршруте
    private String totalTravelTime; // Общее время в пути
    private Integer totalStops; // Общее количество пересадок

}

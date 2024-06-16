package ru.nsu.task4.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AircraftTotalCodeSeatsCountDTO {
    private String aircraftCode;
    private Long totalSeatsCount;
    private Long economySeatsCount;
    private Long businessSeatsCount;
    private Long comfortSeatsCount;
}

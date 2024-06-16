package ru.nsu.task4.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightSoldCodeSeatsCountDTO {
    private Long flightId;
    private String aircraftCode;
    private Long economySoldSeatsCount = 0L;
    private BigDecimal fullPriceEconomy = BigDecimal.ZERO;
    private Long businessSoldSeatsCount = 0L;
    private BigDecimal fullPriceBusiness = BigDecimal.ZERO;
    private Long comfortSoldSeatsCount = 0L;
    private BigDecimal fullPriceComfort = BigDecimal.ZERO;
}

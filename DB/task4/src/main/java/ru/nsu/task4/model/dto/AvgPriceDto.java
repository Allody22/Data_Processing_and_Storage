package ru.nsu.task4.model.dto;

import lombok.Data;

@Data
public class AvgPriceDto {
    private Long flightId;
    private Double avgPrice;

    public AvgPriceDto(Long flightId, Double avgPrice) {
        this.flightId = flightId;
        this.avgPrice = avgPrice;
    }
}

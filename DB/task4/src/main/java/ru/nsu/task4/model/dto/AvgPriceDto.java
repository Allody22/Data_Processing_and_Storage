package ru.nsu.task4.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvgPriceDto {

    private BigDecimal avgPriceForEconomySeat;

    private BigDecimal avgPriceForBusinessSeat;

    private BigDecimal avgPriceForComfortSeat;

    private BigDecimal avgPriceForAnySeat;
}

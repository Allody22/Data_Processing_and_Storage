package ru.nsu.task4.payloads.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirportsNamesResponse {

    @JsonProperty("ru")
    private String ruAirportName;

    @JsonProperty("en")
    private String engAirportName;
}

package ru.nsu.task4.payloads.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AirportsNamesResponse {

    @JsonProperty("ru")
    private String ruAirportName;

    @JsonProperty("en")
    private String engAirportName;
}

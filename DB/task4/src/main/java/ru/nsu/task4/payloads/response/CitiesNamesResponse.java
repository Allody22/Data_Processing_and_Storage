package ru.nsu.task4.payloads.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CitiesNamesResponse {

    @JsonProperty("ru")
    private String ruCityName;

    @JsonProperty("en")
    private String engCityName;
}

package ru.nsu.task4.payloads.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultResponse {

    private Integer totalResults;

    private Integer notAvailableByFareConditionSeatResults;

    private List<RouteResponse> routeResponseList;

}

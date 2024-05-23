package ru.nsu.task4.payloads.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightSegment {
    private Long flightId;
    private String flightNumber;
    private String departureAirportName;
    private String arrivalAirportName;

//    private AirportsNamesResponse departureAirportsNames;
//    private AirportsNamesResponse arrivalAirportsNames;
    private String departureCity;
    private String arrivalCity;
    private String departureTime;
    private String arrivalTime;
    private String duration; // Продолжительность полета
    private String bookingClass; // Класс бронирования
}

package ru.nsu.task4.services;

import org.springframework.stereotype.Service;
import ru.nsu.task4.payloads.requests.BookingRaceRequest;
import ru.nsu.task4.payloads.requests.CheckInRequest;
import ru.nsu.task4.payloads.response.ArrivalFlights;
import ru.nsu.task4.payloads.response.BoardingPassResponse;
import ru.nsu.task4.payloads.response.BookingResponse;
import ru.nsu.task4.payloads.response.DepartureFlights;
import ru.nsu.task4.services.intertaces.IAirportService;

import java.util.Date;
import java.util.List;

@Service
public class AirportService implements IAirportService {
    @Override
    public void getAllAvailableCities() {

    }

    @Override
    public void getAllAvailableAirports() {

    }

    @Override
    public void getAllAirportsInCity(String city) {

    }

    @Override
    public List<ArrivalFlights> getArrivalTimetableOfTheAirport(String airport) {
        return List.of();
    }

    @Override
    public List<DepartureFlights> getDepartureTimetableOfTheAirport(String airport) {
        return List.of();
    }

    @Override
    public void getRaces(String from, String to, Date departureDate, String bookingClass, Integer maxConnections) {

    }

    @Override
    public BookingResponse createBooking(BookingRaceRequest bookingRaceRequest) {
        return new BookingResponse("hello world","hello world");
    }

    @Override
    public BoardingPassResponse checkInOnlineForAFlight(CheckInRequest checkInRequest) {
        return new BoardingPassResponse();
    }
}

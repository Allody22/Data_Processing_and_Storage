package ru.nsu.task4.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.task4.model.Airport;
import ru.nsu.task4.payloads.requests.BookingRaceRequest;
import ru.nsu.task4.payloads.requests.CheckInRequest;
import ru.nsu.task4.payloads.response.*;
import ru.nsu.task4.repository.AirportRepository;
import ru.nsu.task4.services.intertaces.IAirportService;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class AirportService implements IAirportService {

    private final AirportRepository airportRepository;

    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public Set<CitiesNamesResponse> getAllAvailableCities() {
        Set<CitiesNamesResponse> cities = new HashSet<>();
        for (Airport airport : airportRepository.findAll()) {
            try {
                CitiesNamesResponse citiesNamesResponse = objectMapper.readValue(airport.getCity(), CitiesNamesResponse.class);
                cities.add(citiesNamesResponse);
            } catch (IOException e) {
                log.error("Error parsing city JSON for airport: {}", airport.getAirportCode(), e);
            }
        }
        return cities;
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

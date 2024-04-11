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
import ru.nsu.task4.repository.PriceForFullRaceRepository;
import ru.nsu.task4.services.intertaces.IAirportService;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class AirportService implements IAirportService {

    private final AirportRepository airportRepository;

    private final ObjectMapper objectMapper;

    private final PriceForFullRaceRepository priceForFullRaceRepository;

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
    @Transactional
    public Set<AirportsNamesResponse> getAllAvailableAirports() {
        Set<AirportsNamesResponse> airportsNamesResponses = new HashSet<>();
        for (Airport airport : airportRepository.findAll()) {
            try {
                AirportsNamesResponse airportsNamesResponse = objectMapper.readValue(airport.getAirportName(), AirportsNamesResponse.class);
                airportsNamesResponses.add(airportsNamesResponse);
            } catch (IOException e) {
                log.error("Error parsing city JSON for airport: {}", airport.getAirportCode(), e);
            }
        }
        return airportsNamesResponses;

    }

    @Override
    @Transactional
    public Set<AirportsNamesResponse> getAllAirportsInCity(String city) {
        Set<AirportsNamesResponse> airportsNamesResponses = new HashSet<>();
        for (Airport airport : airportRepository.findAllAirportsInTheCityByRuOrEnglishName(city)) {
            try {
                AirportsNamesResponse airportsNamesResponse = objectMapper.readValue(airport.getAirportName(), AirportsNamesResponse.class);
                airportsNamesResponses.add(airportsNamesResponse);
            } catch (IOException e) {
                log.error("Error parsing city JSON for airport: {}", airport.getAirportCode(), e);
            }
        }
        return airportsNamesResponses;
    }

    @Override
    public List<ArrivalFlights> getArrivalTimetableOfTheAirport(String airport) {
        List<ArrivalFlights> arrivalFlights = new ArrayList<>();
//        for (var flightInfo : priceForFullRaceRepository.findAll()){
//            try {
//                var currentArrivalFlight = objectMapper.readValue(airport, AirportsNamesResponse.class);
//                airportsNamesResponses.add(airportsNamesResponse);
//            } catch (IOException e) {
//                log.error("Error parsing city JSON for airport: {}", airport.getAirportCode(), e);
//            }
//        }
        return arrivalFlights;
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

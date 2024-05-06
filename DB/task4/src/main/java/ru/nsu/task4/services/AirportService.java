package ru.nsu.task4.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.task4.model.*;
import ru.nsu.task4.model.ids.TicketFlightId;
import ru.nsu.task4.payloads.requests.BookingRaceRequest;
import ru.nsu.task4.payloads.requests.CheckInRequest;
import ru.nsu.task4.payloads.response.*;
import ru.nsu.task4.repository.*;
import ru.nsu.task4.services.intertaces.IAirportService;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class AirportService implements IAirportService {

    private final AirportRepository airportRepository;

    private final ObjectMapper objectMapper;

    private final FlightsRepository flightsRepository;

    private final PriceForFullRaceRepository priceForFullRaceRepository;

    private final TicketsFlightsRepository ticketsFlightsRepository;
    private final SeatsRepository seatsRepository;
    private final BoardingPassRepository boardingPassRepository;
    private final TicketsRepository ticketsRepository;
    private final BookingsRepository bookingsRepository;

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
    public Set<AirportsNamesResponse> getAllAvailableAirports() throws JsonProcessingException {
        Set<AirportsNamesResponse> airportsNamesResponses = new HashSet<>();
        for (Airport airport : airportRepository.findAll()) {
            airportsNamesResponses.add(getTwoAirportNames(airport.getAirportName()));
        }
        return airportsNamesResponses;

    }

    @Override
    @Transactional
    public Set<AirportsNamesResponse> getAllAirportsInCity(String city) throws JsonProcessingException {
        Set<AirportsNamesResponse> airportsNamesResponses = new HashSet<>();
        for (Airport airport : airportRepository.findAllAirportsInTheCityByRuOrEnglishName(city)) {
            airportsNamesResponses.add(getTwoAirportNames(airport.getAirportName()));
        }
        return airportsNamesResponses;
    }

    @Override
    @Transactional
    public List<ArrivalFlights> getArrivalTimetableOfTheAirport(String airport) throws JsonProcessingException {
        List<ArrivalFlights> arrivalFlights = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (PriceFullOneRaceAnalysis flightInfo : priceForFullRaceRepository.findAll()) {
            if (flightInfo.getArrivalAirportName().contains(airport)) {
                AirportsNamesResponse airportName = getTwoAirportNames(flightInfo.getDepartureAirportName());
                arrivalFlights.add(new ArrivalFlights(flightInfo.getArrivalTime().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                        flightInfo.getArrivalTime().format(dateTimeFormatter), flightInfo.getFlightNumber(), airportName.getRuAirportName(), airportName.getEngAirportName()));
            }
        }
        return arrivalFlights;
    }

    @Override
    public List<DepartureFlights> getDepartureTimetableOfTheAirport(String airport) throws JsonProcessingException {
        List<DepartureFlights> departureFlights = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (PriceFullOneRaceAnalysis flightInfo : priceForFullRaceRepository.findAll()) {
            if (flightInfo.getDepartureAirportName().contains(airport)) {
                AirportsNamesResponse airportName = getTwoAirportNames(flightInfo.getArrivalAirportName());
                departureFlights.add(new DepartureFlights(flightInfo.getArrivalTime().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                        flightInfo.getArrivalTime().format(dateTimeFormatter), flightInfo.getFlightNumber(), airportName.getRuAirportName(), airportName.getEngAirportName()));
            }
        }
        return departureFlights;
    }

    @Override
    public void getRaces(String from, String to, Date departureDate, String bookingClass, Integer maxConnections) {

    }

    @Transactional
    public BookingResponse createBooking(BookingRaceRequest bookingRaceRequest) throws JsonProcessingException {
        Long flightId = bookingRaceRequest.getFlightId();
        log.info("flight id = {}", flightId);
        Flights currentFlightFromDB = flightsRepository.findAllByFlightId(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found in actual db"));

        if (currentFlightFromDB.getActualArrival() != null || currentFlightFromDB.getActualDeparture() != null) {
            throw new RuntimeException("You can book seats on this flight anymore");
        }

        PriceFullOneRaceAnalysis flightInfo = priceForFullRaceRepository.findByFlightUid(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found in my db"));

        String aircraftCode = currentFlightFromDB.getAircraftCode();

        // Проверка существования места с соответствующим классом обслуживания
        Seats seat = seatsRepository.findBySeatNoAndAircraft_AircraftCodeAndFareCondition(
                        bookingRaceRequest.getSeatNumber(), aircraftCode, bookingRaceRequest.getFareCondition())
                .orElseThrow(() -> new RuntimeException("Seat does not exist or fare condition mismatch"));

        if (isSeatOccupied(flightId, bookingRaceRequest.getSeatNumber())) {
            throw new RuntimeException("Seat is occupied");
        }

        // Создание нового бронирования
        log.info("trying to book");
        Bookings userBooking = new Bookings();
        userBooking.setBookDate(OffsetDateTime.now());
        userBooking.setBookRef(generateUniqueBookingRef());
        userBooking.setTotalAmount(bookingRaceRequest.getPrice());
        bookingsRepository.save(userBooking);
        // Создание и сохранение нового билета

        log.info("trying to create ticket");


        var passengerContact = serializeToJson(bookingRaceRequest.getPassengerContact());
        Tickets newTicket = new Tickets();
        newTicket.setTicketNumber(generateUniqueTicketNumber());
        newTicket.setPassengerId(bookingRaceRequest.getPassengerId());
        newTicket.setPassengerName(bookingRaceRequest.getPassengerName());
        newTicket.setContactData(bookingRaceRequest.getPassengerContact());
        newTicket.setBooking(userBooking);


        log.info("jsonContact {}", passengerContact);

        ticketsRepository.insertTicket(newTicket.getTicketNumber(), newTicket.getPassengerId(), newTicket.getPassengerName(), passengerContact, userBooking.getBookRef());


        log.info("trying save full race info");
        flightInfo.setSoldSeatsNumber(flightInfo.getSoldSeatsNumber() + 1);
        priceForFullRaceRepository.save(flightInfo);

        log.info("trying save all");
        // Создание и сохранение записи о полете
        TicketFlightId ticketFlightId = new TicketFlightId(newTicket.getTicketNumber(), currentFlightFromDB.getFlightId());
        TicketFlights newTicketFlight = new TicketFlights();
        newTicketFlight.setId(ticketFlightId);
        newTicketFlight.setTicket(newTicket);
        newTicketFlight.setFlight(currentFlightFromDB);
        newTicketFlight.setFareCondition(bookingRaceRequest.getFareCondition());
        newTicketFlight.setAmount(bookingRaceRequest.getPrice());
        ticketsFlightsRepository.save(newTicketFlight);


        log.info("time to response with ticket = {}, and book ref = {}", newTicket.getTicketNumber(), userBooking.getBookRef());
        return new BookingResponse(newTicket.getTicketNumber(), userBooking.getBookRef());
    }

    public JsonNode createJsonNode(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(json);
        } catch (IOException e) {
            throw new RuntimeException("Error while parsing JSON", e);
        }
    }

    public String serializeToJson(JsonNode jsonNode) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while serializing contact info", e);
        }
    }


    public boolean isSeatOccupied(Long flightId, String seatNo) {
        // Проверяем, есть ли посадочные талоны на данное место для данного рейса
        List<BoardingPass> boardingPasses = boardingPassRepository.findByFlightIdAndSeatNo(flightId, seatNo);
        return !boardingPasses.isEmpty(); // Место занято, если найдены записи
    }

    @Override
    public BoardingPassResponse checkInOnlineForAFlight(CheckInRequest checkInRequest) {
        return new BoardingPassResponse();
    }

    private AirportsNamesResponse getTwoAirportNames(String airport) throws JsonProcessingException {
        AirportsNamesResponse airportsNamesResponse = objectMapper.readValue(airport, AirportsNamesResponse.class);
        String ruAirportName = airportsNamesResponse.getRuAirportName() != null ? airportsNamesResponse.getRuAirportName() : "Неизвестно";
        String engAirportName = airportsNamesResponse.getEngAirportName() != null ? airportsNamesResponse.getEngAirportName() : "Unknown";
        airportsNamesResponse.setEngAirportName(engAirportName);
        airportsNamesResponse.setRuAirportName(ruAirportName);
        return airportsNamesResponse;
    }

    /**
     * Генерирует уникальный номер билета, который всегда содержит 13 символов (как в БД).
     *
     * @return строковый уникальный номер билета.
     */
    public String generateUniqueTicketNumber() {
        String ticketNumber;
        do {
            ticketNumber = RandomStringUtils.randomAlphanumeric(13).toUpperCase();
        } while (ticketsRepository.existsByTicketNumber(ticketNumber));

        return ticketNumber;
    }

    /**
     * Генерируй уникальный айдишник бронирования, который всегда содержит 6 символов (как в БД).
     *
     * @return строковый уникальный айди бронирования
     */
    public String generateUniqueBookingRef() {
        String bookingRefId;
        do {
            bookingRefId = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        } while (bookingsRepository.existsByBookRef(bookingRefId));

        return bookingRefId;
    }
}

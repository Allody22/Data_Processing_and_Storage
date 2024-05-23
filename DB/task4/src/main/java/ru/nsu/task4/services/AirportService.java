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
import ru.nsu.task4.model.exceptions.*;
import ru.nsu.task4.model.ids.TicketFlightId;
import ru.nsu.task4.payloads.requests.BookingRaceRequest;
import ru.nsu.task4.payloads.requests.CheckInRequest;
import ru.nsu.task4.payloads.response.*;
import ru.nsu.task4.repository.*;
import ru.nsu.task4.services.intertaces.IAirportService;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
    public Set<CitiesNamesResponse> getAllAvailableCities(String lang) throws JsonProcessingException {
        Set<CitiesNamesResponse> cities = new HashSet<>();
        for (Airport airport : airportRepository.findAll()) {
            cities.add(getCitiesNames(airport.getCity(), lang));
        }
        return cities;
    }


    @Override
    @Transactional
    public Set<AirportNameResponse> getAllAirportsInCity(String lang, String city) throws JsonProcessingException {
        Set<AirportNameResponse> airportsNamesResponses = new HashSet<>();
        for (Airport airport : airportRepository.findAllAirportsInTheCityByRuOrEnglishName(city)) {
            airportsNamesResponses.add(getAirportName(airport.getAirportName(), airport.getAirportCode(), lang));
        }
        return airportsNamesResponses;
    }

    @Override
    @Transactional
    public List<ArrivalFlights> getArrivalTimetableOfTheAirport(String lang, String airport) throws JsonProcessingException {
        List<ArrivalFlights> arrivalFlights = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (PriceFullOneRaceAnalysis flightInfo : priceForFullRaceRepository.findAll()) {
            if (flightInfo.getArrivalAirportName().contains(airport)) {
                AirportNameResponse airportName = getAirportName(flightInfo.getDepartureAirportName(), flightInfo.getAircraftCode(), lang);
                arrivalFlights.add(new ArrivalFlights(flightInfo.getArrivalTime().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                        flightInfo.getArrivalTime().format(dateTimeFormatter), flightInfo.getFlightNumber(), airportName.getAirportName(), airportName.getAirportCode()));
            }
        }
        return arrivalFlights;
    }

    @Override
    public List<DepartureFlights> getDepartureTimetableOfTheAirport(String lang, String airport) throws JsonProcessingException {
        List<DepartureFlights> departureFlights = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (PriceFullOneRaceAnalysis flightInfo : priceForFullRaceRepository.findAll()) {
            if (flightInfo.getDepartureAirportName().contains(airport)) {
                AirportNameResponse airportName = getAirportName(flightInfo.getDepartureAirportName(), flightInfo.getAircraftCode(), lang);
                departureFlights.add(new DepartureFlights(flightInfo.getArrivalTime().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                        flightInfo.getArrivalTime().format(dateTimeFormatter), flightInfo.getFlightNumber(), airportName.getAirportName(), airportName.getAirportCode()));
            }
        }
        return departureFlights;
    }

    @Override
    @Transactional
    public SearchResultResponse getRaces(String lang, String from, String to, Date departureDate, String bookingClass, Integer maxConnections) throws JsonProcessingException {
        OffsetDateTime startOfTheDay = departureDate.toInstant().atOffset(ZoneOffset.UTC);
        OffsetDateTime endOfTheDay = startOfTheDay.plusDays(1);

        List<PriceFullOneRaceAnalysis> potentialFlights = findFlightsByCityOrAirport(lang, from, startOfTheDay, endOfTheDay);
        List<RouteResponse> routes = new ArrayList<>();
        AtomicInteger noAvailabilityCount = new AtomicInteger(0);  // Добавлен счетчик

        // Добавляем прямые рейсы, соответствующие классу бронирования
        for (PriceFullOneRaceAnalysis flight : potentialFlights) {
            if (checkArrivalAirportPoint(flight.getArrivalAirportName(), flight.getAircraftCode(), to, lang) || checkArrivalCityPoint(flight.getArrivalCity(), to, lang)) {
                if (checkBookingClassAvailability(flight, bookingClass)) {
                    List<FlightSegment> segments = new ArrayList<>();
                    segments.add(createFlightSegment(flight, bookingClass, lang));
                    routes.add(new RouteResponse(segments, 0, "0", calculateTotalTravelTime(segments)));
                } else {
                    noAvailabilityCount.incrementAndGet();  // Увеличиваем счетчик, если мест нет
                }
            }
        }

        // Рекурсивный поиск рейсов с пересадками, если maxConnections > 0
        if (maxConnections > 0) {
            for (PriceFullOneRaceAnalysis flight : potentialFlights) {
                Set<String> initialVisitedCities = new HashSet<>();
                String arrivalCityName = getCitiesNames(flight.getArrivalCity(), lang).getCityName();
                initialVisitedCities.add(arrivalCityName);
                initialVisitedCities.add(from);
                findConnectingFlights(flight, to, endOfTheDay, bookingClass, maxConnections, 0, new ArrayList<>(List.of(flight)),
                        routes, noAvailabilityCount, initialVisitedCities, lang);
            }
        }


        return new SearchResultResponse(routes.size(), noAvailabilityCount.get(), routes);
    }

    private List<PriceFullOneRaceAnalysis> findFlightsByCityOrAirport(String lang, String from, OffsetDateTime startOfTheDay, OffsetDateTime endOfTheDay) {
        return priceForFullRaceRepository.findFlightsByCityOrAirportAndDepartureTime(lang, from, startOfTheDay, endOfTheDay);
    }

    private boolean checkBookingClassAvailability(PriceFullOneRaceAnalysis flight, String bookingClass) {
        return ticketsFlightsRepository.existsByFlight_FlightIdAndFareCondition(flight.getFlightUid(), bookingClass);
    }

    private FlightSegment createFlightSegment(PriceFullOneRaceAnalysis flight, String bookingClass, String lang) throws JsonProcessingException {
        return new FlightSegment(
                flight.getFlightUid(),
                flight.getFlightNumber(),
                getAirportName(flight.getDepartureAirportName(), "0", lang).getAirportName(),
                getAirportName(flight.getArrivalAirportName(), "0", lang).getAirportName(),
                getCitiesNames(flight.getDepartureCity(), lang).getCityName(),
                getCitiesNames(flight.getArrivalCity(), lang).getCityName(),
                flight.getDepartureTime().toString(),
                flight.getArrivalTime().toString(),
                calculateDuration(flight.getDepartureTime(), flight.getArrivalTime()),
                bookingClass
        );
    }

    private void findConnectingFlights(PriceFullOneRaceAnalysis lastFlight, String finalDestination, OffsetDateTime endDate,
                                       String bookingClass, int maxConnections, int currentConnections,
                                       List<PriceFullOneRaceAnalysis> currentRoute, List<RouteResponse> allRoutes,
                                       AtomicInteger noAvailabilityCount, Set<String> visitedCities, String lang) throws JsonProcessingException {
        if (currentConnections >= maxConnections) {
            return;
        }

        AirportNameResponse airportNames = getAirportName(lastFlight.getArrivalAirportName(), lastFlight.getAircraftCode(), lang);
        List<PriceFullOneRaceAnalysis> possibleNextFlights = findFlightsByCityOrAirport(lang, airportNames.getAirportName(), lastFlight.getArrivalTime().plusHours(1), endDate.plusHours(8));
        for (PriceFullOneRaceAnalysis nextFlight : possibleNextFlights) {
            // Получаем город назначения текущего рейса
            String nextCityName = getCitiesNames(nextFlight.getArrivalCity(), lang).getCityName();

            // Проверяем, не посещали ли мы уже этот город
            if (visitedCities.contains(nextCityName)) {
                continue;
            }

            // На рейс просто нет свободных мест
            if (!checkBookingClassAvailability(nextFlight, bookingClass)) {
                noAvailabilityCount.incrementAndGet();
                continue;
            }
            List<PriceFullOneRaceAnalysis> newRoute = new ArrayList<>(currentRoute);
            newRoute.add(nextFlight);

            // Добавляем текущий город в множество посещенных
            visitedCities.add(nextCityName);

            // Проверка, является ли текущий рейс конечным пунктом назначения
            if (checkArrivalAirportPoint(nextFlight.getArrivalAirportName(), nextFlight.getAircraftCode(), finalDestination, lang)
                    || checkArrivalCityPoint(nextFlight.getArrivalCity(), finalDestination, lang)) {
                allRoutes.add(buildRouteResponse(newRoute, bookingClass, lang));
                return;
            } else {
                // Рекурсивный поиск дальнейших пересадок
                findConnectingFlights(nextFlight, finalDestination, endDate, bookingClass, maxConnections,
                        currentConnections + 1, newRoute, allRoutes, noAvailabilityCount, visitedCities, lang);
            }

            // Удаляем текущий город из множества посещенных, чтобы не мешать другим маршрутам
            visitedCities.remove(nextCityName);
        }
    }

    private boolean checkArrivalCityPoint(String city, String pointTo, String lang) throws JsonProcessingException {
        CitiesNamesResponse citiesNames = getCitiesNames(city, lang);
        return citiesNames.getCityName().equals(pointTo);
    }

    private boolean checkArrivalAirportPoint(String arrivalPoint, String airportCode, String pointTo, String lang) throws JsonProcessingException {
        AirportNameResponse airportName = getAirportName(arrivalPoint, airportCode, lang);
        return airportName.getAirportName().equals(pointTo);
    }


    private RouteResponse buildRouteResponse(List<PriceFullOneRaceAnalysis> routeFlights, String bookingClass, String lang) throws JsonProcessingException {
        List<FlightSegment> segments = new ArrayList<>();
        Duration totalWaitingTime = Duration.ZERO;

        for (int i = 0; i < routeFlights.size(); i++) {
            PriceFullOneRaceAnalysis flight = routeFlights.get(i);
            segments.add(new FlightSegment(
                    flight.getFlightUid(),
                    flight.getFlightNumber(),
                    getAirportName(flight.getDepartureAirportName(), flight.getAircraftCode(), lang).getAirportName(),
                    getAirportName(flight.getArrivalAirportName(), flight.getAircraftCode(), lang).getAirportName(),
                    getCitiesNames(flight.getDepartureCity(), lang).getCityName(),
                    getCitiesNames(flight.getArrivalCity(), lang).getCityName(),
                    flight.getDepartureTime().toString(),
                    flight.getArrivalTime().toString(),
                    calculateDuration(flight.getDepartureTime(), flight.getArrivalTime()),
                    bookingClass
            ));
            // Считаем время ожидания, если это не первый рейс
            if (i > 0) {
                OffsetDateTime previousArrival = routeFlights.get(i - 1).getArrivalTime();
                OffsetDateTime currentDeparture = flight.getDepartureTime();
                totalWaitingTime = totalWaitingTime.plus(Duration.between(previousArrival, currentDeparture));
            }
        }

        String totalTravelTime = calculateTotalTravelTime(segments);
        String totalWaitingTimeStr = String.format("%d hours, %d min", totalWaitingTime.toHours(), totalWaitingTime.toMinutesPart());
        return new RouteResponse(segments, segments.size(), totalWaitingTimeStr, totalTravelTime);
    }

    private String calculateDuration(OffsetDateTime departureTime, OffsetDateTime arrivalTime) {
        Duration duration = Duration.between(departureTime, arrivalTime);
        return String.format("%d hours, %d min", duration.toHours(), duration.toMinutesPart());
    }

    private String calculateTotalTravelTime(List<FlightSegment> segments) {
        if (segments.isEmpty()) return "0 hours, 0 min";
        OffsetDateTime start = OffsetDateTime.parse(segments.get(0).getDepartureTime());
        OffsetDateTime end = OffsetDateTime.parse(segments.get(segments.size() - 1).getArrivalTime());
        Duration totalDuration = Duration.between(start, end);
        return String.format("%d hours, %d min", totalDuration.toHours(), totalDuration.toMinutesPart());
    }


    @Transactional
    public List<BookingResponse> createBooking(BookingRaceRequest bookingRaceRequest) {

        List<BookingResponse> bookingResponses = new ArrayList<>();
        for (Long currentFlightId : bookingRaceRequest.getFlightId()) {
            bookingResponses.add(bookOneRaceForResponse(currentFlightId, bookingRaceRequest.getFareCondition(), bookingRaceRequest.getPassengerId(),
                    bookingRaceRequest.getPassengerName(), bookingRaceRequest.getPassengerContact()));
        }
        return bookingResponses;
    }

    private BookingResponse bookOneRaceForResponse(Long flightId, String fareCondition, String passengerId,
                                                   String passengerName, JsonNode passengerContactJsonNode) {

        Flights currentFlightFromDB = flightsRepository.findAllByFlightId(flightId)
                .orElseThrow(() -> new NotFoundException("Flight not found in actual db"));

        if (currentFlightFromDB.getActualArrival() != null || currentFlightFromDB.getActualDeparture() != null) {
            throw new RaceIsGoneException("You can't book seats on this flight anymore. It's already finished.");
        }

        PriceFullOneRaceAnalysis flightInfo = priceForFullRaceRepository.findByFlightUid(flightId)
                .orElseThrow(() -> new NotFoundException("Flight not found in my db"));

        String aircraftCode = currentFlightFromDB.getAircraftCode();
        if (!seatsRepository.existsByAircraft_AircraftCodeAndFareCondition(aircraftCode, fareCondition)) {
            throw new NoSeatsException("Fare condition '" + fareCondition + "' mismatch for aircraft code "
                    + aircraftCode + " and flight " + flightId);
        }
        BigDecimal moneyForRace;
        switch (fareCondition) {
            case "Business" -> {
                if (flightInfo.getSoldSeatsBusiness() >= flightInfo.getTotalSeatsBusiness()) {
                    throw new NoSeatsException("No business seats available");
                } else {
                    moneyForRace = flightInfo.getAveragePriceForOneBusinessSeat();
                    flightInfo.setSoldSeatsBusiness(flightInfo.getSoldSeatsBusiness() + 1);
                }
            }
            case "Comfort" -> {
                if (flightInfo.getSoldSeatsComfort() >= flightInfo.getTotalSeatsComfort()) {
                    throw new NoSeatsException("No comfort seats available");
                } else {
                    moneyForRace = flightInfo.getAveragePriceForOneComfortSeat();
                    flightInfo.setSoldSeatsComfort(flightInfo.getSoldSeatsComfort() + 1);
                }
            }
            case "Economy" -> {
                if (flightInfo.getSoldSeatsEconomy() >= flightInfo.getTotalSeatsEconomy()) {
                    throw new NoSeatsException("No economy seats available");
                } else {
                    moneyForRace = flightInfo.getAveragePriceForOneEconomySeat();
                    flightInfo.setSoldSeatsEconomy(flightInfo.getSoldSeatsEconomy() + 1);
                }
            }
            default -> throw new LanguageNotFoundException("Invalid fare condition with name " + fareCondition);
        }

        // Создание нового бронирования
        Bookings userBooking = new Bookings();
        userBooking.setBookDate(OffsetDateTime.now());
        userBooking.setBookRef(generateUniqueBookingRef());

        userBooking.setTotalAmount(moneyForRace);
        bookingsRepository.save(userBooking);
        // Создание и сохранение нового билета

        var passengerContact = serializeToJson(passengerContactJsonNode);
        Tickets newTicket = new Tickets();
        newTicket.setTicketNumber(generateUniqueTicketNumber());
        newTicket.setPassengerId(passengerId);
        newTicket.setPassengerName(passengerName);
        newTicket.setContactData(passengerContactJsonNode);
        newTicket.setBooking(userBooking);


        ticketsRepository.insertTicket(newTicket.getTicketNumber(), newTicket.getPassengerId(), newTicket.getPassengerName(), passengerContact, userBooking.getBookRef());


        flightInfo.setSoldSeatsNumber(flightInfo.getSoldSeatsNumber() + 1);
        flightInfo.setTotalPrice(flightInfo.getTotalPrice().add(moneyForRace));
        priceForFullRaceRepository.save(flightInfo);

        // Создание и сохранение записи о полете
        TicketFlightId ticketFlightId = new TicketFlightId(newTicket.getTicketNumber(), currentFlightFromDB.getFlightId());
        TicketFlights newTicketFlight = new TicketFlights();
        newTicketFlight.setId(ticketFlightId);
        newTicketFlight.setTicket(newTicket);
        newTicketFlight.setFlight(currentFlightFromDB);
        newTicketFlight.setFareCondition(fareCondition);
        newTicketFlight.setAmount(moneyForRace);
        ticketsFlightsRepository.save(newTicketFlight);


        return new BookingResponse(newTicket.getTicketNumber(), userBooking.getBookRef());
    }

    public String serializeToJson(JsonNode jsonNode) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while serializing contact info", e);
        }
    }

    @Override
    @Transactional
    public Set<AirportNameResponse> getAllAvailableAirports(String lang) throws JsonProcessingException {
        Set<AirportNameResponse> airportsNamesResponses = new HashSet<>();
        for (Airport airport : airportRepository.findAll()) {
            airportsNamesResponses.add(getAirportName(airport.getAirportName(), airport.getAirportCode(), lang));
        }
        return airportsNamesResponses;
    }

    private CitiesNamesResponse getCitiesNames(String city, String lang) throws JsonProcessingException {
        JsonNode citiesNode = objectMapper.readTree(city);
        JsonNode citiesNameNode = citiesNode.get(lang);

        if (citiesNameNode == null) {
            throw new LanguageNotFoundException("Language '" + lang + "' doesn't exist in our cities database");
        }

        String airportName = citiesNameNode.asText();
        return new CitiesNamesResponse(airportName);
    }

    private AirportNameResponse getAirportName(String airport, String airportCode, String lang) throws JsonProcessingException {
        JsonNode airportNode = objectMapper.readTree(airport);
        JsonNode airportNameNode = airportNode.get(lang);

        if (airportNameNode == null) {
            throw new LanguageNotFoundException("Language '" + lang + "' doesn't exist in our airport database");
        }

        String airportName = airportNameNode.asText();
        return new AirportNameResponse(airportName, airportCode);
    }

    public boolean isSeatOccupied(Long flightId, String seatNo) {
        List<BoardingPass> boardingPasses = boardingPassRepository.findByFlightIdAndSeatNo(flightId, seatNo);
        return !boardingPasses.isEmpty(); // Место занято, если найдены записи
    }

    @Override
    public BoardingPassResponse checkInOnlineForAFlight(CheckInRequest checkInRequest) {
        BoardingPassResponse boardingPassResponse = new BoardingPassResponse();

        Tickets userTicket = ticketsRepository.findByBooking_BookRef(checkInRequest.getBookRef())
                .orElseThrow(() -> new NotFoundException("Booking not found"));
        TicketFlights bookedUserTicket = ticketsFlightsRepository.findByTicket_TicketNumber(userTicket.getTicketNumber())
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        // Проверка, создан ли уже BoardingPass для данного билета
        if (boardingPassRepository.existsByTicketNoAndFlightId(userTicket.getTicketNumber(), bookedUserTicket.getFlight().getFlightId())) {
            throw new AlreadyCheckedInException("You have already checked in for this flight.");
        }

        String userFareCondition = bookedUserTicket.getFareCondition();

        Flights userFlight = flightsRepository.findByFlightId(bookedUserTicket.getFlight().getFlightId())
                .orElseThrow(() -> new NotFoundException("Flight is not found"));

        String userSeatNo = findFirstFreeSeat(userFlight.getAircraftCode(), userFlight.getFlightId(), userFareCondition);

        Seats seat = seatsRepository.findBySeatNoAndAircraft_AircraftCodeAndFareCondition(
                        userSeatNo, userFlight.getAircraftCode(), userFareCondition)
                .orElseThrow(() -> new NotFoundException("Seat does not exist or fare condition mismatch"));

        if (isSeatOccupied(userFlight.getFlightId(), seat.getSeatNo())) {
            throw new NoSeatsException("Seat is occupied");
        }
        int maxBoardingNo = boardingPassRepository.findMaxBoardingNoByFlightId(userFlight.getFlightId());
        int newBoardingNo = maxBoardingNo + 1;
        BoardingPass newBoardingPass = new BoardingPass();
        newBoardingPass.setTicketNo(userTicket.getTicketNumber());
        newBoardingPass.setFlightId(userFlight.getFlightId());
        newBoardingPass.setBoardingNo(newBoardingNo);
        newBoardingPass.setSeatNo(userSeatNo);
        boardingPassRepository.save(newBoardingPass);

        boardingPassResponse.setBoardingPassNumber(String.valueOf(newBoardingNo));

        boardingPassResponse.setFareCondition(userFareCondition);
        boardingPassResponse.setSeatNumber(userSeatNo);
        return boardingPassResponse;
    }

    private String findFirstFreeSeat(String aircraftCode, Long flightId, String fareCondition) {
        List<String> occupiedSeats = boardingPassRepository.findOccupiedSeatsByFlightIdAndFareCondition(flightId, fareCondition);

        List<String> allSeats = seatsRepository.findSeatsByAircraftCodeAndFareCondition(aircraftCode, fareCondition);
        if (allSeats.size() == occupiedSeats.size()) {
            throw new NoSeatsException("No seats available on flight " + flightId + " of the type " + fareCondition);
        }

        Set<String> occupiedSeatsSet = new HashSet<>(occupiedSeats);

        for (String seat : allSeats) {
            if (!occupiedSeatsSet.contains(seat)) {
                return seat;
            }
        }
        throw new NoSeatsException("No seats available on flight " + flightId + " of the type " + fareCondition);
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

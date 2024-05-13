package ru.nsu.task4.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.task4.model.Flights;
import ru.nsu.task4.model.PriceFullOneRaceAnalysis;
import ru.nsu.task4.model.dto.AircraftTotalCodeSeatsCountDTO;
import ru.nsu.task4.model.dto.AvgPriceDto;
import ru.nsu.task4.model.dto.FlightSoldCodeSeatsCountDTO;
import ru.nsu.task4.repository.FlightsRepository;
import ru.nsu.task4.repository.PriceForFullRaceRepository;
import ru.nsu.task4.repository.SeatsRepository;
import ru.nsu.task4.repository.TicketsFlightsRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class FlightsPriceService {

    private final FlightsRepository flightsRepository;

    private final TicketsFlightsRepository ticketsFlightsRepository;

    private final PriceForFullRaceRepository priceForFullRaceRepository;

    private final SeatsRepository seatsRepository;

    /**
     * Одна из главных функция для DB 4.
     * Ищем все полёты, а для них ищем все билеты и все места у них.
     * Заполняем всю инфу, считаем среднюю цену за 1 место.
     * Вся эта информация нам понадобится, чтобы посчитать
     * в будущем цену за рейсы с такими же маршрутами, но где цена еще не известна.
     */
    @Transactional
    public void getFullPriceForOneRace() {
        List<Flights> allFlights = flightsRepository.findAll();
        List<AircraftTotalCodeSeatsCountDTO> seatsByAircraftCode = seatsRepository.countSeatsByAircraftCode();
        List<FlightSoldCodeSeatsCountDTO> soldSeatsData = ticketsFlightsRepository.getAllSoldSeatsCount();

        Map<Long, FlightSoldCodeSeatsCountDTO> soldSeatsMap = soldSeatsData.stream()
                .collect(Collectors.toMap(FlightSoldCodeSeatsCountDTO::getFlightId, Function.identity()));

        List<PriceFullOneRaceAnalysis> analyses = new ArrayList<>();

        allFlights.forEach(flight -> {
            String aircraftCode = flight.getAircraftCode();
            var flightId = flight.getFlightId();

            FlightSoldCodeSeatsCountDTO currentSoldSeats = soldSeatsMap.get(flightId);
            if (currentSoldSeats == null) {
                currentSoldSeats = new FlightSoldCodeSeatsCountDTO(flightId, aircraftCode, 0L, BigDecimal.ZERO, 0L, BigDecimal.ZERO, 0L, BigDecimal.ZERO);
            }
            AircraftTotalCodeSeatsCountDTO currentSeatsByAircraftCode = seatsByAircraftCode.stream()
                    .filter(seats -> seats.getAircraftCode().equals(aircraftCode))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Aircraft with code " + aircraftCode + " doesn't found"));

            PriceFullOneRaceAnalysis analysis = new PriceFullOneRaceAnalysis();
            analysis.setFlightUid(flight.getFlightId());
            analysis.setFlightNumber(flight.getFlightNo());
            analysis.setDepartureTime(flight.getScheduledDeparture());
            analysis.setArrivalTime(flight.getScheduledArrival());
            analysis.setDepartureCity(flight.getDepartureAirport().getCity());
            analysis.setArrivalCity(flight.getArrivalAirport().getCity());
            analysis.setTotalPrice(BigDecimal.ZERO);
            analysis.setDepartureAirportName(flight.getDepartureAirport().getAirportName());
            analysis.setArrivalAirportName(flight.getArrivalAirport().getAirportName());
            analysis.setTotalSeatsNumber(Math.toIntExact(currentSeatsByAircraftCode.getTotalSeatsCount()));

            var soldBusinessSeats = currentSoldSeats.getBusinessSoldSeatsCount();
            var soldComfortSeats = currentSoldSeats.getComfortSoldSeatsCount();
            var soldEconomySeats = currentSoldSeats.getEconomySoldSeatsCount();

            var allBusinessSeats = currentSeatsByAircraftCode.getBusinessSeatsCount();
            var allComfortSeats = currentSeatsByAircraftCode.getComfortSeatsCount();
            var allEconomySeats = currentSeatsByAircraftCode.getEconomySeatsCount();

            var totalSoldSeats = soldBusinessSeats + soldEconomySeats + soldComfortSeats;
            var totalSeats = allBusinessSeats + allEconomySeats + allComfortSeats;

            analysis.setSoldSeatsNumber((int) (totalSoldSeats));
            analysis.setTotalSeatsNumber((int) (totalSeats));
            analysis.setAircraftCode(flight.getAircraftCode());

            analysis.setTotalSeatsBusiness(Math.toIntExact(allBusinessSeats));
            analysis.setTotalSeatsEconomy(Math.toIntExact(allEconomySeats));
            analysis.setTotalSeatsComfort(Math.toIntExact(allComfortSeats));
            analysis.setSoldSeatsBusiness(Math.toIntExact(soldBusinessSeats));
            analysis.setSoldSeatsEconomy(Math.toIntExact(soldEconomySeats));
            analysis.setSoldSeatsComfort(Math.toIntExact(soldComfortSeats));
            BigDecimal totalPrice = BigDecimal.ZERO;


            BigDecimal allBusinessSeatsPrice = currentSoldSeats.getFullPriceBusiness() != null ? currentSoldSeats.getFullPriceBusiness() : BigDecimal.ZERO;
            BigDecimal allComfortSeatsPrice = currentSoldSeats.getFullPriceComfort() != null ? currentSoldSeats.getFullPriceComfort() : BigDecimal.ZERO;
            BigDecimal allEconomySeatsPrice = currentSoldSeats.getFullPriceEconomy() != null ? currentSoldSeats.getFullPriceEconomy() : BigDecimal.ZERO;

            totalPrice = totalPrice.add(allBusinessSeatsPrice);
            if (soldBusinessSeats != null && soldBusinessSeats != 0) {
                analysis.setAveragePriceForOneBusinessSeat(allBusinessSeatsPrice.divide(BigDecimal.valueOf(soldBusinessSeats), RoundingMode.HALF_UP));
            } else {
                analysis.setAveragePriceForOneBusinessSeat(BigDecimal.ZERO);
            }

            totalPrice = totalPrice.add(allComfortSeatsPrice);
            if (soldComfortSeats != null && soldComfortSeats != 0) {
                analysis.setAveragePriceForOneComfortSeat(allComfortSeatsPrice.divide(BigDecimal.valueOf(soldComfortSeats), RoundingMode.HALF_UP));
            } else {
                analysis.setAveragePriceForOneComfortSeat(BigDecimal.ZERO);
            }

            totalPrice = totalPrice.add(allEconomySeatsPrice);
            if (soldEconomySeats != null && soldEconomySeats != 0) {
                analysis.setAveragePriceForOneEconomySeat(allEconomySeatsPrice.divide(BigDecimal.valueOf(soldEconomySeats), RoundingMode.HALF_UP));
            } else {
                analysis.setAveragePriceForOneEconomySeat(BigDecimal.ZERO);
            }

            analysis.setTotalPrice(totalPrice);
            if (totalSoldSeats > 0) {
                analysis.setAveragePriceForOneSeat(totalPrice.divide(BigDecimal.valueOf(totalSoldSeats), RoundingMode.HALF_UP));
            } else {
                analysis.setAveragePriceForOneSeat(BigDecimal.ZERO);
            }
            analyses.add(analysis);
        });

        priceForFullRaceRepository.saveAll(analyses);
    }


    @Transactional
    public List<PriceFullOneRaceAnalysis> findAllRacesWithoutTotalPrice() {
        var races = priceForFullRaceRepository.findAllByTotalPrice(BigDecimal.ZERO);
        log.info("races without total price for one seat size = {}", races.size());
        return races;
    }

    @Transactional
    public List<PriceFullOneRaceAnalysis> findAllRacesWithoutAveragePrice() {
        var races = priceForFullRaceRepository.findAllByAveragePriceForOneSeat(BigDecimal.ZERO);
        log.info("races without average price for one seat size = {}", races.size());
        return races;
    }

    /**
     * Еще одна из главных функция для DB 4.
     * Тут мы считаем цену для всех рейсов, для которых цена не известна.
     * А именно - считаем среднюю цену за один билет и деньги которые в общем были вложены в самолёт.
     * Все эти значения мы высчитываем исходя из рейсов с наиболее похожей информацией, для готовых эта информация известна
     */
    @Transactional
    public void calculateMissingPrices() {
        List<PriceFullOneRaceAnalysis> flightsWithoutPrice = findAllRacesWithoutAveragePrice();
        List<PriceFullOneRaceAnalysis> allFlights = priceForFullRaceRepository.findAll();

        Map<String, List<PriceFullOneRaceAnalysis>> flightsByDepartureCityAndArrivalCityAndAircraftCode = allFlights.stream()
                .collect(Collectors.groupingBy(flight -> flight.getDepartureCity() + "_" + flight.getArrivalCity() + "_" + flight.getAircraftCode()));

        Map<String, List<PriceFullOneRaceAnalysis>> flightsByDepartureCityAndArrivalCity = allFlights.stream()
                .collect(Collectors.groupingBy(flight -> flight.getDepartureCity() + "_" + flight.getArrivalCity()));

        Map<String, List<PriceFullOneRaceAnalysis>> flightsByDepartureCityAndAircraftCode = allFlights.stream()
                .collect(Collectors.groupingBy(flight -> flight.getDepartureCity() + "_" + flight.getAircraftCode()));

        Map<String, List<PriceFullOneRaceAnalysis>> flightsByArrivalCityAndAircraftCode = allFlights.stream()
                .collect(Collectors.groupingBy(flight -> flight.getArrivalCity() + "_" + flight.getAircraftCode()));

        Map<String, List<PriceFullOneRaceAnalysis>> flightsByDepartureCity = allFlights.stream()
                .collect(Collectors.groupingBy(PriceFullOneRaceAnalysis::getDepartureCity));

        Map<String, List<PriceFullOneRaceAnalysis>> flightsByArrivalCity = allFlights.stream()
                .collect(Collectors.groupingBy(PriceFullOneRaceAnalysis::getArrivalCity));

        Map<String, List<PriceFullOneRaceAnalysis>> flightsByAircraftCode = allFlights.stream()
                .collect(Collectors.groupingBy(PriceFullOneRaceAnalysis::getAircraftCode));

        flightsWithoutPrice.forEach(currentFlight -> {
            BigDecimal averagePrice = currentFlight.getAveragePriceForOneSeat();

            if (averagePrice != null && !averagePrice.equals(BigDecimal.ZERO)) {
                return;
            }

            AvgPriceDto avgPriceDto = getFilteredFlights(
                    currentFlight, flightsByDepartureCityAndArrivalCityAndAircraftCode, flightsByDepartureCityAndArrivalCity, flightsByDepartureCityAndAircraftCode,
                    flightsByArrivalCityAndAircraftCode, flightsByDepartureCity, flightsByArrivalCity, flightsByAircraftCode);


            currentFlight.setAveragePriceForOneSeat(avgPriceDto.getAvgPriceForAnySeat());
            currentFlight.setAveragePriceForOneEconomySeat(avgPriceDto.getAvgPriceForEconomySeat());
            currentFlight.setAveragePriceForOneBusinessSeat(avgPriceDto.getAvgPriceForBusinessSeat());
            currentFlight.setAveragePriceForOneComfortSeat(avgPriceDto.getAvgPriceForComfortSeat());
            priceForFullRaceRepository.save(currentFlight);
        });
    }

    private AvgPriceDto getFilteredFlights(PriceFullOneRaceAnalysis currentFlight,
                                           Map<String, List<PriceFullOneRaceAnalysis>> flightsByCityAndAircraft,
                                           Map<String, List<PriceFullOneRaceAnalysis>> flightsByCity,
                                           Map<String, List<PriceFullOneRaceAnalysis>> flightsByDepartureCityAndAircraft,
                                           Map<String, List<PriceFullOneRaceAnalysis>> flightsByArrivalCityAndAircraft,
                                           Map<String, List<PriceFullOneRaceAnalysis>> flightsByDepartureCity,
                                           Map<String, List<PriceFullOneRaceAnalysis>> flightsByArrivalCity,
                                           Map<String, List<PriceFullOneRaceAnalysis>> flightsByAircraftCode) {
        String key = currentFlight.getDepartureCity() + "_" + currentFlight.getArrivalCity() + "_" + currentFlight.getAircraftCode();
        String cityKey = currentFlight.getDepartureCity() + "_" + currentFlight.getArrivalCity();
        String departureAircraftKey = currentFlight.getDepartureCity() + "_" + currentFlight.getAircraftCode();
        String arrivalAircraftKey = currentFlight.getArrivalCity() + "_" + currentFlight.getAircraftCode();

        List<PriceFullOneRaceAnalysis> similarFlights = flightsByCityAndAircraft.getOrDefault(key, Collections.emptyList());
        AvgPriceDto avgPriceDto = getAveragePrice(similarFlights);

        if (similarFlights.isEmpty() || avgPriceDto.getAvgPriceForAnySeat() == null || avgPriceDto.getAvgPriceForAnySeat().equals(BigDecimal.ZERO)) {
            similarFlights = flightsByCity.getOrDefault(cityKey, Collections.emptyList());
            avgPriceDto = getAveragePrice(similarFlights);
        }
        if (similarFlights.isEmpty() || avgPriceDto.getAvgPriceForAnySeat() == null || avgPriceDto.getAvgPriceForAnySeat().equals(BigDecimal.ZERO)) {
            similarFlights = flightsByDepartureCityAndAircraft.getOrDefault(departureAircraftKey, Collections.emptyList());
            avgPriceDto = getAveragePrice(similarFlights);
        }
        if (similarFlights.isEmpty() || avgPriceDto.getAvgPriceForAnySeat() == null || avgPriceDto.getAvgPriceForAnySeat().equals(BigDecimal.ZERO)) {
            similarFlights = flightsByArrivalCityAndAircraft.getOrDefault(arrivalAircraftKey, Collections.emptyList());
            avgPriceDto = getAveragePrice(similarFlights);
        }
        if (similarFlights.isEmpty() || avgPriceDto.getAvgPriceForAnySeat() == null || avgPriceDto.getAvgPriceForAnySeat().equals(BigDecimal.ZERO)) {
            similarFlights = flightsByDepartureCity.getOrDefault(currentFlight.getDepartureCity(), Collections.emptyList());
            avgPriceDto = getAveragePrice(similarFlights);
        }
        if (similarFlights.isEmpty() || avgPriceDto.getAvgPriceForAnySeat() == null || avgPriceDto.getAvgPriceForAnySeat().equals(BigDecimal.ZERO)) {
            similarFlights = flightsByArrivalCity.getOrDefault(currentFlight.getArrivalCity(), Collections.emptyList());
            avgPriceDto = getAveragePrice(similarFlights);
        }
        if (similarFlights.isEmpty() || avgPriceDto.getAvgPriceForAnySeat() == null || avgPriceDto.getAvgPriceForAnySeat().equals(BigDecimal.ZERO)) {
            similarFlights = flightsByAircraftCode.getOrDefault(currentFlight.getAircraftCode(), Collections.emptyList());
            avgPriceDto = getAveragePrice(similarFlights);
        }
        if (similarFlights.isEmpty()) {
            throw new RuntimeException("No similar flights found for " + currentFlight.getFlightNumber());
        }
        return avgPriceDto;
    }


    private AvgPriceDto getAveragePrice(List<PriceFullOneRaceAnalysis> similarFlights) {
        if (similarFlights.isEmpty()) {
            return new AvgPriceDto(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        }
        BigDecimal averagePriceForOneSeatFlight = BigDecimal.ZERO;
        BigDecimal averagePriceForOneSeatFlightEconomy = BigDecimal.ZERO;
        BigDecimal averagePriceForOneSeatFlightComfort = BigDecimal.ZERO;
        BigDecimal averagePriceForOneSeatFlightBusiness = BigDecimal.ZERO;

        int flightsNumber = similarFlights.size();
        for (var flight : similarFlights) {
            var currentFlightPrice = flight.getTotalPrice();
            if (currentFlightPrice.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }

            averagePriceForOneSeatFlightBusiness = flight.getAveragePriceForOneBusinessSeat();
            averagePriceForOneSeatFlightEconomy = flight.getAveragePriceForOneEconomySeat();
            averagePriceForOneSeatFlightComfort = flight.getAveragePriceForOneComfortSeat();
            averagePriceForOneSeatFlight = flight.getAveragePriceForOneSeat();
        }
        var averagePriceForOneEconomySeatFinal = averagePriceForOneSeatFlightEconomy.divide(BigDecimal.valueOf(flightsNumber), RoundingMode.HALF_UP);
        var averagePriceForOneBusinessSeatFinal = averagePriceForOneSeatFlightBusiness.divide(BigDecimal.valueOf(flightsNumber), RoundingMode.HALF_UP);
        var averagePriceForOneComfortSeatFinal = averagePriceForOneSeatFlightComfort.divide(BigDecimal.valueOf(flightsNumber), RoundingMode.HALF_UP);
        var averagePriceForOneAnySeatFinal = averagePriceForOneSeatFlight.divide(BigDecimal.valueOf(flightsNumber), RoundingMode.HALF_UP);

        return new AvgPriceDto(averagePriceForOneEconomySeatFinal, averagePriceForOneBusinessSeatFinal,
                averagePriceForOneComfortSeatFinal, averagePriceForOneAnySeatFinal);
    }
}
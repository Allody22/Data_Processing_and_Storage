package ru.nsu.task4.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.task4.model.Flights;
import ru.nsu.task4.model.PriceFullOneRaceAnalysis;
import ru.nsu.task4.model.Seats;
import ru.nsu.task4.model.TicketFlights;
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
        List<TicketFlights> allTicketFlights = ticketsFlightsRepository.findAll();

        List<Seats> allSeats = seatsRepository.getAll();


        Map<String, Long> totalSeatsByAircraft = allSeats.stream()
                .collect(Collectors.groupingBy(seat -> seat.getAircraft().getAircraftCode().trim(), Collectors.counting()));
        totalSeatsByAircraft.put("CN1", 12L);
        totalSeatsByAircraft.put("CR2", 50L);
        totalSeatsByAircraft.put("SU9", 97L);

        Map<Long, List<TicketFlights>> ticketsByFlightId = allTicketFlights.stream()
                .collect(Collectors.groupingBy(tf -> tf.getFlight().getFlightId()));

        List<PriceFullOneRaceAnalysis> analyses = new ArrayList<>();

        allFlights.forEach(flight -> {
            Long totalSeats = totalSeatsByAircraft.getOrDefault(flight.getAircraftCode(), 0L);
            if (totalSeats == 0L) {
                log.info("flights = " + flight.getAircraftCode());
            }
            List<TicketFlights> tickets = ticketsByFlightId.getOrDefault(flight.getFlightId(), Collections.emptyList());
            BigDecimal totalPrice = tickets.stream()
                    .map(TicketFlights::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            int seatsSold = tickets.size();
            BigDecimal averagePriceForOneSeat = seatsSold > 0 ? totalPrice.divide(BigDecimal.valueOf(seatsSold), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

            PriceFullOneRaceAnalysis analysis = new PriceFullOneRaceAnalysis();
            analysis.setFlightUid(flight.getFlightId());
            analysis.setFlightNumber(flight.getFlightNo());
            analysis.setDepartureTime(flight.getScheduledDeparture());
            analysis.setArrivalTime(flight.getScheduledArrival());
            analysis.setDepartureCity(flight.getDepartureAirport().getCity());
            analysis.setArrivalCity(flight.getArrivalAirport().getCity());
            analysis.setTotalPrice(totalPrice);
            analysis.setTotalSeatsNumber(totalSeats.intValue());
            analysis.setSoldSeatsNumber(seatsSold);
            analysis.setAveragePriceForOneSeat(averagePriceForOneSeat);
            analysis.setAircraftCode(flight.getAircraftCode());

            analyses.add(analysis);
        });

        priceForFullRaceRepository.saveAll(analyses);
    }

    @Transactional
    public List<PriceFullOneRaceAnalysis> findAllRacesWithoutTotalPrice() {
        var races = priceForFullRaceRepository.findAllByTotalPrice(BigDecimal.ZERO);
        log.info("races without total price for one seat size = " + races.size());
        return races;
    }

    @Transactional
    public List<PriceFullOneRaceAnalysis> findAllRacesWithoutAveragePrice() {
        var races = priceForFullRaceRepository.findAllByAveragePriceForOneSeat(BigDecimal.ZERO);
        log.info("races without average price for one seat size = " + races.size());
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

        flightsWithoutPrice.forEach(currentFlight -> {
            BigDecimal totalPrice = currentFlight.getTotalPrice();
            int totalSeatsSold = currentFlight.getTotalSeatsNumber();
            int totalSeats = currentFlight.getTotalSeatsNumber();
            BigDecimal averagePriceForOneSeat = BigDecimal.ZERO;

            if (totalPrice != null && !totalPrice.equals(BigDecimal.ZERO)){
                if (totalSeatsSold > 0) {
                    averagePriceForOneSeat = totalPrice.divide(BigDecimal.valueOf(totalSeatsSold), 2, RoundingMode.HALF_UP);
                } else if (totalSeats > 0) {
                    averagePriceForOneSeat = totalPrice.divide(BigDecimal.valueOf(totalSeats), 2, RoundingMode.HALF_UP);
                } else {
                    throw new RuntimeException("No seats  found");
                }
            }

            if (totalPrice.equals(BigDecimal.ZERO) || averagePriceForOneSeat.equals(BigDecimal.ZERO)) {
                List<PriceFullOneRaceAnalysis> similarFlights = new ArrayList<>();
                similarFlights = priceForFullRaceRepository.findAllByDepartureCityAndArrivalCityAndAircraftCodeAndTotalPriceGreaterThan(
                        currentFlight.getDepartureCity(),
                        currentFlight.getArrivalCity(),
                        currentFlight.getAircraftCode(),
                        BigDecimal.ZERO);

                averagePriceForOneSeat = BigDecimal.ZERO;

                if (!similarFlights.isEmpty()) {
                    averagePriceForOneSeat = getAveragePrice(similarFlights, averagePriceForOneSeat);
                }

                if (similarFlights.isEmpty() || averagePriceForOneSeat.equals(BigDecimal.ZERO)) {
                    similarFlights = priceForFullRaceRepository.findAllByDepartureCityAndArrivalCityAndTotalPriceGreaterThan(
                            currentFlight.getDepartureCity(),
                            currentFlight.getArrivalCity(),
                            BigDecimal.ZERO);
                    if (!similarFlights.isEmpty()) {
                        averagePriceForOneSeat = getAveragePrice(similarFlights, averagePriceForOneSeat);
                    }
                }

                if (similarFlights.isEmpty() || averagePriceForOneSeat.equals(BigDecimal.ZERO)) {
                    similarFlights = priceForFullRaceRepository.findAllByDepartureCityAndAircraftCodeAndTotalPriceGreaterThan(
                            currentFlight.getDepartureCity(),
                            currentFlight.getAircraftCode(),
                            BigDecimal.ZERO);

                    if (!similarFlights.isEmpty()) {
                        averagePriceForOneSeat = getAveragePrice(similarFlights, averagePriceForOneSeat);
                    }
                }

                if (similarFlights.isEmpty() || averagePriceForOneSeat.equals(BigDecimal.ZERO)) {
                    similarFlights = priceForFullRaceRepository.findAllByArrivalCityAndAircraftCodeAndTotalPriceGreaterThan(
                            currentFlight.getArrivalCity(),
                            currentFlight.getAircraftCode(),
                            BigDecimal.ZERO);
                    if (!similarFlights.isEmpty()) {
                        averagePriceForOneSeat = getAveragePrice(similarFlights, averagePriceForOneSeat);
                    }
                }

                if (similarFlights.isEmpty() || averagePriceForOneSeat.equals(BigDecimal.ZERO)) {
                    similarFlights = priceForFullRaceRepository.findAllByArrivalCityAndTotalPriceGreaterThan(
                            currentFlight.getArrivalCity(),
                            BigDecimal.ZERO);
                    if (!similarFlights.isEmpty()) {
                        averagePriceForOneSeat = getAveragePrice(similarFlights, averagePriceForOneSeat);
                    }
                }

                if (similarFlights.isEmpty() || averagePriceForOneSeat.equals(BigDecimal.ZERO)) {
                    similarFlights = priceForFullRaceRepository.findAllByDepartureCityAndTotalPriceGreaterThan(
                            currentFlight.getDepartureCity(),
                            BigDecimal.ZERO);
                    if (!similarFlights.isEmpty()) {
                        averagePriceForOneSeat = getAveragePrice(similarFlights, averagePriceForOneSeat);
                    }
                }


                if (similarFlights.isEmpty() || averagePriceForOneSeat.equals(BigDecimal.ZERO)) {
                    similarFlights = priceForFullRaceRepository.findAllByAircraftCodeAndTotalPriceGreaterThan(
                            currentFlight.getAircraftCode(),
                            BigDecimal.ZERO);
                    if (!similarFlights.isEmpty()) {
                        averagePriceForOneSeat = getAveragePrice(similarFlights, averagePriceForOneSeat);
                    }
                }

                if (similarFlights.isEmpty()) {
                    throw new RuntimeException("No similar flights found");
                }

                BigDecimal averageTotalPrice = similarFlights.stream()
                        .map(PriceFullOneRaceAnalysis::getTotalPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(new BigDecimal(similarFlights.size()), RoundingMode.HALF_UP);

                currentFlight.setAveragePriceForOneSeat(averagePriceForOneSeat);
                currentFlight.setTotalPrice(averageTotalPrice);
                priceForFullRaceRepository.save(currentFlight);
            }
            currentFlight.setAveragePriceForOneSeat(averagePriceForOneSeat);
            currentFlight.setTotalPrice(totalPrice);
            priceForFullRaceRepository.save(currentFlight);
        });
    }

    private BigDecimal getAveragePrice(List<PriceFullOneRaceAnalysis> similarFlights, BigDecimal averagePriceForOneSeat) {
        BigDecimal totalPricesSum;
        int totalSeatsSold;
        int totalSeats;
        totalPricesSum = similarFlights.stream()
                .filter(flight -> flight.getTotalPrice().compareTo(BigDecimal.ZERO) > 0) // Фильтруем рейсы с ненулевой ценой
                .map(flight -> flight.getTotalPrice().multiply(BigDecimal.valueOf(flight.getSoldSeatsNumber())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalSeatsSold = similarFlights.stream()
                .filter(flight -> flight.getTotalPrice().compareTo(BigDecimal.ZERO) > 0) // Повторяем фильтрацию для согласованности
                .mapToInt(PriceFullOneRaceAnalysis::getSoldSeatsNumber)
                .sum();

        totalSeats = similarFlights.stream()
                .mapToInt(PriceFullOneRaceAnalysis::getTotalSeatsNumber)
                .sum();

        if (totalSeatsSold > 0) {
            averagePriceForOneSeat = totalPricesSum.divide(BigDecimal.valueOf(totalSeatsSold), 2, RoundingMode.HALF_UP);
        } else if (totalSeats > 0) {
            averagePriceForOneSeat = totalPricesSum.divide(BigDecimal.valueOf(totalSeats), 2, RoundingMode.HALF_UP);
        } else {
            throw new RuntimeException("No seats at all");
        }
        return averagePriceForOneSeat;
    }
}
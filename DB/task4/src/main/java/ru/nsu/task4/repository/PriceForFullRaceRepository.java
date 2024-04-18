package ru.nsu.task4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.task4.model.PriceFullOneRaceAnalysis;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PriceForFullRaceRepository extends JpaRepository<PriceFullOneRaceAnalysis, Long> {

    Optional<PriceFullOneRaceAnalysis> findByFlightUid(Long flightUid);

    List<PriceFullOneRaceAnalysis> findAllByTotalPrice(BigDecimal bigDecimal);

    List<PriceFullOneRaceAnalysis> findAllByAveragePriceForOneSeat(BigDecimal bigDecimal);

    List<PriceFullOneRaceAnalysis> findAllByDepartureCityAndArrivalCityAndAircraftCodeAndTotalPriceGreaterThan(String departureCity, String arrivalCity,
                                                                                                               String aircraftCode, BigDecimal price);

    List<PriceFullOneRaceAnalysis> findAllByDepartureCityAndArrivalCityAndTotalPriceGreaterThan(String departureCity, String arrivalCity, BigDecimal price);

    List<PriceFullOneRaceAnalysis> findAllByDepartureCityAndAircraftCodeAndTotalPriceGreaterThan(String departureCity, String aircraftCode,
                                                                                                 BigDecimal price);

    List<PriceFullOneRaceAnalysis> findAllByArrivalCityAndAircraftCodeAndTotalPriceGreaterThan(String arrivalCity,
                                                                                               String aircraftCode, BigDecimal price);

    List<PriceFullOneRaceAnalysis> findAllByDepartureCityAndTotalPriceGreaterThan(String departureCity, BigDecimal price);

    List<PriceFullOneRaceAnalysis> findAllByArrivalCityAndTotalPriceGreaterThan(String arrivalCity, BigDecimal price);

    List<PriceFullOneRaceAnalysis> findAllByAircraftCodeAndTotalPriceGreaterThan(String aircraftCode, BigDecimal totalPrice);

}
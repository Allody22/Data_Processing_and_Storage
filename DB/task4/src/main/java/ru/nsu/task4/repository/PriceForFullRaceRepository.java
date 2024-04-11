package ru.nsu.task4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.task4.model.Airport;
import ru.nsu.task4.model.PriceFullOneRaceAnalysis;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PriceForFullRaceRepository extends JpaRepository<PriceFullOneRaceAnalysis, Long> {

    Optional<PriceFullOneRaceAnalysis> findByFlightUid(Long flightUid);

    @Transactional
    @Query(value = "SELECT * FROM price_full_one_race_analysis WHERE (->>'en' = :cityName OR city->>'ru' = :cityName)", nativeQuery = true)
    List<Airport> findAllArrivalRaces(@Param("cityName") String cityName);

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
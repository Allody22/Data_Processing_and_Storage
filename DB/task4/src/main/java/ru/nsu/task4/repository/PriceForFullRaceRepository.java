package ru.nsu.task4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.nsu.task4.model.PriceFullOneRaceAnalysis;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PriceForFullRaceRepository extends JpaRepository<PriceFullOneRaceAnalysis, Long> {

    @Query(value = "SELECT * FROM price_full_one_race_analysis p WHERE " +
            "(CAST(p.departure_airport_name AS json) ->> 'en' = :departureAirport OR CAST(p.departure_airport_name AS json) ->> 'ru' = :departureAirport OR CAST(p.departure_city AS json) ->> 'ru' = :departureAirport OR CAST(p.departure_city AS json) ->> 'en' = :departureAirport) AND " +
            "(CAST(p.arrival_airport_name AS json) ->> 'en' = :arrivalAirport OR CAST(p.arrival_airport_name AS json) ->> 'ru' = :arrivalAirport OR CAST(p.arrival_city AS json) ->> 'ru' = :departureAirport OR CAST(p.arrival_city AS json) ->> 'en' = :departureAirport) AND " +
            "p.departure_time BETWEEN :minDepartureTime AND :maxDepartureTime", nativeQuery = true)
    List<PriceFullOneRaceAnalysis> findFlightsByAirportNamesAndDepartureTime(
            @Param("departureAirport") String departureAirport,
            @Param("arrivalAirport") String arrivalAirport,
            @Param("minDepartureTime") OffsetDateTime minDepartureTime,
            @Param("maxDepartureTime") OffsetDateTime maxDepartureTime);

    Optional<PriceFullOneRaceAnalysis> findByFlightUid(Long flightUid);

    List<PriceFullOneRaceAnalysis> findAllByTotalPrice(BigDecimal bigDecimal);

    List<PriceFullOneRaceAnalysis> findAllByAveragePriceForOneSeat(BigDecimal bigDecimal);
}
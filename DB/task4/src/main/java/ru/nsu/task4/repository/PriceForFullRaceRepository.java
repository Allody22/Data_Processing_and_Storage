package ru.nsu.task4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.task4.model.Airport;
import ru.nsu.task4.model.PriceFullOneRaceAnalysis;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Date;
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


    @Query(value = "SELECT * FROM price_full_one_race_analysis p WHERE " +
            "(CAST(p.departure_airport_name AS json) ->> 'en' = :departureAirport OR CAST(p.departure_airport_name AS json) ->> 'ru' = :departureAirport) AND " +
            "(CAST(p.arrival_airport_name AS json) ->> 'en' = :arrivalAirport OR CAST(p.arrival_airport_name AS json) ->> 'ru' = :arrivalAirport)", nativeQuery = true)
    List<PriceFullOneRaceAnalysis> findConnected(
            @Param("departureAirport") String departureAirport,
            @Param("arrivalAirport") String arrivalAirport);

    @Query(value = "SELECT * FROM price_full_one_race_analysis WHERE departure_time BETWEEN :minDepartureTime AND :maxDepartureTime", nativeQuery = true)
    List<PriceFullOneRaceAnalysis> findByDepartureTimeBetween(@Param("minDepartureTime") OffsetDateTime minDepartureTime, @Param("maxDepartureTime") OffsetDateTime maxDepartureTime);


    List<PriceFullOneRaceAnalysis> findByDepartureAirportNameAndArrivalAirportNameAndDepartureTime(String departureAirportName, String arrivalAirportName, OffsetDateTime departureTime);

    @Query("SELECT p FROM PriceFullOneRaceAnalysis p WHERE " +
            "((p.departureAirportName = :from OR p.departureCity = :from) AND " +
            "(p.arrivalAirportName = :to OR p.arrivalCity = :to)) AND " +
            "p.departureTime >= :startDate AND p.departureTime < :endDate")
    List<PriceFullOneRaceAnalysis> findFlights(
            @Param("from") String from,
            @Param("to") String to,
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate);

    Optional<PriceFullOneRaceAnalysis> findByFlightUid(Long flightUid);

    Optional<PriceFullOneRaceAnalysis> findByFlightNumber(String flightNumber);

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
package ru.nsu.task4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.task4.model.TicketFlights;
import ru.nsu.task4.model.dto.FlightSoldCodeSeatsCountDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketsFlightsRepository extends JpaRepository<TicketFlights, String> {

    boolean existsByFlight_FlightIdAndFareCondition(Long flightId, String fareCondition);

    Optional<TicketFlights> findByTicket_TicketNumber(String ticketNumber);

    List<TicketFlights> findByFlight_FlightId(Long flightId);

    @Query("SELECT new ru.nsu.task4.model.dto.FlightSoldCodeSeatsCountDTO(" +
            "tf.flight.flightId, tf.flight.aircraftCode, " +
            "SUM(CASE WHEN tf.fareCondition = 'Economy' THEN 1 ELSE 0 END), " +
            "SUM (CASE WHEN tf.fareCondition = 'Economy' THEN tf.amount ELSE null END), " +
            "SUM(CASE WHEN tf.fareCondition = 'Business' THEN 1 ELSE 0 END), " +
            "SUM (CASE WHEN tf.fareCondition = 'Business' THEN tf.amount ELSE null END), " +
            "SUM(CASE WHEN tf.fareCondition = 'Comfort' THEN 1 ELSE 0 END), " +
            "SUM (CASE WHEN tf.fareCondition = 'Comfort' THEN tf.amount ELSE null END)) " +
            "FROM TicketFlights tf GROUP BY tf.flight.flightId, tf.flight.aircraftCode")
    List<FlightSoldCodeSeatsCountDTO> getAllSoldSeatsCount();


    List<TicketFlights> findByFlight_FlightIdAndFareConditionAndAmountGreaterThan(Long flightId, String fareCondition, BigDecimal amount);
}
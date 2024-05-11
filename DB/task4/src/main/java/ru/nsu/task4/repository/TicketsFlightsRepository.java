package ru.nsu.task4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.task4.model.TicketFlights;
import ru.nsu.task4.model.dto.AvgPriceDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketsFlightsRepository extends JpaRepository<TicketFlights, String> {

    boolean existsByFlight_FlightIdAndFareCondition(Long flightId, String fareCondition);

    Optional<TicketFlights> findByTicket_TicketNumber(String ticketNumber);

    List<TicketFlights> findByFlight_FlightId(Long flightId);

    @Query("SELECT new ru.nsu.task4.model.dto.AvgPriceDto(tf.flight.flightId, AVG(tf.amount)) " +
            "FROM TicketFlights tf " +
            "GROUP BY tf.flight.flightId")
    List<AvgPriceDto> findAveragePricesForFlights();
}
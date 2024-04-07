package ru.nsu.task4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.task4.model.TicketFlights;
import ru.nsu.task4.model.dto.AvgPriceDto;

import java.util.List;

@Repository
public interface TicketsFlightsRepository extends JpaRepository<TicketFlights, String> {

    @Query("SELECT new ru.nsu.task4.model.dto.AvgPriceDto(tf.flight.flightId, AVG(tf.amount)) " +
            "FROM TicketFlights tf " +
            "GROUP BY tf.flight.flightId")
    List<AvgPriceDto> findAveragePricesForFlights();
}
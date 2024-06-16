package ru.nsu.task4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.task4.model.Flights;

import java.util.Optional;

@Repository
public interface FlightsRepository extends JpaRepository<Flights, Long> {
    Optional<Flights> findByFlightNo(String flightNo);

    Optional<Flights> findByFlightId(Long flightId);

    Optional<Flights> findAllByFlightId(Long flightId);

}

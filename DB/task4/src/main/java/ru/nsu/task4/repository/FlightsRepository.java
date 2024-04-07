package ru.nsu.task4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.task4.model.Flights;

@Repository
public interface FlightsRepository extends JpaRepository<Flights, Long> {
}

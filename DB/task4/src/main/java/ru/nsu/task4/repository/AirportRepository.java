package ru.nsu.task4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.task4.model.Airport;

import java.util.List;

@Repository
public interface AirportRepository extends JpaRepository<Airport, String> {

    @Override
    List<Airport> findAll();
}

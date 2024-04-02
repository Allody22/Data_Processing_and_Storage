package ru.nsu.task4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.task4.model.Aircraft;

import java.util.List;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, String> {

    @Override
    List<Aircraft> findAll();
}

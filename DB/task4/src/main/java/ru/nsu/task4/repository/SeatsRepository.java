package ru.nsu.task4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.task4.model.Seats;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface SeatsRepository extends JpaRepository<Seats, String> {

    @Transactional
    @Query(value = "SELECT * FROM seats", nativeQuery = true)
    List<Seats> getAll();

    @Query("SELECT s FROM Seats s JOIN FETCH s.aircraft")
    List<Seats> findAllSeatsWithAircraft();

}

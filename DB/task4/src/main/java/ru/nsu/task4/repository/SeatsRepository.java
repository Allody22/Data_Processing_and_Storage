package ru.nsu.task4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.task4.model.Seats;
import ru.nsu.task4.model.dto.AircraftTotalCodeSeatsCountDTO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeatsRepository extends JpaRepository<Seats, String> {

    Optional<Seats> findBySeatNoAndAircraft_AircraftCodeAndFareCondition(String seatNo, String aircraftCode, String fareCondition);

    boolean existsBySeatNoAndAircraft_AircraftCodeAndFareCondition(String seatNo, String aircraftCode, String fareCondition);

    @Transactional
    @Query(value = "SELECT * FROM seats", nativeQuery = true)
    List<Seats> getAll();

    @Query("SELECT s FROM Seats s JOIN FETCH s.aircraft")
    List<Seats> findAllSeatsWithAircraft();

    @Transactional
    @Query(value = "SELECT new ru.nsu.task4.model.dto.AircraftTotalCodeSeatsCountDTO(s.aircraft.aircraftCode, COUNT(s), " +
            "SUM(CASE WHEN s.fareCondition = 'Economy' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN s.fareCondition = 'Business' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN s.fareCondition = 'Comfort' THEN 1 ELSE 0 END)) " +
            "FROM Seats s GROUP BY s.aircraft.aircraftCode")
    List<AircraftTotalCodeSeatsCountDTO> countSeatsByAircraftCode();
}

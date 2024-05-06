package ru.nsu.task4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.task4.model.BoardingPass;

import java.util.List;

@Repository
public interface BoardingPassRepository extends JpaRepository<BoardingPass, String> {

    List<BoardingPass> findByFlightIdAndSeatNo(Long flightId, String seatNo);
}

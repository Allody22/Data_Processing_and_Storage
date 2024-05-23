package ru.nsu.task4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.nsu.task4.model.BoardingPass;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardingPassRepository extends JpaRepository<BoardingPass, String> {

    List<BoardingPass> findByFlightIdAndSeatNo(Long flightId, String seatNo);

    boolean existsByTicketNoAndFlightId(String ticketNo, Long flightId);

    @Query("SELECT COALESCE(MAX(bp.boardingNo), 0) FROM BoardingPass bp WHERE bp.flightId = :flightId")
    int findMaxBoardingNoByFlightId(Long flightId);

    @Query("SELECT bp.seatNo FROM BoardingPass bp WHERE bp.flightId = :flightId")
    List<String> findSeatsAllByFlightId(@Param("flightId") Long flightId);

    @Query("SELECT bp FROM BoardingPass bp WHERE bp.flightId = :flightId")
    List<BoardingPass> findAllByFlightId(@Param("flightId") Long flightId);

    @Query("SELECT bp.seatNo " +
            "FROM BoardingPass bp " +
            "JOIN bp.ticketFlight tf " +
            "WHERE tf.flight.flightId = :flightId " +
            "AND tf.fareCondition = :fareCondition")
    List<String> findOccupiedSeatsByFlightIdAndFareCondition(@Param("flightId") Long flightId, @Param("fareCondition") String fareCondition);

    Optional<BoardingPass> findByTicketNo(String ticketNo);
}

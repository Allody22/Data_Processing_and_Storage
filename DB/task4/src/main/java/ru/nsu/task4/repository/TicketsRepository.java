package ru.nsu.task4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.nsu.task4.model.Tickets;

import java.util.Optional;

@Repository
public interface TicketsRepository extends JpaRepository<Tickets, String> {

    boolean existsByTicketNumber(String ticketNo);

    @Modifying
    @Query(value = "INSERT INTO tickets (ticket_no, passenger_id, passenger_name, contact_data, book_ref) VALUES (:ticketNo, :passengerId, :passengerName, CAST(:contactData AS jsonb), :bookRef)", nativeQuery = true)
    void insertTicket(@Param("ticketNo") String ticketNo, @Param("passengerId") String passengerId, @Param("passengerName") String passengerName, @Param("contactData") String contactData, @Param("bookRef") String bookRef);

    Optional<Tickets> findByTicketNumber(String ticketNumber);

    Optional<Tickets> findByBooking_BookRef(String bookRef);
}

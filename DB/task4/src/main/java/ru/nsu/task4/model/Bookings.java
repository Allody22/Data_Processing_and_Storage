package ru.nsu.task4.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "bookings")
@Data
public class Bookings {

    @Id
    @Column(name = "book_ref", nullable = false)
    private String bookRef; //Booking number

    @Column(name = "book_date", nullable = false)
    private OffsetDateTime bookDate;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount; // Цена на заказ

    @OneToMany(mappedBy = "booking")
    @ToString.Exclude
    @JsonIgnore
    private List<Tickets> tickets;
}

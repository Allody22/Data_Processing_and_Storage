package ru.nsu.task4.model;

import lombok.Data;
import ru.nsu.task4.payloads.response.AirportNameResponse;

import javax.persistence.*;

@SqlResultSetMapping(
        name = "AirportNameResponseMapping",
        classes = @ConstructorResult(
                targetClass = AirportNameResponse.class,
                columns = {
                        @ColumnResult(name = "airportCode", type = String.class),
                        @ColumnResult(name = "airportName", type = String.class)
                }
        )
)
@Entity
@Table(name = "airports_data")
@Data
public class Airport {

    @Id
    @Column(name = "airport_code")
    private String airportCode;

    @Column(name = "airport_name", columnDefinition = "jsonb")
    private String airportName; // JSON-строка

    @Column(name = "city", columnDefinition = "jsonb")
    private String city; // JSON-строка

    @Column(name = "timezone")
    private String timezone; //Airport time zone
}

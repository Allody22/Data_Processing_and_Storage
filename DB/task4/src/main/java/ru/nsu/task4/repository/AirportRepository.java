package ru.nsu.task4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.task4.model.Airport;

import java.util.List;

@Repository
public interface AirportRepository extends JpaRepository<Airport, String> {

    @Override
    List<Airport> findAll();

    @Transactional
    List<Airport> findAllByCityContaining(String city);

    //city->>'en' - Обращение к JSON-полю city, из которого извлекается значение по
    //    ключу 'en' как текст (оператор ->> возвращает JSON-объект как текст).
    @Transactional
    @Query(value = "SELECT * FROM airports_data WHERE (city->>'en' = :cityName OR city->>'ru' = :cityName)", nativeQuery = true)
    List<Airport> findAllAirportsInTheCityByRuOrEnglishName(@Param("cityName") String cityName);
}

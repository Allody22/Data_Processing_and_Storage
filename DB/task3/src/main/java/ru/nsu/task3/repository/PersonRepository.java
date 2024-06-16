package ru.nsu.task3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.task3.model.Person;

import java.util.Set;

@Repository
public interface PersonRepository  extends JpaRepository<Person, Long> {

    // Получение списка братьев и сестер для заданного Person
    @Transactional
    @Query("SELECT p FROM Person p WHERE (p.mother = :mother AND p.father = :father) AND p.id != :personId")
    Set<Person> findSiblings(@Param("mother") Person mother, @Param("father") Person father, @Param("personId") Long personId);

}

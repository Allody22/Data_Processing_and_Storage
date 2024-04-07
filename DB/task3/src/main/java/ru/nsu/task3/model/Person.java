package ru.nsu.task3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.nsu.task3.model.constants.Gender;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "person")
@Data
@NoArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uid_from_xml", nullable = false, length = 40)
    private String uid_from_xml;

    //Имя точно не юник
    @Column(name = "person_name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 1, name = "gender", nullable = false)
    private Gender gender;

    //Ну нам детэтч надо подумать. Если сын отказывается от матери, то отказывается ли мать от сына? А delete и рефреш точно не надо
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "mother_id")
    private Person mother;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "father_id")
    private Person father;

    @OneToMany(mappedBy = "mother", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JsonIgnore
    @ToString.Exclude
    private Set<Person> childrenFromMother = new HashSet<>();

    @OneToMany(mappedBy = "father", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JsonIgnore
    @ToString.Exclude
    private Set<Person> childrenFromFather = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "spouse_id", unique = true)
    private Person spouse;
}
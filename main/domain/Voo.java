package com.prettyflights.gates.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_flight")
public class Voo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String vooNum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AircraftCategory aircraftCategory;

    @Column(nullable = false)
    private LocalDateTime chegada;

    @Column(nullable = false)
    private LocalDateTime saida;

    @Column(nullable = false)
    private int passageiroCount;

    public Voo() {}

    public Voo(String vooNum, AircraftCategory aircraftCategory,
                  LocalDateTime chegada, LocalDateTime saida,
                  int passageiroCount) {
        this.vooNum = vooNum;
        this.aircraftCategory = aircraftCategory;
        this.chegada = chegada;
        this.saida = saida;
        this.passageiroCount = passageiroCount;
    }

    //getters e setters omitidos
}
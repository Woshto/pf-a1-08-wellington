package com.prettyflights.gates.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_gate")
public class Gate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private aeronaveCateg maxAeronaveCateg;

    @Column(nullable = false)
    private boolean hasBridge;

    @Column(nullable = false)
    private String terminal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GateStatus status = GateStatus.AVAILABLE;

    public Gate() {}

    public Gate(String codigo, aeronaveCateg maxAeronaveCateg,
                boolean hasBridge, String terminal) {
        this.codigo = codigo;
        this.maxAeronaveCateg = maxAeronaveCateg;
        this.hasBridge = hasBridge;
        this.terminal = terminal;
        this.status = GateStatus.AVAILABLE;
    }

    //getters e setters omitidos
}
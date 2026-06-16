package com.prettyflights.gates.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_gate_allocation")
public class GateAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "voo_id")
    private Voo voo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "gate_id")
    private Gate gate;

    @Column(nullable = false)
    private LocalDateTime allocatedAt;

    @Column(nullable = false)
    private String allocatedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AllocationStatus status = AllocationStatus.ACTIVE;

    public GateAllocation() {}

    public GateAllocation(Voo voo, Gate gate, String allocatedBy) {
        this.Voo = voo;
        this.gate = gate;
        this.allocatedBy = allocatedBy;
        this.allocatedAt = LocalDateTime.now();
        this.status = AllocationStatus.ACTIVE;
    }

    //getters e setters omitidos
}
package com.prettyflights.gates.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_allocation_log")
public class AllocationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "allocation_id")
    private GateAllocation allocation;

    @Column(nullable = false)
    private String changedBy;

    private String changeReason;

    @Column(columnDefinition = "TEXT")
    private String previousState;

    @Column(columnDefinition = "TEXT")
    private String newState;

    @Column(nullable = false)
    private LocalDateTime changedAt;

    public AllocationLog() {}

    public AllocationLog(GateAllocation allocation, String changedBy,
                         String changeReason, String previousState, String newState) {
        this.allocation = allocation;
        this.changedBy = changedBy;
        this.changeReason = changeReason;
        this.previousState = previousState;
        this.newState = newState;
        this.changedAt = LocalDateTime.now();
    }

    //getters e setters omitidos
}
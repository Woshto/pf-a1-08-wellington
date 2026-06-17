package br.univali.es2.prettyflights.main.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_gate_allocation")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "allocated_at", nullable = false)
    private LocalDateTime allocatedAt;

    @Column(name = "allocated_by", nullable = false)
    private String allocatedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AllocationStatus status = AllocationStatus.ACTIVE;

    public GateAllocation(Voo voo, Gate gate, String allocatedBy) {
        this.voo = voo;
        this.gate = gate;
        this.allocatedBy = allocatedBy;
        this.allocatedAt = LocalDateTime.now();
        this.status = AllocationStatus.ACTIVE;
    }

    public void cancel() {
        this.status = AllocationStatus.CANCELLED;
    }

    public boolean isActive() {
        return this.status == AllocationStatus.ACTIVE;
    }
}
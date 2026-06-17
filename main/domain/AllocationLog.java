package br.univali.es2.prettyflights.main.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_allocation_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllocationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "allocation_id")
    private GateAllocation allocation;

    @Column(name = "changed_by", nullable = false)
    private String changedBy;

    @Column(name = "change_reason")
    private String changeReason;

    @Column(name = "previous_state", columnDefinition = "TEXT")
    private String previousState;

    @Column(name = "new_state", columnDefinition = "TEXT")
    private String newState;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    public AllocationLog(GateAllocation allocation, String changedBy,
                         String changeReason, String previousState, String newState) {
        this.allocation = allocation;
        this.changedBy = changedBy;
        this.changeReason = changeReason;
        this.previousState = previousState;
        this.newState = newState;
        this.changedAt = LocalDateTime.now();
    }

    public static AllocationLog createCreationLog(GateAllocation allocation, 
                                                   String createdBy) {
        return new AllocationLog(
            allocation,
            createdBy,
            "Alocação automática inicial",
            null,
            "Alocado no portão: " + allocation.getGate().getCodigo()
        );
    }

    public static AllocationLog createReallocationLog(GateAllocation allocation,
                                                       String changedBy,
                                                       String reason,
                                                       String previousState) {
        return new AllocationLog(
            allocation,
            changedBy,
            reason,
            previousState,
            "Realocado para o portão: " + allocation.getGate().getCodigo()
        );
    }
}
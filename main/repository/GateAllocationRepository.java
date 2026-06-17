package br.univali.es2.prettyflights.main.repository;

import br.univali.es2.prettyflights.main.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GateAllocationRepository extends JpaRepository<GateAllocation, Long> {

    List<GateAllocation> findByGateAndStatus(Gate gate, AllocationStatus status);

    Optional<GateAllocation> findByVoo_VooNumAndStatus(String vooNum, AllocationStatus status);

    @Query("""
        SELECT a FROM GateAllocation a
        WHERE a.gate = :gate
          AND a.status = 'ACTIVE'
          AND a.voo.chegada < :departure
          AND a.voo.saida > :arrival
    """)
    List<GateAllocation> findConflictingAllocations(
        @Param("gate") Gate gate,
        @Param("arrival") LocalDateTime arrival,
        @Param("departure") LocalDateTime departure
    );

    List<GateAllocation> findAllByStatus(AllocationStatus status);

    List<GateAllocation> findByGate(Gate gate);
}
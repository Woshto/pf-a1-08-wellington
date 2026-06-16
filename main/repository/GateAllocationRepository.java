package com.prettyflights.gates.repository;

import com.prettyflights.gates.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GateAllocationRepository extends JpaRepository<GateAllocation, Long> {

    List<GateAllocation> findByGateAndStatus(Gate gate, AllocationStatus status);

    Optional<GateAllocation> findByFlight_FlightNumberAndStatus(String flightNumber, AllocationStatus status);

    @Query("""
        SELECT a FROM GateAllocation a
        WHERE a.gate = :gate
          AND a.status = 'ACTIVE'
          AND a.voo.estimatedArrival < :departure
          AND a.voo.estimatedDeparture > :arrival
    """)
    List<GateAllocation> findConflictingAllocations(
        @Param("gate") Gate gate,
        @Param("arrival") LocalDateTime arrival,
        @Param("departure") LocalDateTime departure
    );
}
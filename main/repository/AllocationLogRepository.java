package com.prettyflights.gates.repository;

import com.prettyflights.gates.model.AllocationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AllocationLogRepository extends JpaRepository<AllocationLog, Long> {
    List<AllocationLog> findByAllocation_Voo_FlightNumberOrderByChangedAtAsc(String vooNumber);
}
package com.prettyflights.gates.repository;

import com.prettyflights.gates.model.Gate;
import com.prettyflights.gates.model.GateStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GateRepositorio extends JpaRepository<Gate, Long> {
    List<Gate> findByStatus(GateStatus status);
}
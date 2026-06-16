package com.prettyflights.gates.repository;

import com.prettyflights.gates.model.Voo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Voo, Long> {
    Optional<Voo> findByFlightNumber(String flightNumber);
}
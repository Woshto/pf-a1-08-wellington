package br.univali.es2.prettyflights.main.repository;

import br.univali.es2.prettyflights.main.domain.Gate;
import br.univali.es2.prettyflights.main.domain.GateStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GateRepository extends JpaRepository<Gate, Long> {

    List<Gate> findByStatus(GateStatus status);

    Gate findByCodigo(String codigo);

    @Query("SELECT g FROM Gate g WHERE g.status = 'DISPONIVEL' AND g.maxAeronaveCateg >= :category")
    List<Gate> findAvailableGatesForCategory(@Param("category") AircraftCategory category);
}
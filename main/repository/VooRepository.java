package br.univali.es2.prettyflights.main.repository;

import br.univali.es2.prettyflights.main.domain.Voo;
import br.univali.es2.prettyflights.main.domain.AircraftCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VooRepository extends JpaRepository<Voo, Long> {

    Optional<Voo> findByVooNum(String vooNum);

    List<Voo> findByAircraftCategory(AircraftCategory category);

    List<Voo> findByChegadaBetween(LocalDateTime start, LocalDateTime end);

    @Query("""
        SELECT v FROM Voo v
        WHERE v.chegada < :departure
          AND v.saida > :arrival
          AND v.id != :excludeId
    """)
    List<Voo> findConflictingFlights(
        @Param("arrival") LocalDateTime arrival,
        @Param("departure") LocalDateTime departure,
        @Param("excludeId") Long excludeId
    );
}
package br.univali.es2.prettyflights.main.repository;

import br.univali.es2.prettyflights.main.domain.AllocationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllocationLogRepository extends JpaRepository<AllocationLog, Long> {
    
    List<AllocationLog> findAllByAllocation_Voo_VooNumOrderByChangedAtAsc(String vooNum);
    
    List<AllocationLog> findAllByAllocation_Voo_VooNum(String vooNum);
}
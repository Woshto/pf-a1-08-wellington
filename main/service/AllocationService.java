package br.univali.es2.prettyflights.main.service;

import br.univali.es2.prettyflights.main.domain.*;
import br.univali.es2.prettyflights.main.repository.AllocationLogRepository;
import br.univali.es2.prettyflights.main.repository.GateAllocationRepository;
import br.univali.es2.prettyflights.main.repository.GateRepository;
import br.univali.es2.prettyflights.main.repository.VooRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AllocationService {

    @Autowired
    private GateRepository gateRepository;

    @Autowired
    private VooRepository vooRepository;

    @Autowired
    private GateAllocationRepository allocationRepository;

    @Autowired
    private AllocationLogRepository logRepository;

    //Aloca um voo automaticamente
    public GateAllocation allocateFlight(Long vooId, String userId) {
        Voo voo = vooRepository.findById(vooId)
                .orElseThrow(() -> new RuntimeException("Voo não encontrado"));

        Gate gate = findCompatibleGate(voo);

        if (gate == null) {
            throw new RuntimeException("Nenhum portão compatível disponível");
        }

        GateAllocation allocation = new GateAllocation(voo, gate, userId);
        allocation = allocationRepository.save(allocation);

        AllocationLog log = AllocationLog.createCreationLog(allocation, userId);
        logRepository.save(log);

        return allocation;
    }

    //Aloca todos os voos automaticamente
    @Transactional
    public List<GateAllocation> allocateAllFlights(String userId) {
        List<Voo> voos = vooRepository.findAll();
        List<GateAllocation> allocations = new ArrayList<>();

        for (Voo voo : voos) {
            try {
                GateAllocation allocation = allocateFlight(voo.getId(), userId);
                allocations.add(allocation);
            } catch (RuntimeException e) {
                System.err.println("Erro ao alocar voo " + voo.getVooNum() + ": " + e.getMessage());
            }
        }

        return allocations;
    }

    //Realoca um voo manualmente para outro portão
    @Transactional
    public GateAllocation manualReallocate(String vooNum, String gateCodigo, String userId, String motivo) {
        GateAllocation currentAllocation = allocationRepository.findAll().stream()
                .filter(a -> a.getVoo().getVooNum().equals(vooNum) && a.isActive())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Voo não encontrado ou não está alocado"));

        Gate newGate = gateRepository.findAll().stream()
                .filter(g -> g.getCodigo().equals(gateCodigo))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Portão não encontrado"));

        String previousState = "Alocado no portão: " + currentAllocation.getGate().getCodigo();

        currentAllocation.cancel();
        allocationRepository.save(currentAllocation);

        GateAllocation newAllocation = new GateAllocation(currentAllocation.getVoo(), newGate, userId);
        newAllocation = allocationRepository.save(newAllocation);

        AllocationLog log = AllocationLog.createReallocationLog(
                newAllocation,
                userId,
                motivo,
                previousState
        );
        logRepository.save(log);

        return newAllocation;
    }

    //Busca logs de um voo específico
    public List<AllocationLog> findLogsByFlight(String vooNum) {
        return logRepository.findAll().stream()
                .filter(log -> log.getAllocation().getVoo().getVooNum().equals(vooNum))
                .toList();
    }

    
    //Encontra um portão compatível disponível
    private Gate findCompatibleGate(Voo voo) {
        return gateRepository.findAll().stream()
                .filter(gate -> gate.getStatus() == GateStatus.DISPONIVEL)
                .filter(gate -> gate.canAccommodate(voo))
                .findFirst()
                .orElse(null);
    }

    public List<Gate> findAllGates() {
        return gateRepository.findAll();
    }

    public List<Voo> findAllFlights() {
        return vooRepository.findAll();
    }
}
package br.univali.es2.prettyflights.main.controller;

import br.univali.es2.prettyflights.main.domain.AllocationLog;
import br.univali.es2.prettyflights.main.domain.GateAllocation;
import br.univali.es2.prettyflights.main.service.AllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/allocation")
public class AllocationController {

    @Autowired
    private AllocationService allocationService;

    //Aloca um voo automaticamente
    @PostMapping("/run")
    public ResponseEntity<Map<String, Object>> allocateFlight(
            @RequestParam Long vooId,
            @RequestParam(defaultValue = "sistema") String userId) {

        GateAllocation allocation = allocationService.allocateFlight(vooId, userId);

        Map<String, Object> response = new HashMap<>();
        response.put("gateId", allocation.getGate().getId());
        response.put("gateCode", allocation.getGate().getCodigo());
        response.put("allocationId", allocation.getId());
        response.put("status", allocation.getStatus().name());

        return ResponseEntity.ok(response);
    }

    //Aloca todos os voos automaticamente
    @PostMapping("/run-all")
    public ResponseEntity<List<GateAllocation>> allocateAllFlights(
            @RequestParam(defaultValue = "sistema") String userId) {

        List<GateAllocation> allocations = allocationService.allocateAllFlights(userId);
        return ResponseEntity.ok(allocations);
    }

    //Realoca um voo manualmente
    @PostMapping("/reallocate")
    public ResponseEntity<Map<String, Object>> manualReallocate(
            @RequestParam String vooNum,
            @RequestParam String gateCodigo,
            @RequestParam(defaultValue = "operador") String userId,
            @RequestParam String motivo) {

        GateAllocation allocation = allocationService.manualReallocate(vooNum, gateCodigo, userId, motivo);

        Map<String, Object> response = new HashMap<>();
        response.put("vooNum", allocation.getVoo().getVooNum());
        response.put("newGate", allocation.getGate().getCodigo());
        response.put("status", allocation.getStatus().name());
        response.put("allocatedBy", allocation.getAllocatedBy());

        return ResponseEntity.ok(response);
    }

    //Busca logs de um voo
    @GetMapping("/logs")
    public ResponseEntity<List<AllocationLog>> getLogsByFlight(
            @RequestParam String vooNum) {

        List<AllocationLog> logs = allocationService.findLogsByFlight(vooNum);
        return ResponseEntity.ok(logs);
    }
}
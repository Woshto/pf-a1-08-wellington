package br.univali.es2.prettyflights.main.test;

import br.univali.es2.prettyflights.main.domain.*;
import br.univali.es2.prettyflights.main.repository.AllocationLogRepository;
import br.univali.es2.prettyflights.main.repository.GateAllocationRepository;
import br.univali.es2.prettyflights.main.repository.GateRepository;
import br.univali.es2.prettyflights.main.repository.VooRepository;
import br.univali.es2.prettyflights.main.service.AllocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Testes de Sistema / End-to-End (Topo do V)

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Teste de Sistema - Fluxo Completo de Alocação")
class AllocationSystemTest {

    @Autowired
    private GateRepository gateRepository;

    @Autowired
    private VooRepository vooRepository;

    @Autowired
    private GateAllocationRepository allocationRepository;

    @Autowired
    private AllocationLogRepository logRepository;

    @Autowired
    private AllocationService allocationService;

    @BeforeEach
    void setUp() {
        // Limpar os repositórios antes de cada teste
        logRepository.deleteAll();
        allocationRepository.deleteAll();
        vooRepository.deleteAll();
        gateRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("Fluxo completo: alocação, conflito e realocação manual")
    void shouldCompleteFullAllocationFlowWithConflictResolutionAndManualReallocation() {
        System.out.println("\n========== INICIANDO TESTE DE SISTEMA ==========\n");

        System.out.println("a. Cadastrando 10 portões...");

        Gate g1 = new Gate("G1", AircraftCategory.A, true, "Terminal 1");
        Gate g2 = new Gate("G2", AircraftCategory.A, true, "Terminal 1");
        Gate g3 = new Gate("G3", AircraftCategory.B, true, "Terminal 1");
        Gate g4 = new Gate("G4", AircraftCategory.B, true, "Terminal 1");
        Gate g5 = new Gate("G5", AircraftCategory.C, true, "Terminal 2");
        Gate g6 = new Gate("G6", AircraftCategory.C, true, "Terminal 2");
        Gate g7 = new Gate("G7", AircraftCategory.D, true, "Terminal 2"); 
        Gate g8 = new Gate("G8", AircraftCategory.D, true, "Terminal 2");
        Gate g9 = new Gate("G9", AircraftCategory.A, false, "Terminal 3");
        Gate g10 = new Gate("G10", AircraftCategory.B, false, "Terminal 3");

        gateRepository.save(g1);
        gateRepository.save(g2);
        gateRepository.save(g3);
        gateRepository.save(g4);
        gateRepository.save(g5);
        gateRepository.save(g6);
        gateRepository.save(g7);
        gateRepository.save(g8);
        gateRepository.save(g9);
        gateRepository.save(g10);

        assertEquals(10, gateRepository.count(), "Deve ter 10 portões cadastrados");
        System.out.println("10 portões cadastrados com sucesso");

        System.out.println("\nb. Importando 20 voos...");

        LocalDateTime baseDate = LocalDateTime.of(2024, 1, 1, 8, 0);

        Voo v1 = new Voo("AA100", AircraftCategory.A, baseDate.plusHours(0), baseDate.plusHours(2), 50);
        Voo v2 = new Voo("AA101", AircraftCategory.A, baseDate.plusHours(2), baseDate.plusHours(4), 60);
        Voo v3 = new Voo("AA102", AircraftCategory.A, baseDate.plusHours(4), baseDate.plusHours(6), 55);
        Voo v4 = new Voo("AA103", AircraftCategory.B, baseDate.plusHours(0), baseDate.plusHours(3), 120);
        Voo v5 = new Voo("AA104", AircraftCategory.B, baseDate.plusHours(3), baseDate.plusHours(6), 130);
        Voo v6 = new Voo("AA105", AircraftCategory.B, baseDate.plusHours(6), baseDate.plusHours(9), 110);
        Voo v7 = new Voo("AA106", AircraftCategory.C, baseDate.plusHours(0), baseDate.plusHours(4), 180);
        Voo v8 = new Voo("AA107", AircraftCategory.C, baseDate.plusHours(4), baseDate.plusHours(8), 190);
        Voo v9 = new Voo("AA108", AircraftCategory.C, baseDate.plusHours(8), baseDate.plusHours(12), 170);
        Voo v10 = new Voo("AA109", AircraftCategory.D, baseDate.plusHours(0), baseDate.plusHours(5), 250);
        Voo v11 = new Voo("AA110", AircraftCategory.D, baseDate.plusHours(5), baseDate.plusHours(10), 260);
        
        // VOOS CONFLITANTES: Mesmo horário, mesma categoria D (mais compatível com portão D)
        Voo v12 = new Voo("LA3041", AircraftCategory.D, baseDate.plusHours(2), baseDate.plusHours(7), 280);
        Voo v13 = new Voo("LA3042", AircraftCategory.D, baseDate.plusHours(2), baseDate.plusHours(7), 270);
        Voo v14 = new Voo("AA111", AircraftCategory.A, baseDate.plusHours(7), baseDate.plusHours(9), 45);
        Voo v15 = new Voo("AA112", AircraftCategory.B, baseDate.plusHours(9), baseDate.plusHours(12), 140);
        Voo v16 = new Voo("AA113", AircraftCategory.C, baseDate.plusHours(12), baseDate.plusHours(16), 200);
        Voo v17 = new Voo("AA114", AircraftCategory.D, baseDate.plusHours(10), baseDate.plusHours(15), 300);
        Voo v18 = new Voo("AA115", AircraftCategory.A, baseDate.plusHours(10), baseDate.plusHours(12), 40);
        Voo v19 = new Voo("AA116", AircraftCategory.B, baseDate.plusHours(12), baseDate.plusHours(15), 125);
        Voo v20 = new Voo("AA117", AircraftCategory.C, baseDate.plusHours(16), baseDate.plusHours(20), 210);

        vooRepository.save(v1);
        vooRepository.save(v2);
        vooRepository.save(v3);
        vooRepository.save(v4);
        vooRepository.save(v5);
        vooRepository.save(v6);
        vooRepository.save(v7);
        vooRepository.save(v8);
        vooRepository.save(v9);
        vooRepository.save(v10);
        vooRepository.save(v11);
        vooRepository.save(v12); 
        vooRepository.save(v13); 
        vooRepository.save(v14);
        vooRepository.save(v15);
        vooRepository.save(v16);
        vooRepository.save(v17);
        vooRepository.save(v18);
        vooRepository.save(v19);
        vooRepository.save(v20);

        assertEquals(20, vooRepository.count(), "Deve ter 20 voos cadastrados");
        System.out.println("20 voos importados com sucesso");
        System.out.println("Voos conflitantes: LA3041 e LA3042 (mesmo horário, categoria D)");


        //O sistema executa a alocação automática e resolve o conflito realocando o segundo voo para o próximo 
        //portão compatível disponível.

        System.out.println("\nc. Executando alocação automática...");

        List<GateAllocation> allocations = allocationService.allocateAllFlights("sistema");

        assertNotNull(allocations, "A alocação não deve ser nula");
        System.out.println("Alocação automática executada");

        // Verificar se os voos conflitantes foram alocados
        List<GateAllocation> la3041Allocations = allocationRepository.findAll().stream()
                .filter(a -> a.getVoo().getVooNum().equals("LA3041") && a.isActive())
                .toList();

        List<GateAllocation> la3042Allocations = allocationRepository.findAll().stream()
                .filter(a -> a.getVoo().getVooNum().equals("LA3042") && a.isActive())
                .toList();

        assertFalse(la3041Allocations.isEmpty(), "Voo LA3041 deve estar alocado");
        assertFalse(la3042Allocations.isEmpty(), "Voo LA3042 deve estar alocado");

        // Verificar que estão em portões diferentes (conflito resolvido)
        String gateLA3041 = la3041Allocations.get(0).getGate().getCodigo();
        String gateLA3042 = la3042Allocations.get(0).getGate().getCodigo();

        assertNotEquals(gateLA3041, gateLA3042, 
                "Voos conflitantes devem estar em portões diferentes");

        System.out.println("Conflito resolvido:");
        System.out.println(" - LA3041 alocado no portão: " + gateLA3041);
        System.out.println(" - LA3042 alocado no portão: " + gateLA3042);

        // d. Um operador realoca manualmente o voo LA3041 para o portão G7,registrando motivo "Solicitação da 
        // companhia aérea".
        System.out.println("\nd. Realocando manualmente o voo LA3041 para o portão G7...");

        GateAllocation reallocated = allocationService.manualReallocate(
                "LA3041",
                "G7",
                "operador_joao",
                "Solicitação da companhia aérea"
        );

        assertNotNull(reallocated, "A realocação deve ser bem-sucedida");
        assertEquals("G7", reallocated.getGate().getCodigo(), 
                "O voo deve ser realocado para o portão G7");
        assertEquals("operador_joao", reallocated.getAllocatedBy(),
                "O operador deve ser registrado");
        assertEquals(AllocationStatus.ACTIVE, reallocated.getStatus(),
                "A nova alocação deve estar ativa");

        System.out.println("Voo LA3041 realocado para o portão G7");
        System.out.println("Motivo: 'Solicitação da companhia aérea'");
        System.out.println("Operador: 'operador_joao'");

        // Verificar se o portão anterior foi liberado
        boolean oldGateAvailable = allocationRepository.findAll().stream()
                .filter(a -> a.getVoo().getVooNum().equals("LA3041"))
                .allMatch(a -> a.getStatus() == AllocationStatus.CANCELLED);

        assertTrue(oldGateAvailable, 
                "A alocação anterior do LA3041 deve estar cancelada");

        // e. Uma consulta ao log do voo LA3041 deve retornar o histórico completo com estado anterior, estado 
        // posterior, horário e ID do operador.
        System.out.println("\ne. Consultando logs do voo LA3041...");

        List<AllocationLog> logs = allocationService.findLogsByFlight("LA3041");

        // Deve ter pelo menos 2 logs: criação + realocação
        assertFalse(logs.isEmpty(), "Deve haver logs para o voo LA3041");
        assertTrue(logs.size() >= 2, 
                "Deve ter pelo menos 2 logs (criação e realocação)");

        System.out.println("Logs encontrados: " + logs.size());

        // Verificar o primeiro log (criação)
        AllocationLog creationLog = logs.get(0);
        assertEquals("sistema", creationLog.getChangedBy());
        assertEquals("Alocação automática inicial", creationLog.getChangeReason());
        assertNull(creationLog.getPreviousState());
        assertNotNull(creationLog.getNewState());
        assertNotNull(creationLog.getChangedAt());

        // Verificar o último log (realocação)
        AllocationLog reallocationLog = logs.get(logs.size() - 1);
        assertEquals("operador_joao", reallocationLog.getChangedBy(),
                "O operador deve ser 'operador_joao'");
        assertEquals("Solicitação da companhia aérea", reallocationLog.getChangeReason(),
                "O motivo deve ser 'Solicitação da companhia aérea'");

        // VERIFICAÇÃO CRÍTICA: estado anterior e posterior
        String previousState = reallocationLog.getPreviousState();
        String newState = reallocationLog.getNewState();

        assertNotNull(previousState, "Estado anterior deve estar registrado");
        assertNotNull(newState, "Estado posterior deve estar registrado");
        
        assertTrue(previousState.contains("Alocado no portão:"), 
                " Estado anterior deve conter o portão antigo");
        assertTrue(newState.contains("Realocado para o portão: G7"), 
                "Estado posterior deve conter o portão G7");

        System.out.println("Estado anterior: " + previousState);
        System.out.println("Estado posterior: " + newState);

        // Verificar horário
        assertNotNull(reallocationLog.getChangedAt(),
                "Horário deve estar registrado");
        System.out.println("Horário: " + reallocationLog.getChangedAt());

        // Verificar ID do operador
        assertEquals("operador_joao", reallocationLog.getChangedBy(),
                "ID do operador deve ser 'operador_joao'");
        System.out.println("Operador: " + reallocationLog.getChangedBy());

        // VERIFICAÇÃO FINAL
        System.out.println("\n========== TESTE DE SISTEMA CONCLUÍDO COM SUCESSO ==========");
        System.out.println("a. 10 portões cadastrados");
        System.out.println("b. 20 voos importados (LA3041 e LA3042 conflitantes)");
        System.out.println("c. Conflito resolvido automaticamente");
        System.out.println("d. LA3041 realocado manualmente para G7");
        System.out.println("e. Log completo com estado anterior, posterior, horário e operador");
    }
}
package br.univali.es2.prettyflights.main.test;

import br.univali.es2.prettyflights.main.domain.*;
import br.univali.es2.prettyflights.main.repository.AllocationLogRepository;
import br.univali.es2.prettyflights.main.repository.GateAllocationRepository;
import br.univali.es2.prettyflights.main.repository.GateRepository;
import br.univali.es2.prettyflights.main.repository.VooRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


//Testes de Integração - Meio do V

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("Teste de Integração - Alocação de Portões")
class AllocationIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GateRepository gateRepository;

    @Autowired
    private VooRepository vooRepository;

    @Autowired
    private GateAllocationRepository allocationRepository;

    @Autowired
    private AllocationLogRepository logRepository;

    private Gate gateC;
    private Voo vooC;

    @BeforeEach
    void setUp() {
        //Limpar os repositórios antes de cada teste
        logRepository.deleteAll();
        allocationRepository.deleteAll();
        gateRepository.deleteAll();
        vooRepository.deleteAll();

        //Cadastrar um portão como disponível e categoria "C"
        gateC = new Gate("G5", AircraftCategory.C, true, "Terminal 2");
        gateC.setStatus(GateStatus.DISPONIVEL);
        gateC = gateRepository.save(gateC);

        //Criar um voo de categoria C
        vooC = new Voo("LA3050", AircraftCategory.C,
                       LocalDateTime.now().plusHours(1),
                       LocalDateTime.now().plusHours(3),
                       180);
        vooC = vooRepository.save(vooC);
    }

    @Test
    @DisplayName("Deve alocar voo categoria C no portão categoria C e gerar log")
    void shouldAllocateFlightCToGateCAndGenerateLog() {
        //Acionar o endpoint /allocation/run com um voo de categoria C
        String url = "/allocation/run?vooId=" + vooC.getId() + "&userId=operador_teste";
        ResponseEntity<Map> response = restTemplate.postForEntity(url, null, Map.class);

        //Verificar se a resposta foi bem-sucedida
        assertTrue(response.getStatusCode().is2xxSuccessful(), 
            "O endpoint deve retornar status 2xx");

        //O sistema deve retornar na resposta o ID do portão correto
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody, "A resposta não deve ser nula");

        Long gateId = ((Number) responseBody.get("gateId")).longValue();
        assertNotNull(gateId, "O ID do portão deve estar na resposta");
        
        //Verificar se o portão retornado é o que foi cadastrado
        assertEquals(gateC.getId(), gateId, 
            "O portão retornado deve ser o portão categoria C cadastrado");

        //Verificar se o código do portão está correto
        assertEquals("G5", responseBody.get("gateCode"), 
            "O código do portão deve ser G5");

        //Verificar se a alocação foi salva no banco
        List<GateAllocation> allocations = allocationRepository.findAll();
        assertFalse(allocations.isEmpty(), 
            "A alocação deve ser salva no banco de dados");

        GateAllocation savedAllocation = allocations.get(0);
        assertEquals(vooC.getId(), savedAllocation.getVoo().getId(),
            "O voo salvo deve ser o mesmo que foi alocado");
        assertEquals(gateC.getId(), savedAllocation.getGate().getId(),
            "O portão salvo deve ser o mesmo que foi retornado");
        assertEquals(AllocationStatus.ACTIVE, savedAllocation.getStatus(),
            "O status da alocação deve ser ACTIVE");
        assertEquals("operador_teste", savedAllocation.getAllocatedBy(),
            "O userID deve ser 'operador_teste'");
        assertNotNull(savedAllocation.getAllocatedAt(),
            "O horário da alocação deve ser registrado");

        //Verificar se o log foi gerado na tabela com horário, userID e dados
        List<AllocationLog> logs = logRepository.findAll();
        assertFalse(logs.isEmpty(), 
            "O log deve ser gerado na tabela");

        AllocationLog log = logs.get(0);
        assertEquals(savedAllocation.getId(), log.getAllocation().getId(),
            "O log deve referenciar a alocação correta");
        assertEquals("operador_teste", log.getChangedBy(),
            "O userID deve ser 'operador_teste'");
        assertEquals("Alocação automática inicial", log.getChangeReason(),
            "O motivo deve ser 'Alocação automática inicial'");
        assertNull(log.getPreviousState(),
            "Estado anterior deve ser null na criação");
        assertEquals("Alocado no portão: G5", log.getNewState(),
            "Estado novo deve conter o portão correto");
        assertNotNull(log.getChangedAt(),
            "O horário deve ser registrado no log");

        //Verificação final - se chegou até aqui, o teste passou!
        System.out.println("Teste de integração passou com sucesso!");
        System.out.println("   - Portão G5 (Categoria C) alocado");
        System.out.println("   - Voo LA3050 (Categoria C) alocado");
        System.out.println("   - Log gerado com horário, userID e dados");
    }
}
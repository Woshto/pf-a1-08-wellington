package br.univali.es2.prettyflights.main.test;

import br.univali.es2.prettyflights.main.domain.AircraftCategory;
import br.univali.es2.prettyflights.main.domain.Gate;
import br.univali.es2.prettyflights.main.domain.Voo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

//Testes de Unidade - Base do V

@DisplayName("Testes de Unidade - Gate e Flight")
class GateTest {

    //Compatibilidade de Categorias

    @Test
    @DisplayName("Voo categoria D em portão categoria B deve retornar false")
    void shouldReturnFalseWhenFlightCategoryDInGateCategoryB() {
        Gate gate = new Gate("G1", AircraftCategory.B, true, "Terminal 1");
        
        Voo flight = new Voo("LA3041", AircraftCategory.D, 
                             LocalDateTime.now(), 
                             LocalDateTime.now().plusHours(2), 
                             200);
        
        boolean resultado = gate.canAccommodate(flight);
        
        assertFalse(resultado, 
            "Voo categoria D NÃO deve caber em portão categoria B");
    }

    //Conflito de Janela de Tempo

    @Test
    @DisplayName("Dois voos com janela de tempo sobreposta no mesmo portão devem conflitar")
    void shouldDetectConflictWhenTwoFlightsOverlapInSameGate() {
        Gate gate = new Gate("G1", AircraftCategory.B, true, "Terminal 1");
        
        Voo voo1 = new Voo("AA100", AircraftCategory.B,
                           LocalDateTime.of(2024, 1, 1, 10, 0),
                           LocalDateTime.of(2024, 1, 1, 12, 0),
                           150);
        
        Voo voo2 = new Voo("LA3041", AircraftCategory.B,
                           LocalDateTime.of(2024, 1, 1, 11, 0),
                           LocalDateTime.of(2024, 1, 1, 13, 0),
                           180);
        
        assertTrue(gate.canAccommodate(voo1), "Voo1 cabe no portão");
        assertTrue(gate.canAccommodate(voo2), "Voo2 cabe no portão");
        
        boolean temConflito = voo1.hasTimeConflictWith(voo2);
        
        assertTrue(temConflito, 
            "Voos com horários sobrepostos devem conflitar");
    }
}
package br.univali.es2.prettyflights.main.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_flight")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "voo_num", nullable = false, unique = true)
    private String vooNum;

    @Enumerated(EnumType.STRING)
    @Column(name = "aircraft_category", nullable = false)
    private AircraftCategory aircraftCategory;

    @Column(nullable = false)
    private LocalDateTime chegada;

    @Column(nullable = false)
    private LocalDateTime saida;

    @Column(name = "passageiro_count", nullable = false)
    private int passageiroCount;
    
    public Voo(String vooNum, AircraftCategory aircraftCategory,
               LocalDateTime chegada, LocalDateTime saida,
               int passageiroCount) {
        this.vooNum = vooNum;
        this.aircraftCategory = aircraftCategory;
        this.chegada = chegada;
        this.saida = saida;
        this.passageiroCount = passageiroCount;
    }

    public boolean hasTimeConflictWith(Voo outroVoo) {
        if (outroVoo == null) {
            return false;
        }
        return !(this.saida.isBefore(outroVoo.chegada) || 
                 outroVoo.saida.isBefore(this.chegada));
    }
}
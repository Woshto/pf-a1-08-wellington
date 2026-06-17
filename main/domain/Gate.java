package br.univali.es2.prettyflights.main.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_gate")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Gate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(name = "max_aeronave_categ", nullable = false)
    private AircraftCategory maxAeronaveCateg;

    @Column(name = "has_bridge", nullable = false)
    private boolean hasBridge;

    @Column(nullable = false)
    private String terminal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GateStatus status = GateStatus.DISPONIVEL;

    public Gate(String codigo, AircraftCategory maxAeronaveCateg,
                boolean hasBridge, String terminal) {
        this.codigo = codigo;
        this.maxAeronaveCateg = maxAeronaveCateg;
        this.hasBridge = hasBridge;
        this.terminal = terminal;
        this.status = GateStatus.DISPONIVEL;
    }

    public boolean canAccommodate(Voo voo) {
        if (voo == null) {
            return false;
        }
        return voo.getAircraftCategory().fitsIn(this.maxAeronaveCateg);
    }
}
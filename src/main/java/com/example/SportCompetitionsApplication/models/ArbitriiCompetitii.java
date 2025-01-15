/** clasa pentru tabelul Arbitrii Competitii
 * @author Bobeica Alexandru
 * @version 20 decembrie 2024
 */
package com.example.SportCompetitionsApplication.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ArbitriiCompetitii {
    @EmbeddedId
    private ArbitriiCompetitiiId id;

    @MapsId("arbitruID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ArbitruID", nullable = false)
    private Arbitrii arbitruID;

    @MapsId("competitieID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CompetitieID", nullable = false)
    private Competitii competitieID;

    @Column(name = "NrPartide")
    private Integer nrPartide;

    @Column(name = "Salariu")
    private Integer salariu;

}
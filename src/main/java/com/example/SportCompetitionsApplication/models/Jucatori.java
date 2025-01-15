/** clasa pentru tabelul Jucatori
 * @author Bobeica Alexandru
 * @version 20 decembrie 2024
 */
package com.example.SportCompetitionsApplication.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Entity
@Data
public class Jucatori {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JucatorID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EchipaID", nullable = false)
    private Echipe echipaID;

    @Nationalized
    @Column(name = "Nume", nullable = false, length = 50)
    private String nume;

    @Nationalized
    @Column(name = "Prenume", nullable = false, length = 50)
    private String prenume;

    @Column(name = "Legitimatie", nullable = false, length = 15)
    private String legitimatie;

    @Column(name = "DataNasterii")
    private Instant dataNasterii;

    @Column(name = "Sex", nullable = false)
    private Character sex;

    @Column(name = "Salariu")
    private Integer salariu;

}
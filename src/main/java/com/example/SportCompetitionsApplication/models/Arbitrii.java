package com.example.SportCompetitionsApplication.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
/** clasa pentru tabelul Arbitrii
 * @author Bobeica Alexandru
 * @version 20 decembrie 2024
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Arbitrii {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ArbitruID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "Nume", nullable = false, length = 50)
    private String nume;

    @Nationalized
    @Column(name = "Prenume", nullable = false, length = 50)
    private String prenume;

    @Column(name = "DataNasterii")
    private Instant dataNasterii;

    @Column(name = "Legitimatie", nullable = false, length = 15)
    private String legitimatie;

    @Column(name = "Sex")
    private Character sex;

    @Column(name = "SezoaneArbitrate")
    private Integer sezoaneArbitrate;
}
/** clasa pentru tabelul Staff
 * @author Bobeica Alexandru
 * @version 20 decembrie 2024
 */
package com.example.SportCompetitionsApplication.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Staff")
@Access(AccessType.FIELD)
@ToString
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StaffID", nullable = false)
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

    @Nationalized
    @Column(name = "Post", length = 50)
    private String post;

    @Column(name = "DataNasterii")
    private Instant dataNasterii;

    @Column(name = "DataAngajarii")
    private Instant dataAngajarii;

    @Column(name = "Sex")
    private Character sex;

    @Column(name = "Salariu")
    private Integer salariu;
}
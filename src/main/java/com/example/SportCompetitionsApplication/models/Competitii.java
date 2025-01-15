/** clasa pentru tabelul Competitii
 * @author Bobeica Alexandru
 * @version 20 decembrie 2024
 */
package com.example.SportCompetitionsApplication.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Competitii")
@ToString
@Data
@Getter
@Setter
@Builder
public class Competitii {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CompetitieID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "Nume", length = 50)
    private String nume;

    @Column(name = "DataIncepere")
    private Instant dataIncepere;

    @Column(name = "DataSfarsit")
    private Instant dataSfarsit;

    @Nationalized
    @Column(name = "Locatie", length = 50)
    private String locatie;

    @Column(name = "PrizePool", nullable = false)
    private Double prizePool;

    @Column(name = "Castigator")
    private String castigator;

    @Column(name = "CreatedBy")
    private Integer createdBy;

    @Column(name = "Incheiat")
    private Integer incheiat;

}
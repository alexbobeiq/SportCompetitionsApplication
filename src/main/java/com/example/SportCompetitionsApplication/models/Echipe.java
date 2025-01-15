/** clasa pentru tabelul Echipe
 * @author Bobeica Alexandru
 * @version 20 decembrie 2024
 */
package com.example.SportCompetitionsApplication.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Echipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EchipaID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "Nume", nullable = false, length = 50)
    private String nume;

    @Nationalized
    @Column(name = "Presedinte", length = 50)
    private String presedinte;

    @Column(name = "DataInfiintare")
    private OffsetDateTime dataInfiintare;

    @Nationalized
    @Column(name = "AdresaSediu", nullable = false, length = 50)
    private String adresaSediu;

    @Nationalized
    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Nationalized
    @Column(name = "WebSite", nullable = false, length = 50)
    private String webSite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ManagerID")
    private User managerID;

}
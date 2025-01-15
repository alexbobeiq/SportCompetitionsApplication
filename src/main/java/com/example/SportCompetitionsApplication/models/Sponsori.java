/** clasa pentru tabelul Sponsori
 * @author Bobeica Alexandru
 * @version 20 decembrie 2024
 */
package com.example.SportCompetitionsApplication.models;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Entity
public class Sponsori {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SponsorID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EchipaID", nullable = false)
    private Echipe echipaID;

    @Nationalized
    @Column(name = "Nume", nullable = false, length = 50)
    private String nume;

    @Column(name = "ValoareContract")
    private Integer valoareContract;

    @Column(name = "DataIncepereContract")
    private Instant dataIncepereContract;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Echipe getEchipaID() {
        return echipaID;
    }

    public void setEchipaID(Echipe echipaID) {
        this.echipaID = echipaID;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Integer getValoareContract() {
        return valoareContract;
    }

    public void setValoareContract(Integer valoareContract) {
        this.valoareContract = valoareContract;
    }

    public Instant getDataIncepereContract() {
        return dataIncepereContract;
    }

    public void setDataIncepereContract(Instant dataIncepereContract) {
        this.dataIncepereContract = dataIncepereContract;
    }

}
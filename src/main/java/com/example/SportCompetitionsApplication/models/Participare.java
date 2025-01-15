package com.example.SportCompetitionsApplication.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
/** clasa pentru tabelul Participare
 * @author Bobeica Alexandru
 * @version 20 decembrie 2024
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Participare {
    @EmbeddedId
    private ParticipareId id;

    @MapsId("competitieID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CompetitieID", nullable = false)
    private Competitii competitieID;

    @MapsId("echipaID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EchipaID", nullable = false)
    private Echipe echipaID;

    @Column(name = "NrVictorii")
    private Integer nrVictorii;

    @Column(name = "NrEgaluri")
    private Integer nrEgaluri;

    @Column(name = "NrInfrangeri")
    private Integer nrInfrangeri;

    public ParticipareId getId() {
        return id;
    }

    public void setId(ParticipareId id) {
        this.id = id;
    }

    public Competitii getCompetitieID() {
        return competitieID;
    }

    public void setCompetitieID(Competitii competitieID) {
        this.competitieID = competitieID;
    }

    public Echipe getEchipaID() {
        return echipaID;
    }

    public void setEchipaID(Echipe echipaID) {
        this.echipaID = echipaID;
    }

    public Integer getNrVictorii() {
        return nrVictorii;
    }

    public void setNrVictorii(Integer nrVictorii) {
        this.nrVictorii = nrVictorii;
    }

    public Integer getNrEgaluri() {
        return nrEgaluri;
    }

    public void setNrEgaluri(Integer nrEgaluri) {
        this.nrEgaluri = nrEgaluri;
    }

    public Integer getNrInfrangeri() {
        return nrInfrangeri;
    }

    public void setNrInfrangeri(Integer nrInfrangeri) {
        this.nrInfrangeri = nrInfrangeri;
    }

}
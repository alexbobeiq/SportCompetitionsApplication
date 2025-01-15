package com.example.SportCompetitionsApplication.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.util.Objects;

@NoArgsConstructor
@Embeddable
public class ParticipareId implements java.io.Serializable {
    private static final long serialVersionUID = 4069400176054146203L;
    @Column(name = "CompetitieID", nullable = false)
    private Integer competitieID;

    @Column(name = "EchipaID", nullable = false)
    private Integer echipaID;

    public Integer getCompetitieID() {
        return competitieID;
    }

    public void setCompetitieID(Integer competitieID) {
        this.competitieID = competitieID;
    }

    public Integer getEchipaID() {
        return echipaID;
    }

    public void setEchipaID(Integer echipaID) {
        this.echipaID = echipaID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ParticipareId entity = (ParticipareId) o;
        return Objects.equals(this.echipaID, entity.echipaID) &&
                Objects.equals(this.competitieID, entity.competitieID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(echipaID, competitieID);
    }

}
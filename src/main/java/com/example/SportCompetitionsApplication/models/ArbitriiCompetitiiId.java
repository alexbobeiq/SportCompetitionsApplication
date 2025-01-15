package com.example.SportCompetitionsApplication.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class ArbitriiCompetitiiId implements java.io.Serializable {
    private static final long serialVersionUID = -788911004776463178L;
    @Column(name = "ArbitruID", nullable = false)
    private Integer arbitruID;

    @Column(name = "CompetitieID", nullable = false)
    private Integer competitieID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ArbitriiCompetitiiId entity = (ArbitriiCompetitiiId) o;
        return Objects.equals(this.arbitruID, entity.arbitruID) &&
                Objects.equals(this.competitieID, entity.competitieID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arbitruID, competitieID);
    }

}
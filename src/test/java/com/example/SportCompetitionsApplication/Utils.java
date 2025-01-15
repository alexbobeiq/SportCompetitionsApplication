package com.example.SportCompetitionsApplication;

import com.example.SportCompetitionsApplication.models.Competitii;
import com.example.SportCompetitionsApplication.models.Echipe;
import com.example.SportCompetitionsApplication.models.Participare;
import com.example.SportCompetitionsApplication.models.ParticipareId;

import java.time.Instant;

public class Utils {
    public static Competitii getCompetion() {
        return Competitii.builder()
                .id(2)
                .castigator("das")
                .nume("dsas")
                .createdBy(2)
                .dataIncepere(Instant.now())
                .dataSfarsit(Instant.now())
                .incheiat(3)
                .locatie("sds")
                .prizePool(200.2)
                .build();
    }

    public static Participare getParticipare() {
        return Participare.builder()
                .competitieID(getCompetion())
                .echipaID(new Echipe())
                .nrInfrangeri(1)
                .nrVictorii(1)
                .nrEgaluri(1)
                .id(new ParticipareId())
                .build();
    }
}

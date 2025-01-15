package com.example.SportCompetitionsApplication;

import com.example.SportCompetitionsApplication.models.Competitii;
import com.example.SportCompetitionsApplication.repository.CompetitiiRepository;
import com.example.SportCompetitionsApplication.repository.ParticipareRepository;
import com.example.SportCompetitionsApplication.services.ParticipareService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class AddCompetitionTest {

    @Mock
    ParticipareRepository participareRepository;
    @Mock
    CompetitiiRepository competitiiRepository;
    @InjectMocks
    ParticipareService participareService;

    @Test
    void overlappingTest() {
        Integer teamId = 1;
        Competitii competitie = Utils.getCompetion();
        given(participareRepository.findOverlappingParticipations(teamId, competitie.getDataIncepere(), competitie.getDataSfarsit())).willReturn(List.of(Utils.getParticipare()));
        try {
            participareService.addCompetition(competitie, List.of(teamId));
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof IllegalArgumentException);
            Assertions.assertEquals(e.getMessage(), "Team ID " + teamId + " is already participating in an overlapping competition.");
        }
    }

    @Test
    void duplicateCompetitionNameTest() {
        Integer teamId = 1;
        Competitii competitie = Utils.getCompetion();
        // Mock repository to simulate duplicate competition name
        given(participareRepository.findOverlappingParticipations(teamId, competitie.getDataIncepere(), competitie.getDataSfarsit()))
                .willReturn(List.of());
        given(competitiiRepository.existsByNume(competitie.getNume()))
                .willReturn(true);

        try {
            participareService.addCompetition(competitie, List.of(teamId));
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof IllegalArgumentException);
            Assertions.assertEquals("A competition with the name '" + competitie.getNume() + "' already exists.", e.getMessage());
        }
    }

    @Test
    void noTeamsTest() {
        Competitii competitie = Utils.getCompetion();

        try {
            participareService.addCompetition(competitie, List.of());
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof IllegalArgumentException);
            Assertions.assertEquals("At least one team must participate in the competition.", e.getMessage());
        }
    }

    @Test
    void startingDateIsInThePastTest() {
        //Given
        Integer teamId = 1;
        Competitii competitie = Utils.getCompetion();
        competitie.setDataIncepere(Instant.now().minusSeconds(100000));
        given(participareRepository.findOverlappingParticipations(teamId, competitie.getDataIncepere(), competitie.getDataSfarsit())).willReturn(List.of());
        //When-Then
        try {
            participareService.addCompetition(competitie, List.of(teamId));
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof IllegalArgumentException);
            Assertions.assertEquals("The starting date of the competition must be today or in the future.", e.getMessage());
        }
    }

    @Test
    void endingDateBeforeStartingDateTest() {
        Integer teamId = 1;
        Competitii competitie = Utils.getCompetion();
        competitie.setDataSfarsit(Instant.now().minusSeconds(100000));
        competitie.setDataIncepere(Instant.now().plusSeconds(1000000));
        given(participareRepository.findOverlappingParticipations(teamId, competitie.getDataIncepere(), competitie.getDataSfarsit())).willReturn(List.of());
        try {
            participareService.addCompetition(competitie, List.of(teamId));
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof IllegalArgumentException);
            Assertions.assertEquals("The ending date cannot be before the starting date.", e.getMessage());
        }
    }

    @Test
    void whenValidCompetitions() {
        Integer teamId = 1;
        Competitii competitie = Utils.getCompetion();
        competitie.setDataIncepere(Instant.now().plus(40, java.time.temporal.ChronoUnit.DAYS));
        competitie.setDataSfarsit(Instant.now().plus(60, java.time.temporal.ChronoUnit.DAYS));
        given(participareRepository.findOverlappingParticipations(teamId, competitie.getDataIncepere(), competitie.getDataSfarsit()))
                .willReturn(List.of());
        given(competitiiRepository.existsByNume(competitie.getNume()))
                .willReturn(false);

        Assertions.assertDoesNotThrow(() -> participareService.addCompetition(competitie, List.of(teamId)));
    }


}

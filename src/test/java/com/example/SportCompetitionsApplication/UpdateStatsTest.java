package com.example.SportCompetitionsApplication;

import com.example.SportCompetitionsApplication.models.Competitii;
import com.example.SportCompetitionsApplication.repository.CompetitiiRepository;
import com.example.SportCompetitionsApplication.repository.ParticipareRepository;
import com.example.SportCompetitionsApplication.services.LoggedInUser;
import com.example.SportCompetitionsApplication.services.ParticipareService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class UpdateStatsTest {
    @Mock
    private CompetitiiRepository competitiiRepository;

    @Mock
    private ParticipareRepository participareRepository;

    @Mock
    private LoggedInUser loggedInUser;

    @InjectMocks
    private ParticipareService participareService;

    @Test
    void whenInputsAreValidThenSuccess() {
        Integer competitionId = 1;
        Integer teamId = 2;
        Integer nrVictorii = 3;
        Integer nrEgaluri = 2;
        Integer nrInfrangeri = 1;
        Integer maxMatches = 10;

        Competitii competition = Utils.getCompetion();
        competition.setCreatedBy(1);

        given(competitiiRepository.findById(competitionId)).willReturn(Optional.of(competition));
        given(loggedInUser.getUserId()).willReturn(1);

        Assertions.assertDoesNotThrow(() -> participareService.updateTeamStats(competitionId, teamId, nrVictorii, nrEgaluri, nrInfrangeri, maxMatches));

        verify(participareRepository).updateStats(competitionId, teamId, nrVictorii, nrEgaluri, nrInfrangeri);
    }

    @Test
    void whenUserIsNotCreatorThenThrowError() {
        Integer competitionId = 1;
        Integer teamId = 2;
        Integer nrVictorii = 3;
        Integer nrEgaluri = 2;
        Integer nrInfrangeri = 1;
        Integer maxMatches = 10;

        Competitii competition = Utils.getCompetion();
        competition.setCreatedBy(1);

        given(competitiiRepository.findById(competitionId)).willReturn(Optional.of(competition));
        given(loggedInUser.getUserId()).willReturn(2);

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            participareService.updateTeamStats(competitionId, teamId, nrVictorii, nrEgaluri, nrInfrangeri, maxMatches);
        });

        Assertions.assertEquals("You can only edit competitions you created.", exception.getMessage());
        verify(participareRepository, never()).updateStats(any(), any(), any(), any(), any());
    }

    @Test
    void whenTotalMatchesExceedMaxThenThrowError() {
        Integer competitionId = 1;
        Integer teamId = 2;
        Integer nrVictorii = 5;
        Integer nrEgaluri = 4;
        Integer nrInfrangeri = 3;
        Integer maxMatches = 10;

        Competitii competition = Utils.getCompetion();
        competition.setCreatedBy(1);

        given(competitiiRepository.findById(competitionId)).willReturn(Optional.of(competition));
        given(loggedInUser.getUserId()).willReturn(1);

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            participareService.updateTeamStats(competitionId, teamId, nrVictorii, nrEgaluri, nrInfrangeri, maxMatches);
        });

        Assertions.assertEquals("The sum of victories, draws, and defeats exceeds the maximum number of matches.", exception.getMessage());
        verify(participareRepository, never()).updateStats(any(), any(), any(), any(), any());
    }

    @Test
    void whenCompetitionNotFoundThenThrowError() {
        Integer competitionId = 1;
        Integer teamId = 2;
        Integer nrVictorii = 3;
        Integer nrEgaluri = 2;
        Integer nrInfrangeri = 1;
        Integer maxMatches = 10;

        given(competitiiRepository.findById(competitionId)).willReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NoSuchElementException.class, () -> {
            participareService.updateTeamStats(competitionId, teamId, nrVictorii, nrEgaluri, nrInfrangeri, maxMatches);
        });

        Assertions.assertEquals("No value present", exception.getMessage());
        verify(participareRepository, never()).updateStats(any(), any(), any(), any(), any());
    }



}

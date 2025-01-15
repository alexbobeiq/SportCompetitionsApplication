package com.example.SportCompetitionsApplication;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class DeleteCompetitionTest {
    @Mock
    private CompetitiiRepository competitiiRepository;

    @Mock
    private ParticipareRepository participareRepository;

    @Mock
    private LoggedInUser loggedInUser;

    @InjectMocks
    private ParticipareService participareService;

    @Test
    void whenUserNotCreatorThenThrowError() {
        Integer competitionId = 1;
        Integer createdBy = 2;

        // Mock repository behavior
        doNothing().when(participareRepository).deleteByCompetitieID(competitionId);
        given(competitiiRepository.deleteCompetitionByUser(competitionId, createdBy)).willReturn(0);

        // Execute method and expect exception
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            participareService.deleteCompetition(competitionId, createdBy);
        });

        // Assert exception message
        Assertions.assertEquals("You can only delete competitions you created.", exception.getMessage());

        // Verify methods are called
        verify(participareRepository).deleteByCompetitieID(competitionId);
        verify(competitiiRepository).deleteCompetitionByUser(competitionId, createdBy);
    }

    @Test
    void whenParticipationsDeletedThenSuccess() {
        Integer competitionId = 1;
        Integer createdBy = 2;

        // Mock repository behavior
        doNothing().when(participareRepository).deleteByCompetitieID(competitionId);
        given(competitiiRepository.deleteCompetitionByUser(competitionId, createdBy)).willReturn(1);

        // Execute method
        Assertions.assertDoesNotThrow(() -> participareService.deleteCompetition(competitionId, createdBy));

        // Verify methods are called
        verify(participareRepository).deleteByCompetitieID(competitionId);
        verify(competitiiRepository).deleteCompetitionByUser(competitionId, createdBy);
    }



}

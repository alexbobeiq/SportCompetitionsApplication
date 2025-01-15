/** Clasa pentru manipularea competitiilor
 * @author Bobeica Alexandru
 * @version 10 Ianuarie 2024
 */
package com.example.SportCompetitionsApplication.services;

import com.example.SportCompetitionsApplication.models.Arbitrii;
import com.example.SportCompetitionsApplication.models.Competitii;
import com.example.SportCompetitionsApplication.models.Participare;
import com.example.SportCompetitionsApplication.repository.ArbitriiCompetitiiRepository;
import com.example.SportCompetitionsApplication.repository.ArbitriiRepository;
import com.example.SportCompetitionsApplication.repository.ParticipareRepository;
import com.example.SportCompetitionsApplication.repository.CompetitiiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
@Service
public class ParticipareService {

    @Autowired
    private LoggedInUser loggedInUser;

    @Autowired
    private CompetitiiRepository competitiiRepository;

    @Autowired
    private ParticipareRepository participareRepository;

    @Autowired
    private ArbitriiCompetitiiRepository arbitriiCompetitiiRepository;

    @Autowired
    private ArbitriiRepository arbitriiRepository;


    public void addCompetition(Competitii competition, List<Integer> teamIds) {
        // validarea unei noi competitii
        for (Integer teamId : teamIds) {
            List<Participare> overlappingParticipations =
                    participareRepository.findOverlappingParticipations(
                            teamId, competition.getDataIncepere(), competition.getDataSfarsit());

            if (!overlappingParticipations.isEmpty()) {
                throw new IllegalArgumentException("Team ID " + teamId + " is already participating in an overlapping competition.");
            }else {
                if (competitiiRepository.existsByNume(competition.getNume())) {
                    throw new IllegalArgumentException("A competition with the name '" + competition.getNume() + "' already exists.");
                }
            }

            if (competitiiRepository.existsByNume(competition.getNume())) {
                throw new IllegalArgumentException("A competition with the name '" + competition.getNume() + "' already exists.");
            }
        }

        // Validarea datelor de sfasit si inceput
        if (competition.getDataIncepere() == null || competition.getDataSfarsit() == null) {
            throw new IllegalArgumentException("Start date and end date must be provided.");
        }

        if (competition.getDataIncepere().isBefore(Instant.now())) {
            throw new IllegalArgumentException("The starting date of the competition must be today or in the future.");
        }

        if (competition.getDataSfarsit().isBefore(competition.getDataIncepere())) {
            throw new IllegalArgumentException("The ending date cannot be before the starting date.");
        }

        competitiiRepository.save(competition);

        for(Integer teamId : teamIds)
            participareRepository.saveParticipare(competition.getId(), teamId);
    }

    @Transactional
    public void deleteCompetition(Integer competitionId, Integer createdBy) {
        // Stergerea participarilor
        participareRepository.deleteByCompetitieID(competitionId);

        // Stergerea competitiei
        int rowsAffected = competitiiRepository.deleteCompetitionByUser(competitionId, createdBy);
        if (rowsAffected == 0) {
            throw new IllegalArgumentException("You can only delete competitions you created.");
        }
    }

    // Calcularea Castigatorului unei competirii
    @Transactional
    public void updateCompetitionWinner(Integer competitionId) {

        List<Participare> participations = participareRepository.findParticipationsByCompetitionId(competitionId);

        Participare winner = participations.stream()
                .max(Comparator.comparingInt(p ->
                        (p.getNrVictorii() != null ? p.getNrVictorii() : 0) * 3
                                + (p.getNrEgaluri() != null ? p.getNrEgaluri() : 0)))
                .orElse(null);

        if (winner != null) {
            competitiiRepository.updateWinner(competitionId, winner.getEchipaID().getNume());
        }
    }
    // Functie pentru editarea statisticilor pentru fiecare echipa dintr-o competitie
    public void updateTeamStats(Integer competitionId, Integer teamId, Integer nrVictorii, Integer nrEgaluri, Integer nrInfrangeri, Integer maxMatches) {

        Competitii competition = competitiiRepository.findById(competitionId).orElseThrow();
        if (nrVictorii < 0 || nrEgaluri < 0 || nrInfrangeri < 0) {
            throw new IllegalArgumentException("Values must be non-negative integers.");
        }
        System.out.println("prima chestie: " + competition.getCreatedBy() + " cealata:" + loggedInUser.getUserId());
        if (!competition.getCreatedBy().equals(loggedInUser.getUserId())) {
            throw new IllegalArgumentException("You can only edit competitions you created.");
        }


        int totalMatches = nrVictorii + nrEgaluri + nrInfrangeri;
        if (totalMatches > maxMatches) {
            throw new IllegalArgumentException("The sum of victories, draws, and defeats exceeds the maximum number of matches.");
        }

        updateCompetitionWinner(competitionId);
        participareRepository.updateStats(competitionId, teamId, nrVictorii, nrEgaluri, nrInfrangeri);
    }

    public List<Participare> getParticipationsByCompetitionId(Integer competitionId) {
        List<Participare> participations = participareRepository.findParticipationsByCompetitionId(competitionId);

        participations.sort(Comparator.comparingInt((Participare p) ->
                        (p.getNrVictorii() != null ? p.getNrVictorii() : 0) * 3
                                + (p.getNrEgaluri() != null ? p.getNrEgaluri() : 0))
                .thenComparingInt(p -> p.getNrVictorii() != null ? p.getNrVictorii() : 0)
                .reversed()
        );

        return participations;
    }


    public void endCompetition(Integer competitionId) {
        competitiiRepository.endCompetition(competitionId);
    }

    public List<Arbitrii> getAllReferees() {
        return arbitriiRepository.findAllReferees();
    }
    @Transactional
    public void assignRefereesToCompetition(Integer competitionId, List<Integer> refereeIds) {
        for (Integer refereeId : refereeIds) {
            arbitriiCompetitiiRepository.addArbitruToCompetition(refereeId, competitionId, 0, 0);
        }
    }

    public List<Arbitrii> getRefereesForCompetition(Integer competitionId) {
        return arbitriiCompetitiiRepository.findRefereesByCompetitionId(competitionId);
    }
}

/** clasa pentru afisarea statisticilor pentru echipe
 * @author Bobeica Alexandru
 * @version 11 Ianuarie 2024
 */
package com.example.SportCompetitionsApplication.services;

import com.example.SportCompetitionsApplication.models.Participare;
import com.example.SportCompetitionsApplication.repository.CompetitiiRepository;
import com.example.SportCompetitionsApplication.repository.ParticipareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {

    @Autowired
    private CompetitiiRepository competitiiRepository;

    @Autowired
    private ParticipareService participareService;
    @Autowired
    private ParticipareRepository participareRepository;

    public Map<String, Object> getTeamStatistics(Integer managerId, Integer teamId) {
        Map<String, Object> statistics = new HashMap<>();

        // Number of ongoing competitions
        long ongoingCompetitions = competitiiRepository.countOngoingCompetitionsByManager(managerId);
        statistics.put("ongoingCompetitions", ongoingCompetitions);

        // Match statistics
        List<Object[]> matchStats = competitiiRepository.getMatchStatsByTeam(teamId);
        long totalMatches = 0;
        long totalWins = 0;
        long totalEquals = 0;
        long totalLosses = 0;
        long totalCompetition = 0;

        if (!matchStats.isEmpty()) {
            Object[] stats = matchStats.get(0);
            totalCompetition = stats[0] != null ? ((Number) stats[0]).longValue() : 0;
            totalWins = stats[1] != null ? ((Number) stats[1]).longValue() : 0;
            totalEquals = stats[2] != null ? ((Number) stats[2]).longValue() : 0;
            totalLosses = stats[3] != null ? ((Number) stats[3]).longValue() : 0;
            totalMatches = totalWins + totalEquals + totalLosses;
        }

        // Calculate percentages
        if (totalMatches > 0) {
            statistics.put("winningPercentage", (double) totalWins / totalMatches * 100);
            statistics.put("equalPercentage", (double) totalEquals / totalMatches * 100);
            statistics.put("lossPercentage", (double) totalLosses / totalMatches * 100);
            statistics.put("pointsPerCompetition", (totalWins * 3 + totalEquals) / (double) totalCompetition);
        } else {
            statistics.put("winningPercentage", 0.0);
            statistics.put("equalPercentage", 0.0);
            statistics.put("lossPercentage", 0.0);
            statistics.put("pointsPerCompetition", 0.0);
        }

        // Calculate average ranking
        List<Integer> competitionIds = participareRepository.findCompetitionsByTeamId(teamId);
        List<Integer> rankings = new ArrayList<>();

        for (Integer competitionId : competitionIds) {
            List<Participare> participations = participareService.getParticipationsByCompetitionId(competitionId);

            // Find the ranking for the team in this competition
            for (int i = 0; i < participations.size(); i++) {
                if (participations.get(i).getEchipaID().getId().equals(teamId)) {
                    rankings.add(i + 1); // Ranking is 1-based
                    break;
                }
            }
        }

        double averageRanking = rankings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        statistics.put("averageRanking", averageRanking);

        return statistics;
    }
}


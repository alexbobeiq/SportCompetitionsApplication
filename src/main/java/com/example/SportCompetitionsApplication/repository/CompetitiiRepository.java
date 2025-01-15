/** interfata pentru query pentru tabelul Competitii
 * @author Bobeica Alexandru
 * @version 5 Decembrie 2024
 */
package com.example.SportCompetitionsApplication.repository;

import com.example.SportCompetitionsApplication.models.Competitii;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CompetitiiRepository extends CrudRepository<Competitii, Integer> {

        @Query("SELECT COUNT(c) > 0 FROM Competitii c WHERE c.nume = :nume")
        boolean existsByNume(@Param("nume") String nume);
        //Returs a list with all competitions
        @Query(value = "select * from Competitii", nativeQuery = true)
        Iterable<Competitii> aduTotiCompetitii();

        //Returns a list with all competitions created by a specific user
        @Query("SELECT c FROM Competitii c WHERE c.createdBy = :createdBy")
        List<Competitii> findCompetitiiByUserId(@Param("createdBy") String createdBy);

        @Modifying
        @Transactional
        @Query("DELETE FROM Competitii c WHERE c.id = :competitionId AND c.createdBy = :createdBy")
        int deleteCompetitionByUser(@Param("competitionId") Integer competitionId, @Param("createdBy") Integer createdBy);

        @Modifying
        @Transactional
        @Query("UPDATE Competitii c SET c.castigator = :winnerName WHERE c.id = :competitionId")
        void updateWinner(@Param("competitionId") Integer competitionId, @Param("winnerName") String winnerName);

        boolean existsByIdAndCreatedBy(Integer id, Integer createdBy);

        @Modifying
        @Transactional
        @Query("UPDATE Competitii c SET c.incheiat = 1 WHERE c.id = :competitionId")
        void endCompetition(@Param("competitionId") Integer competitionId);

        List<Competitii> findByIncheiat(int incheiat);

        @Query("SELECT c FROM Competitii c WHERE COALESCE(c.incheiat, 0) = 0")
        List<Competitii> findOngoingCompetitions(); // Ongoing (Incheiat = 0 or NULL)

        @Query("SELECT c FROM Competitii c WHERE COALESCE(c.incheiat, 0) = 1")
        List<Competitii> findEndedCompetitions(); // Ended (Incheiat = 1)

        ///!!!!!!
        @Query(value = """
            SELECT COUNT(DISTINCT c.CompetitieID)
            FROM Competitii c
            JOIN Participare p ON p.CompetitieID = c.CompetitieID
            JOIN Echipe e ON e.EchipaID = p.EchipaID
            WHERE e.ManagerID = :managerId AND (c.Incheiat = 0 OR c.Incheiat IS NULL)
        """, nativeQuery = true)
        long countOngoingCompetitionsByManager(@Param("managerId") Integer managerId);

        //!!!!!
        @Query(value = """
            SELECT c.CompetitieID, c.nume AS competition_name, COUNT(j.JucatorID) AS total_players
            FROM Competitii c
            JOIN Participare p ON c.CompetitieID = p.CompetitieID
            JOIN Jucatori j ON p.EchipaID = j.EchipaID
            GROUP BY c.CompetitieID, c.nume
""", nativeQuery = true)
        List<Object[]> findCompetitionsWithTotalPlayers();

        @Query("""
            SELECT COUNT(p) AS totalMatches,
                   SUM(p.nrVictorii) AS totalWins,
                   SUM(p.nrEgaluri) AS totalEquals,
                   SUM(p.nrInfrangeri) AS totalLosses
            FROM Participare p
            WHERE p.echipaID.id = :teamId
""")
        List<Object[]> getMatchStatsByTeam(@Param("teamId") Integer teamId);



}

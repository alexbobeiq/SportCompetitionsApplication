/** interfata pentru query pentru tabelul Participare
 * @author Bobeica Alexandru
 * @version 5 Ianuarie 2024
 */
package com.example.SportCompetitionsApplication.repository;

import com.example.SportCompetitionsApplication.models.Participare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Repository
public interface ParticipareRepository extends JpaRepository<Participare, Integer> {

    // Check if a team is already participating in an overlapping competition
    @Query("""
    SELECT p
    FROM Participare p
    JOIN p.competitieID c
    WHERE p.echipaID.id = :teamId
    AND (
        (:startDate BETWEEN c.dataIncepere AND c.dataSfarsit) OR
        (:endDate BETWEEN c.dataIncepere AND c.dataSfarsit) OR
        (c.dataIncepere BETWEEN :startDate AND :endDate) OR
        (c.dataSfarsit BETWEEN :startDate AND :endDate)
    )
""")
    List<Participare> findOverlappingParticipations(
            @Param("teamId") Integer teamId,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate);

    @Transactional
    @Modifying
    @Query(value = """
        INSERT INTO Participare (CompetitieID, EchipaID, NrVictorii, NrEgaluri, NrInfrangeri) 
        VALUES (:competitionID, :teamID, 0, 0, 0)
        """, nativeQuery = true)
    void saveParticipare(@Param("competitionID") Integer competitionID, @Param("teamID") Integer teamID);

    @Modifying
    @Transactional
    @Query("DELETE FROM Participare p WHERE p.competitieID.id = :competitionId")
    void deleteByCompetitieID(@Param("competitionId") Integer competitionId);

    @Query(value = """
    SELECT p.*
    FROM Participare p
    JOIN Echipe e ON p.EchipaID = e.EchipaID
    WHERE p.CompetitieID = :competitionId
""", nativeQuery = true)
    List<Participare> findParticipationsByCompetitionId(@Param("competitionId") Integer competitionId);

    @Modifying
    @Transactional
    @Query("""
        UPDATE Participare p
        SET p.nrVictorii = :nrVictorii,
            p.nrEgaluri = :nrEgaluri,
            p.nrInfrangeri = :nrInfrangeri
        WHERE p.id.competitieID = :competitionId AND p.id.echipaID = :teamId
    """)
    void updateStats(@Param("competitionId") Integer competitionId,
                     @Param("teamId") Integer teamId,
                     @Param("nrVictorii") Integer nrVictorii,
                     @Param("nrEgaluri") Integer nrEgaluri,
                     @Param("nrInfrangeri") Integer nrInfrangeri);

    ///!!!!!!11
    @Query(value = """
        SELECT DISTINCT c.CompetitieID
        FROM Competitii c
        JOIN Participare p ON c.CompetitieID = p.CompetitieID
        JOIN Echipe e ON p.EchipaID = e.EchipaID
        WHERE e.EchipaID = :teamId
        """, nativeQuery = true)
    List<Integer> findCompetitionsByTeamId(@Param("teamId") Integer teamId);

}

/** interfata pentru query pentru tabelul Jucatori
 * @author Bobeica Alexandru
 * @version 5 Ianuarie 2024
 */
package com.example.SportCompetitionsApplication.repository;

import com.example.SportCompetitionsApplication.models.Jucatori;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface JucatoriRepository extends JpaRepository<Jucatori, Integer> {
    @Query("SELECT j FROM Jucatori j WHERE j.echipaID.id = :echipaId")
    List<Jucatori> findByEchipaId(@Param("echipaId") Integer echipaId);

    @Query("SELECT COUNT(j) > 0 FROM Jucatori j WHERE j.legitimatie = :legitimatie AND (:playerId IS NULL OR j.id != :playerId)")
    boolean existsByLegitimatie(@Param("legitimatie") String legitimatie, @Param("playerId") Integer playerId);

    @Query("SELECT j FROM Jucatori j WHERE j.legitimatie = :legitimatie AND j.id != :playerId")
    Optional<Jucatori> findConflictingLegitimatie(@Param("legitimatie") String legitimatie, @Param("playerId") Integer playerId);


    @Modifying
    @Transactional
    @Query("DELETE FROM Jucatori j WHERE j.id = :playerId")
    void deleteById(@Param("playerId") Integer playerId);
    //!!!!!!!!!!!!!!!
    @Query(value = """
    SELECT MAX(j.Salariu) AS top_salary
    FROM Jucatori j
    JOIN Echipe e ON j.EchipaID = e.EchipaID
    JOIN Participare p ON e.EchipaID = p.EchipaID
    JOIN Competitii c ON p.CompetitieID = c.CompetitieID
    WHERE c.CompetitieID = :competitionId
""", nativeQuery = true)
    Integer findTopSalaryByCompetitionId(@Param("competitionId") Integer competitionId);

}

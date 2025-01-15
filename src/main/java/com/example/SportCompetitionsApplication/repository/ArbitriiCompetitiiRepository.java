/** interfata pentru query pentru tabelul ArbitriiCompetitii
 * @author Bobeica Alexandru
 * @version 5 Decembrie 2024
 */
package com.example.SportCompetitionsApplication.repository;

import com.example.SportCompetitionsApplication.models.Arbitrii;
import com.example.SportCompetitionsApplication.models.ArbitriiCompetitii;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface ArbitriiCompetitiiRepository extends JpaRepository <ArbitriiCompetitii, Integer> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO ArbitriiCompetitii (arbitruID, competitieID, nrPartide, salariu) VALUES (:arbitruID, :competitieID, :nrPartide, :salariu)", nativeQuery = true)
    void addArbitruToCompetition(@Param("arbitruID") Integer arbitruID, @Param("competitieID") Integer competitieID,
                                 @Param("nrPartide") Integer nrPartide, @Param("salariu") Integer salariu);

    @Query("""
        SELECT ac.arbitruID
        FROM ArbitriiCompetitii ac
        WHERE ac.competitieID.id = :competitionId
    """)
    List<Arbitrii> findRefereesByCompetitionId(@Param("competitionId") Integer competitionId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ArbitriiCompetitii ac WHERE ac.id.competitieID = :competitionId")
    void deleteByCompetitionId(@Param("competitionId") Integer competitionId);


}

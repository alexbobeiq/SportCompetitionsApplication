/** interfata pentru query pentru tabelul Sponsori
 * @author Bobeica Alexandru
 * @version 5 Ianuarie 2024
 */
package com.example.SportCompetitionsApplication.repository;

import com.example.SportCompetitionsApplication.models.Sponsori;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SponsoriRepository extends JpaRepository<Sponsori, Integer> {

    @Query("SELECT s FROM Sponsori s WHERE s.echipaID.id = :teamId")
    List<Sponsori> findByTeamId(@Param("teamId") Integer teamId);
}


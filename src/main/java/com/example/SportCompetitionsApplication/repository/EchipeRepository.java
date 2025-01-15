/** interfata pentru query pentru tabelul Echipe
 * @author Bobeica Alexandru
 * @version 5 Ianuarie 2024
 */
package com.example.SportCompetitionsApplication.repository;

import com.example.SportCompetitionsApplication.models.Echipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EchipeRepository extends JpaRepository<Echipe, Integer> {
    @Query("SELECT e FROM Echipe e WHERE e.managerID.id = :managerId")
    Echipe findByManagerId(@Param("managerId") Integer managerId);
    /////!!!!!!
    @Query(value = """
            SELECT DISTINCT s.nume AS sponsor_name, c.nume AS competition_name
                        FROM Echipe e
                        JOIN Participare p ON e.EchipaID = p.EchipaID
                        JOIN Sponsori s ON p.EchipaID = s.EchipaID
            			JOIN Competitii c ON c.CompetitieID = p.CompetitieID
                        WHERE e.ManagerID = 14
""", nativeQuery = true)
    List<Object[]> findSponsorCompetitionByUserId(@Param("userId") Integer userId);

}

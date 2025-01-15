/** interfata pentru query pentru tabelul Echipe
 * @author Bobeica Alexandru
 * @version 5 Ianuarie 2024
 */
package com.example.SportCompetitionsApplication.repository;

import com.example.SportCompetitionsApplication.models.Echipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EchipeRepository extends JpaRepository<Echipe, Integer> {
    @Query("SELECT e FROM Echipe e WHERE e.managerID.id = :managerId")
    Echipe findByManagerId(@Param("managerId") Integer managerId);

}

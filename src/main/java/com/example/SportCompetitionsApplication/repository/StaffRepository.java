/** interfata pentru query pentru tabelul Staff
 * @author Bobeica Alexandru
 * @version 5 Ianuarie 2024
 */
package com.example.SportCompetitionsApplication.repository;

import com.example.SportCompetitionsApplication.models.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {

    @Query("SELECT s FROM Staff s WHERE s.echipaID.id = :teamId")
    List<Staff> findByTeamId(@Param("teamId") Integer teamId);
}

/** interfata pentru query pentru tabelul Arbitrii
 * @author Bobeica Alexandru
 * @version 5 Decembrie 2024
 */
package com.example.SportCompetitionsApplication.repository;

import com.example.SportCompetitionsApplication.models.Arbitrii;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArbitriiRepository extends CrudRepository<Arbitrii, Integer> {

    @Query("SELECT a FROM Arbitrii a")
    List<Arbitrii> findAllReferees();
}

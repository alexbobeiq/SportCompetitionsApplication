/** interfata pentru query pentru tabelul Users
 * @author Bobeica Alexandru
 * @version 5 Ianuarie 2024
 */
package com.example.SportCompetitionsApplication.repository;

import com.example.SportCompetitionsApplication.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {

    Optional<User> findById(Integer id);

    @Query(value = "select * from Users", nativeQuery = true)
    List<User> aduTotiUserii();

    @Query(value = "select * from Users where UserID = :id ", nativeQuery = true)
    User aduUser(@Param("id")Integer id);

    @Query(value = "select * from Users where username = :username and password = :password", nativeQuery = true)
    List<User> getUserID(@Param("username") String username, @Param("password") String password);

    @Query(value = "select * from Users where username = :username", nativeQuery = true)
    List<User> getUserIDByUsername(@Param("username") String username);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO Users (username, password) VALUES (:username, :password)", nativeQuery = true)
    void saveUser(@Param("username") String username, @Param("password") String password);
}
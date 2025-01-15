/** clasa pentru salvarea userului logat
 * @author Bobeica Alexandru
 * @version 23 decembrie 2024
 */
package com.example.SportCompetitionsApplication.services;

import com.example.SportCompetitionsApplication.models.User;
import com.example.SportCompetitionsApplication.repository.UsersRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
public class LoggedInUser {

    @Autowired
    private UsersRepository usersRepository;
    private Integer userId;

    public String getLoggedInUserRole() {
        if (userId == null) {
            return null;
        }

        User user = usersRepository.findById(userId).orElse(null);
        return user != null ? user.getRole() : null;
    }
}

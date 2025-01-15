/** Clasa pentru Controller-ul paginii de login
 * @author Nume Student
 * @version 5 ianuarie 2025
 */
package com.example.SportCompetitionsApplication.controller;

import com.example.SportCompetitionsApplication.services.LoggedInUser;
import com.example.SportCompetitionsApplication.models.User;
import com.example.SportCompetitionsApplication.services.loginRequest;
import com.example.SportCompetitionsApplication.repository.CompetitiiRepository;
import com.example.SportCompetitionsApplication.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class LoginController {

    private final UsersRepository usersRepository;
    private final CompetitiiRepository competitiiRepository;
    @Autowired
    private LoggedInUser loggedInUser;

    @GetMapping("/")
    public String getLoginPage(Model model) {
        model.addAttribute("loginRequest", new loginRequest());
        return "index";
    }

    // logarea userului
    @PostMapping("/loginUser")
    public String loginUser(@ModelAttribute loginRequest loginRequest, Model model) {
        log.info("Login request: {}", loginRequest);

        List<User> list = usersRepository.getUserID(loginRequest.getUsername(), loginRequest.getPassword());
        if (list.isEmpty()) {
            model.addAttribute("error", "Invalid username or password");
            return "index";
        }

        User loggedInUserEntity = list.get(0);

        // Salvarea userului logat
        loggedInUser.setUserId(loggedInUserEntity.getId());
        log.info("User logged in successfully with ID: {}", loggedInUserEntity.getId());

        if ("ROLE_MANAGER".equals(loggedInUser.getLoggedInUserRole())) {
            return "redirect:/teamPlayers";
        }

        return "redirect:/competitions";
    }

    // Sign-Up
    @PostMapping("/registerUser")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {
        log.info("Registering user: username={}", username);

        //validare username unic
        List<User> existingUsers = usersRepository.getUserIDByUsername(username);
        if (!existingUsers.isEmpty()) {
            model.addAttribute("error", "The username '" + username + "' is already taken.");
            return "register";
        }

        // validare parola
        if (password.length() < 5) {
            model.addAttribute("error", "Password must be at least 5 characters long");
            return "register";
        }

        boolean containsDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                containsDigit = true;
                break;
            }
        }
        if (!containsDigit) {
            model.addAttribute("error", "Password must contain at least one digit");
            return "register";
        }

        boolean containsSpecialChar = false;
        for (char c : password.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                containsSpecialChar = true;
                break;
            }
        }
        if (!containsSpecialChar) {
            model.addAttribute("error", "Password must contain at least one special character");
            return "register";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }

        usersRepository.saveUser(username, password);
        model.addAttribute("success", "Registration successful! You can now log in.");
        return "login"; // Redirect to login page after successful registration
    }


    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register"; // Maps to register.html
    }



}

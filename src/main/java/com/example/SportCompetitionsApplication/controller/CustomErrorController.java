/** Clasa pentru Controller-ul pentru gestionarea erorilor
 * @author Bobeica Alexandru
 * @version 5 Ianuarie 2024
 */
package com.example.SportCompetitionsApplication.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(Model model) {
        model.addAttribute("message", "Oops! Something went wrong.");
        return "error"; // Maps to error.html in the templates folder
    }
}

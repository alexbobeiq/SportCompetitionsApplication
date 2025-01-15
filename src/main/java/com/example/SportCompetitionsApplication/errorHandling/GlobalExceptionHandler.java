package com.example.SportCompetitionsApplication.errorHandling;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public String handleException(Exception ex, Model model) {
    // Add the exception message to the model
    model.addAttribute("message", ex.getMessage());
    // Return the error.html template
    return "error";
  }
}

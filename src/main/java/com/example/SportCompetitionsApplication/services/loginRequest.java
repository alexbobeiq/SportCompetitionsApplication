/** clasa pentru requestul de login
 * @author Bobeica Alexandru
 * @version 23 decembrie 2024
 */
package com.example.SportCompetitionsApplication.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class loginRequest {
    private String username;
    private String password;
}

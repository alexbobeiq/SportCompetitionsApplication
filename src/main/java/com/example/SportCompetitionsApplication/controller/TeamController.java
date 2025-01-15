/** Clasa pentru Controller-ul pentru pagina de management echipa
 * @author Bobeica Alexandru
 * @version 11 Ianuarie 2025
 */
package com.example.SportCompetitionsApplication.controller;

import com.example.SportCompetitionsApplication.repository.*;
import com.example.SportCompetitionsApplication.services.LoggedInUser;
import com.example.SportCompetitionsApplication.models.Echipe;
import com.example.SportCompetitionsApplication.models.Jucatori;
import com.example.SportCompetitionsApplication.models.Sponsori;
import com.example.SportCompetitionsApplication.models.Staff;
import com.example.SportCompetitionsApplication.services.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

@Controller
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class TeamController {

    @Autowired
    private CompetitiiRepository competitiiRepository;

    @Autowired
    private JucatoriRepository jucatoriRepository;

    @Autowired
    private EchipeRepository echipeRepository;

    @Autowired
    private LoggedInUser loggedInUserComponent;

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private SponsoriRepository sponsoriRepository;


    @GetMapping("/teamPlayers")
    public String showTeamPlayers(Model model) {
        // ID-ul managerului
        Integer managerId = loggedInUserComponent.getUserId();

        // Cautarea echipei
        Echipe team = echipeRepository.findByManagerId(managerId);
        if (team == null) {
            model.addAttribute("error", "No team found for this manager.");
            return "teamPlayers"; // Show empty page with error
        }

        // Fetch sponsor and competition information
        List<Object[]> sponsorCompetitionList = echipeRepository.findSponsorCompetitionByUserId(managerId);

        // Group competitions by sponsor
        Map<String, List<String>> sponsorCompetitionsMap = new LinkedHashMap<>();
        for (Object[] row : sponsorCompetitionList) {
            String sponsorName = (String) row[0];
            String competitionName = (String) row[1];

            sponsorCompetitionsMap
                    .computeIfAbsent(sponsorName, k -> new ArrayList<>())
                    .add(competitionName);
        }

        // Se salveaza jucatorii
        List<Jucatori> players = jucatoriRepository.findByEchipaId(team.getId());

        model.addAttribute("teamName", team.getNume());
        model.addAttribute("players", players);
        model.addAttribute("teamId", team.getId());
        model.addAttribute("sponsorCompetitionsMap", sponsorCompetitionsMap);

        // Statistici
        Integer teamId = team.getId();
        Map<String, Object> statistics = statisticsService.getTeamStatistics(managerId, teamId);

        model.addAttribute("statistics", statistics);

        // Se salveaza Staff
        List<Staff> staffList = staffRepository.findByTeamId(team.getId());
        model.addAttribute("staff", staffList);

        // se salveaza Sponsori
        List<Sponsori> sponsors = sponsoriRepository.findByTeamId(team.getId());
        model.addAttribute("sponsors", sponsors);

        model.addAttribute("teamName", team.getNume());

        return "teamPlayers";
    }

    // adaugarea unui noi jucator in echipa
    @PostMapping("/teamPlayers/add")
    public String addPlayer(
            @RequestParam Integer teamId,
            @RequestParam String nume,
            @RequestParam String prenume,
            @RequestParam String legitimatie,
            @RequestParam Character sex,
            @RequestParam Integer salariu,
            @RequestParam(required = false) String dataNasterii,
            Model model) {

        // validari pentru legitimatie
        if (jucatoriRepository.existsByLegitimatie(legitimatie, null)) {
            model.addAttribute("error", "Legitimation key '" + legitimatie + "' is already taken.");
            return showTeamPlayers(model);
        }

        if (!legitimatie.matches("[a-zA-Z0-9]+")) {
            model.addAttribute("error", "Legitimation key can only contain letters and digits.");
            return showTeamPlayers(model);
        }

        if(!dataNasterii.isEmpty() && !dataNasterii.isBlank()){
            LocalDate parsedDate;
            try {
                parsedDate = LocalDate.parse(dataNasterii); // Convert string to LocalDate
            } catch (DateTimeParseException e) {
                model.addAttribute("error", "Invalid date format. Please use YYYY-MM-DD.");
                return showTeamPlayers(model);
            }
            if (LocalDate.now().minusYears(16).isBefore(parsedDate)) {
                model.addAttribute("error", "Player must be at least 16 years old.");
                return showTeamPlayers(model);
            }
        }

        if (!sex.equals('M') && !sex.equals('F')) {
            model.addAttribute("error", "Sex field must be 'M' or 'F'.");
            return showTeamPlayers(model);
        }

        Echipe team = echipeRepository.findById(teamId).orElse(null);
        if (team != null) {
            Jucatori newPlayer = new Jucatori();
            newPlayer.setEchipaID(team);
            newPlayer.setNume(nume);
            newPlayer.setPrenume(prenume);
            newPlayer.setLegitimatie(legitimatie);
            newPlayer.setSex(sex);
            newPlayer.setSalariu(salariu);
            if(!dataNasterii.isEmpty())
                newPlayer.setDataNasterii(Instant.parse(dataNasterii + "T00:00:00Z"));
            jucatoriRepository.save(newPlayer);
        }
        return "redirect:/teamPlayers";
    }

    // Editarea unui jucator
    @PostMapping("/teamPlayers/edit")
    public String editPlayer(
            @RequestParam Integer playerId,
            @RequestParam String nume,
            @RequestParam String prenume,
            @RequestParam String legitimatie,
            @RequestParam Character sex,
            @RequestParam Integer salariu,
            @RequestParam String dataNasterii,
            Model model) {

        Optional<Jucatori> conflictingPlayer = jucatoriRepository.findConflictingLegitimatie(legitimatie, playerId);
        if (conflictingPlayer.isPresent()) {
            model.addAttribute("error", "Legitimation key '" + legitimatie + "' is already taken by another player.");
            return showTeamPlayers(model);
        }

        if (!legitimatie.matches("[a-zA-Z0-9]+")) {
            model.addAttribute("error", "Legitimation key can only contain letters and digits.");
            return showTeamPlayers(model);
        }

        if(!dataNasterii.isEmpty()){
            LocalDate parsedDate;
            try {
                parsedDate = LocalDate.parse(dataNasterii);
            } catch (DateTimeParseException e) {
                model.addAttribute("error", "Invalid date format. Please use YYYY-MM-DD.");
                return showTeamPlayers(model);
            }
            if (LocalDate.now().minusYears(16).isBefore(parsedDate)) {
                model.addAttribute("error", "Player must be at least 16 years old.");
                return showTeamPlayers(model);
            }
        }

        if (!sex.equals('M') && !sex.equals('F')) {
            model.addAttribute("error", "Sex field must be 'M' or 'F'.");
            return showTeamPlayers(model);
        }

        Jucatori player = jucatoriRepository.findById(playerId).orElse(null);
        if (player != null) {
            player.setNume(nume);
            player.setPrenume(prenume);
            player.setLegitimatie(legitimatie);
            player.setSex(sex);
            player.setSalariu(salariu);
            if(!dataNasterii.isEmpty())
                player.setDataNasterii(Instant.parse(dataNasterii + "T00:00:00Z"));
            jucatoriRepository.save(player);
        }
        return "redirect:/teamPlayers";
    }

    // Stergerea unui jucator
    @PostMapping("/teamPlayers/delete")
    public String deletePlayer(@RequestParam Integer playerId) {
        jucatoriRepository.deleteById(playerId);
        return "redirect:/teamPlayers";
    }

    // Adaugarea unui angajat Staff
    @PostMapping("/teamPlayers/staff/add")
    public String addStaff(
            @RequestParam Integer teamId,
            @RequestParam String nume,
            @RequestParam String prenume,
            @RequestParam(required = false) String post,
            @RequestParam(required = false) String dataAngajarii,
            @RequestParam(required = false) Character sex,
            @RequestParam(required = false) Integer salariu,
            Model model) {

        Echipe team = echipeRepository.findById(teamId).orElse(null);
        if (team == null) {
            model.addAttribute("error", "Invalid team ID.");
            return showTeamPlayers(model);
        }

        Staff staff = new Staff();
        staff.setEchipaID(team);
        staff.setNume(nume);
        staff.setPrenume(prenume);
        staff.setPost(post);
        staff.setSalariu(salariu);

        if (dataAngajarii != null && !dataAngajarii.isEmpty()) {
            try {
                staff.setDataAngajarii(Instant.parse(dataAngajarii + "T00:00:00Z"));
            } catch (DateTimeParseException e) {
                model.addAttribute("error", "Invalid date format for hiring date.");
                return showTeamPlayers(model);
            }
        }

        if (sex != null && !(sex.equals("M") || sex.equals("F"))) {
            model.addAttribute("error", "Sex must be 'M' or 'F'.");
            return showTeamPlayers(model);
        }

        staff.setSex(sex);
        staffRepository.save(staff);
        return "redirect:/teamPlayers";
    }

    //Editarea unui angajat Staff
    @PostMapping("/teamPlayers/staff/edit")
    public String editStaff(
            @RequestParam Integer staffId,
            @RequestParam String nume,
            @RequestParam String prenume,
            @RequestParam(required = false) String post,
            @RequestParam(required = false) String dataAngajarii,
            @RequestParam(required = false) Character sex,
            @RequestParam(required = false) Integer salariu,
            Model model) {

        Staff staff = staffRepository.findById(staffId).orElse(null);
        if (staff == null) {
            model.addAttribute("error", "Invalid staff ID.");
            return showTeamPlayers(model);
        }

        staff.setNume(nume);
        staff.setPrenume(prenume);
        staff.setPost(post);
        staff.setSalariu(salariu);

        if (dataAngajarii != null && !dataAngajarii.isEmpty()) {
            try {
                staff.setDataAngajarii(Instant.parse(dataAngajarii + "T00:00:00Z"));
            } catch (DateTimeParseException e) {
                model.addAttribute("error", "Invalid date format for hiring date.");
                return showTeamPlayers(model);
            }
        }

        if (sex != null && !(sex.equals("M") || sex.equals("F"))) {
            model.addAttribute("error", "Sex must be 'M' or 'F'.");
            return showTeamPlayers(model);
        }

        staff.setSex(sex);
        staffRepository.save(staff);
        return "redirect:/teamPlayers";
    }

    // Stergerea Unui Angajat Staff
    @PostMapping("/teamPlayers/staff/delete")
    public String deleteStaff(@RequestParam Integer staffId) {
        staffRepository.deleteById(staffId);
        return "redirect:/teamPlayers";
    }

    // Adaugare sponsor
    @PostMapping("/teamPlayers/sponsors/add")
    public String addSponsor(
            @RequestParam Integer teamId,
            @RequestParam String nume,
            @RequestParam Integer valoareContract,
            @RequestParam(required = false) String dataIncepereContract,
            Model model) {

        Echipe team = echipeRepository.findById(teamId).orElse(null);
        if (team == null) {
            model.addAttribute("error", "Invalid team ID.");
            return showTeamPlayers(model);
        }

        Sponsori sponsor = new Sponsori();
        sponsor.setEchipaID(team);
        sponsor.setNume(nume);
        sponsor.setValoareContract(valoareContract);

        if (dataIncepereContract != null && !dataIncepereContract.isEmpty()) {
            try {
                sponsor.setDataIncepereContract(Instant.parse(dataIncepereContract + "T00:00:00Z"));
            } catch (DateTimeParseException e) {
                model.addAttribute("error", "Invalid date format for contract start date.");
                return showTeamPlayers(model);
            }
        }

        sponsoriRepository.save(sponsor);
        return "redirect:/teamPlayers";
    }

    @PostMapping("/teamPlayers/sponsors/edit")
    public String editSponsor(
            @RequestParam Integer sponsorId,
            @RequestParam String nume,
            @RequestParam Integer valoareContract,
            @RequestParam(required = false) String dataIncepereContract,
            Model model) {

        Sponsori sponsor = sponsoriRepository.findById(sponsorId).orElse(null);
        if (sponsor == null) {
            model.addAttribute("error", "Invalid sponsor ID.");
            return showTeamPlayers(model);
        }

        sponsor.setNume(nume);
        sponsor.setValoareContract(valoareContract);

        if (dataIncepereContract != null && !dataIncepereContract.isEmpty()) {
            try {
                sponsor.setDataIncepereContract(Instant.parse(dataIncepereContract + "T00:00:00Z"));
            } catch (DateTimeParseException e) {
                model.addAttribute("error", "Invalid date format for contract start date.");
                return showTeamPlayers(model);
            }
        }

        sponsoriRepository.save(sponsor);
        return "redirect:/teamPlayers";
    }

    @PostMapping("/teamPlayers/sponsors/delete")
    public String deleteSponsor(@RequestParam Integer sponsorId) {
        sponsoriRepository.deleteById(sponsorId);
        return "redirect:/teamPlayers";
    }

}

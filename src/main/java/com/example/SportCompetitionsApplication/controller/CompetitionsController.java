/** Clasa pentru Controller-ul paginii de organizare de competitii
 * @author Bobeica Alexandru
 * @version 5 Ianuarie 2024
 */

package com.example.SportCompetitionsApplication.controller;

import com.example.SportCompetitionsApplication.services.LoggedInUser;
import com.example.SportCompetitionsApplication.models.Arbitrii;
import com.example.SportCompetitionsApplication.models.Competitii;
import com.example.SportCompetitionsApplication.models.Participare;
import com.example.SportCompetitionsApplication.repository.ArbitriiCompetitiiRepository;
import com.example.SportCompetitionsApplication.repository.CompetitiiRepository;
import com.example.SportCompetitionsApplication.services.ParticipareService;
import com.example.SportCompetitionsApplication.repository.EchipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/competitions")
public class CompetitionsController {

    @Autowired
    private ParticipareService participareService;

    @Autowired
    private EchipeRepository echipeRepository;


    @Autowired
    private LoggedInUser loggedInUser;
    @Autowired
    private CompetitiiRepository competitiiRepository;
    @Autowired
    private ArbitriiCompetitiiRepository arbitriiCompetitiiRepository;

    @GetMapping
    public String showCompetitions(Model model) {
        // Generarea paginii
        // 1. Formular pentru crearea unei nnoi competitii
        // 2. Drop down menu pentru afisarea competiilor in desfasurare
        // 3. Dropdown menu pentru istoricul competitiilor
        Integer userId = loggedInUser.getUserId();
        System.out.println(userId);
        model.addAttribute("loggedInUser", userId);
        model.addAttribute("teams", echipeRepository.findAll());
        Iterable<Competitii> ongoingCompetitions = competitiiRepository.findOngoingCompetitions();
        List<Competitii> endedCompetitions = competitiiRepository.findEndedCompetitions();

        ongoingCompetitions.forEach(competition -> participareService.updateCompetitionWinner(competition.getId()));
        endedCompetitions.forEach(competition -> participareService.updateCompetitionWinner(competition.getId()));
        model.addAttribute("ongoingCompetitions", ongoingCompetitions);
        model.addAttribute("endedCompetitions", endedCompetitions);


        // Maparea echipelor participante pentru fiecare competitie in desfasurare
        Map<Integer, List<Participare>> participationsMap = new HashMap<>();
        for (Competitii competition : ongoingCompetitions) {
            List<Participare> participations = participareService.getParticipationsByCompetitionId(competition.getId());
            participationsMap.put(competition.getId(), participations);
        }

        // Maparea echipelor participante pentru fiecare competitie finalizata
        Map<Integer, List<Participare>> participationsMapEnded = new HashMap<>();
        for (Competitii competition : endedCompetitions) {
            List<Participare> participationsEnded = participareService.getParticipationsByCompetitionId(competition.getId());
            participationsMapEnded.put(competition.getId(), participationsEnded);
        }

        // Maparea arbitrilor
        Map<Integer, List<Arbitrii>> refereesMap = new HashMap<>();
        for (Competitii competition : ongoingCompetitions) {
            refereesMap.put(competition.getId(), participareService.getRefereesForCompetition(competition.getId()));
        }
        System.out.println(refereesMap);

        // Fetch competitions with total players
        List<Object[]> competitionsWithPlayers = competitiiRepository.findCompetitionsWithTotalPlayers();

        // Create a map to associate competition IDs with the total number of players
        Map<Integer, Long> competitionPlayerCounts = new HashMap<>();
        for (Object[] row : competitionsWithPlayers) {
            Integer competitionId = (Integer) row[0];
            Long totalPlayers = ((Number) row[2]).longValue();
            competitionPlayerCounts.put(competitionId, totalPlayers);
        }


        model.addAttribute("competitionPlayerCounts", competitionPlayerCounts);
        model.addAttribute("participationsMap", participationsMap);
        model.addAttribute("participationsMapEnded", participationsMapEnded);
        model.addAttribute("ongoingCompetitions", ongoingCompetitions);
        model.addAttribute("endedCompetitions", endedCompetitions);
        model.addAttribute("teams", echipeRepository.findAll());
        model.addAttribute("referees", participareService.getAllReferees());
        model.addAttribute("refereesMap", refereesMap);
        return "competitions";
    }

    // Logica crearii unei noi competitii
    @PostMapping("/create")
    public String createCompetition(@RequestParam String name,
                                    @RequestParam String startDate,
                                    @RequestParam String endDate,
                                    @RequestParam String location,
                                    @RequestParam Double prizePool,
                                    @RequestParam List<Integer> teamIds,
                                    @RequestParam List<Integer> refereeIds,
                                    Model model) {
        try {
            if (teamIds.isEmpty()) {
                throw new IllegalArgumentException("At least one team must be selected.");
            }

            Competitii competition = new Competitii();
            competition.setNume(name);
            competition.setDataIncepere(Instant.parse(startDate + "T00:00:00Z"));
            competition.setDataSfarsit(Instant.parse(endDate + "T23:59:59Z"));
            competition.setLocatie(location);
            competition.setPrizePool(prizePool);
            competition.setCastigator(null);
            competition.setCreatedBy(1);

            participareService.addCompetition(competition, teamIds);

            if (refereeIds != null && !refereeIds.isEmpty()) {
                participareService.assignRefereesToCompetition(competition.getId(), refereeIds);
            }

            model.addAttribute("success", "Competition created successfully!");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return showCompetitions(model);
        }

        return showCompetitions(model);
    }

    //Logica stergerii unei competitii
    @PostMapping("/delete")
    public String deleteCompetition(@RequestParam Integer competitionId,
                                    @RequestParam Integer createdBy,
                                    Model model) {
        System.out.println("received detete request for competition:" + (competitionId));
        try {
            arbitriiCompetitiiRepository.deleteByCompetitionId(competitionId);
            participareService.deleteCompetition(competitionId, createdBy);
            model.addAttribute("success", "Competition deleted successfully!");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            System.out.println("error:" + e.getMessage());
            return showCompetitions(model);
        }
        return showCompetitions(model);
    }

    @GetMapping("/{competitionId}/participations")
    public String getParticipations(@PathVariable Integer competitionId, Model model) {
        List<Participare> participations = participareService.getParticipationsByCompetitionId(competitionId);

        // Sortarea echipelor in functie de puncte. Daca punctajul este egal, se sorteaza dupa nr de victorii
        participations.sort(Comparator.comparingInt((Participare p) ->
                        (p.getNrVictorii() != null ? p.getNrVictorii() : 0) * 3
                                + (p.getNrEgaluri() != null ? p.getNrEgaluri() : 0)) // calcularea punctelor
                .thenComparingInt(p -> p.getNrVictorii() != null ? p.getNrVictorii() : 0)
                .reversed()
        );

        model.addAttribute("participations", participations);
        return "participations";
    }

    @PostMapping("/{competitionId}/updateTeamStats")
    public String editTeamStats(@PathVariable Integer competitionId,
                                @RequestParam Integer teamId,
                                @RequestParam Integer nrVictorii,
                                @RequestParam Integer nrEgaluri,
                                @RequestParam Integer nrInfrangeri,
                                Model model) {
        try {
            int maxMatches = (participareService.getParticipationsByCompetitionId(competitionId).size() - 1) * 2;

            participareService.updateTeamStats(competitionId, teamId, nrVictorii, nrEgaluri, nrInfrangeri, maxMatches);

            model.addAttribute("success", "Team stats updated successfully.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return showCompetitions(model);
        }

        return showCompetitions(model);
    }
    // Finalizarea unei competitii
    @PostMapping("/{competitionId}/end")
    public String endCompetition(@PathVariable Integer competitionId) {
        participareService.endCompetition(competitionId);
        return "redirect:/competitions";
    }

}

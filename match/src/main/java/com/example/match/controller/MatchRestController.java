package com.example.match.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.match.service.MatchService;

@RestController
public class MatchRestController {
    private static MatchService matchService;
    public MatchRestController(MatchService matchService) {
        this.matchService = matchService;
    }
    @PostMapping("/create")
    //Endpoint to create a match
    public String createMatch(@RequestBody String[] teams) {
        return matchService.createMatch(teams[0], teams[1]);
    }
    @GetMapping("/active_count")
    //Endpoint to count active matches
    public int countActiveMatches() {
        return matchService.countActiveMatches();
    }
    @GetMapping("/total/{team}")
    public int getGoalCount(@PathVariable String team) {
        //Endpoint to get the total number of goals scored by a specific team
        return matchService.getGoalCount(team);
    }
    @GetMapping("/score/{matchId}")
    public String getMethodName(@PathVariable String matchId) {
    	//Endpoint to get the score of a certain match
        return matchService.getMatchDetails(matchId);
    }
    static MatchService getMatchService() {
        return matchService;
    }
}

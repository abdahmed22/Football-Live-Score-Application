package com.example.match.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.example.match.model.Match;
import com.example.match.model.MatchStat;

@Service
public class MatchService {
    Map<String, Match> matchMap=new ConcurrentHashMap<>();
    AtomicInteger newId=new AtomicInteger(1);
    public String createMatch(String teamA, String teamB) {
        String id = String.valueOf(newId.getAndIncrement());
        Match match = new Match(teamA, teamB);
        matchMap.put(id, match);
        return id;
    }

    public int incrementScore(String id, String team) {
        Match match = matchMap.get(id);
        if (match == null) {
            return -1;
        }

        return switch (team) {
            case "A" -> {
                match.incrementScoreA();
                yield 1;
            }
            case "B" -> {
                match.incrementScoreB();
                yield 1;
            }
            default -> -1;
        };
    }

    public String getMatchDetails(String id) {
        Match match = matchMap.get(id);

        if(match == null){
            return "";
        }

        return match.getMatchScore();
    }

    public void endMatch(String id){
        matchMap.get(id).endMatch();
    }

    public int countActiveMatches() {
        return Math.toIntExact(
            matchMap.values().stream()
                    .filter(match -> match.getMatchStat() == MatchStat.LIVE)
                    .count()
        );
    }

    public int getGoalCount(String team) {
        return matchMap.values().stream()
                .mapToInt(match ->
                    (team.equalsIgnoreCase(match.getTeamA()) ? match.getScoreA() : 0) +
                    (team.equalsIgnoreCase(match.getTeamB()) ? match.getScoreB() : 0)
                ).sum();
    }
}

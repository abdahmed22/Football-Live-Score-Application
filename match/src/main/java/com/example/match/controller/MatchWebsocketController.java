package com.example.match.controller;

import com.example.match.model.Message;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.match.service.MatchService;

@Controller
public class MatchWebsocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private MatchService matchService;

    public MatchWebsocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        this.matchService = MatchRestController.getMatchService();
    }

    @MessageMapping("/match/update/{matchId}")
    public void updateScore(@DestinationVariable String matchId, Message message) {
        String team = message.matchScore();
        int result = matchService.incrementScore(matchId, team);

        if (result == -1) {
            return;
        }

        String updatedScore = matchService.getMatchDetails(matchId);
        messagingTemplate.convertAndSend("/topic/match/" + matchId, new Message(updatedScore, false));
    }

    @MessageMapping("/match/end/{matchId}")
    public void endMatch(@DestinationVariable String matchId, Message message) {
        if (message.isEnded()) {
            matchService.endMatch(matchId);
        }

        String finalScore = matchService.getMatchDetails(matchId);
        messagingTemplate.convertAndSend("/topic/match/" + matchId, new Message(finalScore, true));
    }
}
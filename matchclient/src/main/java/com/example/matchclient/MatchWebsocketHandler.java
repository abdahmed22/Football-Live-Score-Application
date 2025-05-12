package com.example.matchclient;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Scanner;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;

import java.lang.reflect.Type;

import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.messaging.converter.MessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;


public class MatchWebsocketHandler {
    StompSession stompSession;
    private static Thread inputThread;
    

    public void connectToWebSocket() {
        try {       
            List<Transport> transports = Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()));
            SockJsClient sockJsClient;
            WebSocketStompClient stompClient;

            sockJsClient = new SockJsClient(transports);
            stompClient = new WebSocketStompClient(sockJsClient);

            List<MessageConverter> converters = Collections.singletonList(new MappingJackson2MessageConverter());
            ((MappingJackson2MessageConverter) converters.get(0)).setObjectMapper(new ObjectMapper());
            stompClient.setMessageConverter(converters.get(0));

            StompSessionHandler sessionHandler = new MyStompSessionHandler();
            this.stompSession = stompClient.connect("ws://localhost:8080/ws", sessionHandler).get();
        } catch (Exception e) {
            System.out.println("Websocket connection failed");
        }
    }
    public void updateScore(String team, String matchId) {
        Message message = new Message(team, false); 
        stompSession.send("/app/match/update/" + matchId, message);
    }    
    public void endMatch(String matchId) {
        Message message = new Message("", true); 
        stompSession.send("/app/match/end/" + matchId , message);
    }

    public void subscribeToMatch(String matchId) {
        connectToWebSocket();
        AtomicBoolean matchEnded = new AtomicBoolean(false);

        stompSession.subscribe("/topic/match/" + matchId, new StompSessionHandlerAdapter() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Message.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Message message = (Message) payload;
                if (message.isEnded()) {
                    System.out.println("Match Ended.");
                    matchEnded.set(true);
                } else {
                    System.out.println("Score updated: " + message.matchScore());
                }
            }
        });

        inputThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            try {
                scanner.nextLine();
                matchEnded.set(true);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                scanner.close();
            }
        });

        inputThread.setDaemon(true);
        inputThread.start();

        while (!matchEnded.get()) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void close(){
        if(this.stompSession!=null){
            this.stompSession.disconnect();
            System.out.println("Closed websocket connection");
        }
    }
}

class MyStompSessionHandler extends StompSessionHandlerAdapter {
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        System.err.println("An error occurred: " + exception.getMessage());
    }
}
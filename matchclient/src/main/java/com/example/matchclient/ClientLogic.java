package com.example.matchclient;

import java.util.Scanner;

public class ClientLogic {
    public static void creatorLogic(String matchId,Scanner scanner,MatchWebsocketHandler websocket) {
        int choice = 0;
		while (choice <= 1) {
			System.out.println("1. Update Score");
            System.out.println("2. End Match");
			System.out.print("Enter your choice: ");
			choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    // send score update through websocket
                    System.out.println("Which team scored?:");
                    String team=scanner.next();
                    if(!team.equalsIgnoreCase("A") && !team.equalsIgnoreCase("B")) {
                        break;
                    }
                    websocket.updateScore(team, matchId);
                    break;
                case 2:
                    // send end through websocket
                    websocket.endMatch(matchId);
                    choice=3;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    public static void statisticsClientLogic(Scanner scanner){
        int choice = 0;
		while (choice <=2) {
			System.out.println("1. Get Number of Active Matches");
            System.out.println("2. Get Total Team Goals");
			System.out.println("3. Exit");
			System.out.print("Enter your choice: ");
			choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    int active = HttpHelper.countActiveMatch();
                    System.out.println("Active Matches: " + active);
                    break;
                case 2:
                    System.out.print("Enter team name: ");
                    String teamName = scanner.next();
                    int teamMatches = HttpHelper.countTeamGoals(teamName);
                    System.out.println("Number of goals scored by " + teamName + ": " + teamMatches);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void subscriberLogic(Scanner scanner) {
        System.out.println("Press enter to exit...");
        scanner.nextLine();
        scanner.nextLine();
        System.out.println("Exiting...");
    }
}

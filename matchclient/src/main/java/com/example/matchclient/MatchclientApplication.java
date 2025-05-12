package com.example.matchclient;

import java.util.Scanner;

public class MatchclientApplication {
	public static void main(String[] args) {
		int choice = 0;
		Scanner scanner = new Scanner(System.in);
		MatchWebsocketHandler websocket=new MatchWebsocketHandler();
		try{
			//create or subscribe to auction
			while(choice != 1 && choice != 2 && choice !=3) {
				System.out.println("Welcome !");
				System.out.println("1. Create Match");
				System.out.println("2. Get Match Statistics");
				System.out.println("3. Subscribe to Match");
				System.out.print("Enter your choice: ");
				choice = scanner.nextInt();
			}
	
		String matchId =null;
		if(choice == 1) {
			matchId = HttpHelper.createMatch(scanner);
			websocket.connectToWebSocket();
			ClientLogic.creatorLogic(matchId, scanner,websocket);
		} 
		else if(choice==2){
			ClientLogic.statisticsClientLogic(scanner);	
		}
		else{
			System.out.print("Enter Match Id: ");
			matchId = scanner.next();
			String score=HttpHelper.getMatchScore(matchId);
			while (score.isBlank()) {
				System.out.println("Match not found, try again");
				matchId = scanner.next();
				score=HttpHelper.getMatchScore(matchId);
			}
			System.out.println("Score: " + score);
			final String matchIdFinal = matchId;
			websocket.subscribeToMatch(matchIdFinal);
		}}
		catch (Exception e){
			System.out.println("Invalid input");
			
		}
		finally{
		scanner.close();
		websocket.close();
	}
	}

}

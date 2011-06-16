package ia04.projet.loup;

import ia04.projet.loup.communication.AgtAction;
import ia04.projet.loup.communication.AgtVote;
import ia04.projet.loup.controller.AgtStoryteller;
import ia04.projet.loup.players.AgtPlayer;
import ia04.projet.loup.rest.AgtRest;
import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileException;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

import java.io.IOException;

public class StartGame {
	private static boolean MainComput = true;
	private static String MainContainerIP = "172.22.20.6";
	private static int MainContainerPort = 1099;

	/**
	 * @param args
	 * @throws ProfileException 
	 * @throws ControllerException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws ProfileException, ControllerException, IOException {
		Debugger.setOn(true);
		if(args.length >= 7) {
			Global.DEFAULT_NB_PLAYERS = Integer.valueOf(args[args.length-1]);
			Global.AVERAGE_SPEED = Float.valueOf(args[args.length-2]);
			MainComput = Boolean.getBoolean(args[args.length-3]);	
			MainContainerIP = args[args.length-4];		
		}
		if(MainComput){
			Runtime rt = Runtime.instance();
			Profile pf = new ProfileImpl("./resources/properties.txt");

			// Create the AgentContainer
			AgentContainer mc = rt.createMainContainer(pf);
			System.out.println("Main Container created...");

			// First create the Storyteller Agent
			AgtStoryteller storyteller = new AgtStoryteller();
			AgentController ac = mc.acceptNewAgent(Global.LOCALNAME_STORYTELLER,storyteller);
			ac.start();
			System.out.println("Storyteller agent created...");
			
			// Create a Kb agent linked to the Storyteller Agent
			storyteller.createKbAgent();
			System.out.println("StorytellerKB agent created...");
			
			// Create a Vote agent linked to the Storyteller Agent
			AgtVote vote = new AgtVote();
			ac = mc.acceptNewAgent(Global.LOCALNAME_VOTE,vote);
			ac.start();
			vote.registerServiceToDf();
			System.out.println("Vote agent created...");
			
			// Create a Vote agent linked to the Storyteller Agent
			AgtAction action = new AgtAction();
			ac = mc.acceptNewAgent(Global.LOCALNAME_ACTION,action);
			ac.start();
			action.registerServiceToDf();
			System.out.println("Action agent created...");
			
			// Link the agents to the storyteller
			storyteller.setVoteAgent(vote.getAID());
			storyteller.setActionAgent(action.getAID());
			
			// Rest Service
			System.out.println("Starting Rest Agent");
			AgtRest agtRest = new AgtRest();
			ac = mc.acceptNewAgent(Global.LOCALNAME_REST,agtRest);
			ac.start();
			
			// Create players
			System.out.println("Populating the room with players...");
			storyteller.setNbRequiredPlayers(Global.DEFAULT_NB_PLAYERS); //TODO: change nb of players
			
			//*
			storyteller.populate(storyteller.nbOfRequiredPlayersToStartAGame); 
			/*/			
			// Create a new Agent for the player
			AgtPlayer player1 = new AgtPlayer(Global.IS_HUMAN_PLAYER);
			ac = mc.acceptNewAgent("player1", player1);
			ac.start();
			player1.GuiCreation();
			// Register the Agent to this AgtStoryteller
			AID controllerAID = new AID(Global.LOCALNAME_STORYTELLER,AID.ISLOCALNAME);
			player1.Register(controllerAID);
			
			// Create a new Agent for the player
			AgtPlayer player2 = new AgtPlayer(Global.IS_HUMAN_PLAYER);
			ac = mc.acceptNewAgent("player2", player2);
			ac.start();
			player2.GuiCreation();
			// Register the Agent to this AgtStoryteller
			//AID controllerAID = new AID(Global.LOCALNAME_STORYTELLER,AID.ISLOCALNAME);
			player2.Register(controllerAID);
			//*/
			
			
		}
		else {
			Runtime rt = Runtime.instance();
			Profile p = new ProfileImpl(MainContainerIP,MainContainerPort,null,false);
			
			// Create the AgentContainer
			AgentContainer mc = rt.createAgentContainer(p);	
			
			// Create a new Agent for the player
			AgtPlayer player = new AgtPlayer(Global.IS_HUMAN_PLAYER);
			AgentController ac = mc.acceptNewAgent("aurelien", player);
			ac.start();
			player.GuiCreation();
			// Register the Agent to this AgtStoryteller
			AID controllerAID = new AID(Global.LOCALNAME_STORYTELLER,AID.ISLOCALNAME);
			player.Register(controllerAID);
		}
	}
}

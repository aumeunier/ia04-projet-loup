package ia04.projet.loup;

import ia04.projet.loup.communication.AgtVote;
import ia04.projet.loup.controller.AgtStoryteller;
import ia04.projet.loup.players.AgtPlayer;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileException;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

public class StartGame {
	private final static boolean MainComput = true;
	private final static String MainContainerIP = "172.22.20.6";
	private final static int MainContainerPort = 1099;

	/**
	 * @param args
	 * @throws ProfileException 
	 * @throws ControllerException 
	 */
	public static void main(String[] args) throws ProfileException, ControllerException {
		Debugger.setOn(true);
		if(MainComput){
			Runtime rt = Runtime.instance();
			Profile pf = new ProfileImpl("./resources/properties.txt");

			// Create the AgentContainer
			AgentContainer mc = rt.createMainContainer(pf);
			System.out.println("Main Container created...");

			// First create the Storyteller Agent
			AgtStoryteller storyteller = new AgtStoryteller();
			AgentController ac = mc.acceptNewAgent("Storyteller",storyteller);
			ac.start();
			System.out.println("Storyteller agent created...");
			
			// Create a Kb agent linked to the Storyteller Agent
			storyteller.createKbAgent();
			System.out.println("StorytellerKB agent created...");
			
			// Create a Vote agent linked to the Storyteller Agent
			AgtVote vote = new AgtVote();
			ac = mc.acceptNewAgent("Vote",vote);
			ac.start();
			vote.registerServiceToDf();
			System.out.println("Vote agent created...");
			
			// Link the agents to the storyteller
			// TODO: use the DF ? Vote find storyteller and register itself to the storyteller using a message
			storyteller.setVoteAgent(vote.getAID());
			
			// Create players
			System.out.println("Populating the room with players...");
			storyteller.populate(storyteller.nbOfRequiredPlayersToStartAGame); 
		}
		else {
			Runtime rt = Runtime.instance();
			Profile p = new ProfileImpl(MainContainerIP,MainContainerPort,null);
			
			// Create the AgentContainer
			AgentContainer mc = rt.createAgentContainer(p);	
			
			// Create a client
			AgtPlayer agp = new AgtPlayer();
			Agent a = (Agent)mc.getAgent("Storyteller");
			agp.Register(a.getAID());
		}
	}
}

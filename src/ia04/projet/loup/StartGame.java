package ia04.projet.loup;

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
import jade.wrapper.StaleProxyException;

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
		if(MainComput){
			Runtime rt = Runtime.instance();
			Profile pf = new ProfileImpl("./resources/properties.txt");

			// Create the AgentContainer
			AgentContainer mc = rt.createMainContainer(pf);
			System.out.println("Main Container created...");

			// First create the toryteller Agent
			AgtStoryteller storyteller = new AgtStoryteller();
			AgentController ac = mc.acceptNewAgent("Storyteller",storyteller);
			ac.start();
			storyteller.populate(); 
			System.out.println("Storyteller agent created...");
			
			// Start the game
			//TODO: should start when they are enough players registered and willing to play
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

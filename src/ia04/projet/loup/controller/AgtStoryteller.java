package ia04.projet.loup.controller;

import jade.core.AID;
import jade.core.Agent;

public class AgtStoryteller extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1537520826022941930L;
	
	public AgtStoryteller(){
		// TODO:
		super();
	}
	
	public void createMessageForAgent(AID receiver, String messageString, int messagePerformative){
		// TODO:
		// A - Message for Vote: demande un vote
		// B - Message for Advice: demande un tour de conseils
		// C - Message for Action: demande realisation d'une phase/action, mort d'un joueur, debut de partie
	}
	public void createMessageForRegisteredAgents(String messageString, int messagePerformative){
		// TODO:
		// A - Lancement d'une partie
		// B - Attribution des roles
		// C - Fin d'une partie
	}
}

package ia04.projet.loup.controller;

import ia04.projet.loup.Global;
import jade.core.Agent;
import jade.util.leap.HashMap;

public class AgtStoryteller extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1537520826022941930L;
	private HashMap playersMap = new HashMap();
	private Global.GamePhases currentPhase = Global.GamePhases.NONE;
	public AgtStoryteller(){
		super();
		this.addBehaviour(new BehaviourStoryteller(this));
	}

	
	/**
	 * Prepare a message for a Communication Agent
	 * @param messageType The type of message to send (is it a vote, an advice, an action)
	 * @return
	 */
	public String createMessageForCommunicationAgent(String messageType){
		// TODO:
		// A - Message for Vote: ask for a vote
		// B - Message for Advice: ask for an advice turn
		// C - Message for Action: ask for the realisation of a phase/action, notify a player's death, the beginning of a game
		return "";
	}
	/**
	 * Prepare a message to send to all the registered agents
	 * @param messageType
	 * @return
	 */
	public String createMessageForRegisteredAgents(String messageType){
		// TODO:
		// A - Start a game
		// B - Roles attribution
		// C - End of the current game
		return "";
	}
}

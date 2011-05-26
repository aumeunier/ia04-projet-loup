package ia04.projet.loup.controller;

import ia04.projet.loup.messages.mMessage;
import ia04.projet.loup.messages.mStorytellerPlayer;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class BehaviourStoryteller extends Behaviour {
	private static final long serialVersionUID = 1058995384097785392L;
	
	public BehaviourStoryteller(AgtStoryteller a){
		super(a);
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if(msg != null){
			// Message reception
			String msgString = msg.getContent();
			int msgPerformative = msg.getPerformative();
			// TODO:
			//System.out.println(msgString);
			
			// A - Message can come from an AgtPlayer:			
			// 1. Player's registration
			// 2. Player's unregistration
			// 3. Participation in the new game
			// 4. Eventual answers to role attribution
			// 5. Is the player ready to play (has finished role initialization)
			
			// B - Message can come from an AgtCommunication (Vote / Advice / Action)
			// 1. Answer to an action
			// 2. Answer to a vote 
		}
	}

	@Override
	/**
	 * Our Storyteller agent will be running constantly. 
	 * Its behaviour should also be running at all times.
	 */
	public boolean done() {
		return false;
	}

}

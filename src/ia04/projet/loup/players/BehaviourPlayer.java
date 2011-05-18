package ia04.projet.loup.players;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class BehaviourPlayer extends Behaviour {

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if(msg != null){
			// Message reception
			String msgString = msg.getContent();
			int msgPerformative = msg.getPerformative();
			// TODO:
			
			// A - Message can come from an AgtStoryteller:			
			// 1. Inscription validation
			// 2. Beginning of a game, participation request
			// 3. Role attribution
			// 4. Eventual additional messages concerning the role attribution
			// 5. End of the game
			
			// B - Message can come from an AgtRole
			// 1. Reception of information concerning the db (stats, can be reused in future games)
		}
	}

	@Override
	public boolean done() {
		return false;
	}

}

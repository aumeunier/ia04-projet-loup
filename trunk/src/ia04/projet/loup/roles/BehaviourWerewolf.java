package ia04.projet.loup.roles;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class BehaviourWerewolf extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5093693108424533789L;

	@Override
	public void action() {
		/** Waits the nightfall */

		ACLMessage msg = myAgent.receive();
		if(msg != null){
			// Message reception
			String msgString = msg.getContent();
			int msgPerformative = msg.getPerformative();			
			
			/** Votes for the victim of the night */
			/** TODO catch the right message */
			((AgtWerewolf) myAgent).eatSomebody();
		}

	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}

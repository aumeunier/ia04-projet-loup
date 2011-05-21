package ia04.projet.loup.roles;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class BehaviourVillager extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2060328251311657769L;

	/**
	 * Waits for the day and do the corresponding actions
	 *  
	 * @author claquette
	 */
	@Override
	public void action() {
		/** Waits the sunrise */

		ACLMessage msg = myAgent.receive();
		if(msg != null){
			// Message reception
			String msgString = msg.getContent();
			int msgPerformative = msg.getPerformative();			
			/** TODO beta1 mayor election - Message can come from the AgtAdvice */		
			
			/** Votes for the victim of the  day */
			/** TODO catch the right message */
			((AgtRole) myAgent).vote();
		}
			
		
			

	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}

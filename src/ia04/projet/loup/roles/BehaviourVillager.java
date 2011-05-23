package ia04.projet.loup.roles;

import ia04.projet.loup.messages.mCommunicationRole;
import jade.core.AID;
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
			AID msgSender = msg.getSender();
			int msgPerformative = msg.getPerformative();
			
			/** TODO Checks the source of the message */
			//if( sender == ACTION || ADVICE || VOTE)
				mCommunicationRole msgContent = mCommunicationRole.parseJson(msgString); //TODO mCommunicationRole.parseJson must return a mCommunicationRole object
				switch (msgContent.getType()){
				/** TODO beta1 mayor election - Message can come from the AgtAdvice */		
				case msgContent.type.ELECT_MAYOR: break;
				case NAME_SUCCESSOR: break;
				/** Votes for the victim of the  day */
				case KILL_PAYSAN: 
					((AgtRole) myAgent).vote();
					break;
				}
			}
			
		}
			

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}

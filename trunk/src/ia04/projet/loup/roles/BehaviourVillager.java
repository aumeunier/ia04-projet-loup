package ia04.projet.loup.roles;

import ia04.projet.loup.messages.mCommunicationRole;
import ia04.projet.loup.messages.mMessage;
import ia04.projet.loup.messages.mVote;
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
			//if( msgSender == ACTION || ADVICE || VOTE)
				mVote msgContent = (mVote)mMessage.parseJson(msgString, mCommunicationRole.class);
				switch (msgContent.getType()){
				/** TODO beta1 mayor election - Message can come from the AgtAdvice */		
				case ELECT_MAYOR: break;
				case SUCCESSOR: break;
				/** Votes for the victim of the  day */
				case KILL_PAYSAN: 
					msgContent.setChoice(((AgtRole) myAgent).vote(msgContent.getCandidates()));
					ACLMessage response = msg.createReply();
					response.setContent(msgContent.toJson());
					myAgent.send(response);
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

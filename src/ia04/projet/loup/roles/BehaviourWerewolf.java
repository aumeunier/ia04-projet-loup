package ia04.projet.loup.roles;

import ia04.projet.loup.messages.mCommunicationRole;
import ia04.projet.loup.messages.mMessage;
import ia04.projet.loup.messages.mVote;
import jade.core.AID;
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
			AID msgSender = msg.getSender();
			int msgPerformative = msg.getPerformative();			
			
			//TODO check who sent the message (DF) if( msgSender == ACTION || ADVICE || VOTE)
				mVote msgContent = (mVote)mMessage.parseJson(msgString, mCommunicationRole.class);
				if (msgContent.getType() == mVote.mType.KILL_WW){
					/** Votes for the victim of the night */
					msgContent.setChoice(((AgtWerewolf) myAgent).eatSomebody(msgContent.getCandidates()));
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

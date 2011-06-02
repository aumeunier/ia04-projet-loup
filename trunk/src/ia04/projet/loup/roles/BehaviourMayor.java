package ia04.projet.loup.roles;

import ia04.projet.loup.messages.mMessage;
import ia04.projet.loup.messages.mVote;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class BehaviourMayor extends RoleBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6216754252215810671L;


	/**
	 * Waits for the day and do the corresponding actions
	 *  
	 * @author claquette
	 */
	@Override
	public void roleAction(ACLMessage msg) {
		/** The mayor has two voices during a vote */
		((AgtRole)myAgent).setVoices(2);
		/** Waits the sunrise */
		// Message reception
		String msgString = msg.getContent();
		AID msgSender = msg.getSender();
		int msgPerformative = msg.getPerformative();

		/** TODO Checks the source of the message */
		//if( msgSender == ACTION || ADVICE || VOTE)
		mVote msgContent = (mVote)mMessage.parseJson(msgString, mVote.class);
		ACLMessage response = msg.createReply();
		switch (msgContent.getType()){
		/** elects the next mayor */
		case SUCCESSOR: 
			msgContent.setChoice(((AgtRole) myAgent).nameSuccessor(msgContent.getCandidates()));
			response.setContent(msgContent.toJson());
			myAgent.send(response);
			break;					

			/** Resolves equality */
		case EQUALITY: 
			msgContent.setChoice(((AgtRole) myAgent).resolveEquality(msgContent.getCandidates()));
			response.setContent(msgContent.toJson());
			myAgent.send(response);
			break;						
		}
	}
}

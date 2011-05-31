package ia04.projet.loup.roles;

import ia04.projet.loup.messages.mMessage;
import ia04.projet.loup.messages.mVote;
import ia04.projet.loup.messages.mVoteResult;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import ia04.projet.loup.communication.AgtVote;

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
			switch(msgPerformative){
			case ACLMessage.INFORM: 
				/** Gets votes of all the werewolves */
				mVoteResult msgResultContent = (mVoteResult)mMessage.parseJson(msgString, mVoteResult.class);
				if (msgResultContent.getType() == AgtVote.voteType.VOTE_WW){
					((AgtWerewolf)myAgent).setLastVote(msgResultContent.getWhoVotesForWho());
					((AgtWerewolf)myAgent).updateConfidenceVoteWerewolf();
				}
				break;
			case ACLMessage.REQUEST: 
				/** Votes for the victim of the night */
				mVote msgContent = (mVote)mMessage.parseJson(msgString, mVote.class);
				if (msgContent.getType() == AgtVote.voteType.VOTE_WW){
					/** if this isn't the first turn updates the confidence levels */
					if(msgContent.getWhoVotesForWho()==null){
						((AgtWerewolf)myAgent).setLastVote(msgContent.getWhoVotesForWho());
						((AgtWerewolf)myAgent).updateConfidenceVoteWerewolf();
					}
					msgContent.setChoice(((AgtWerewolf) myAgent).eatSomebody(msgContent.getCandidates()));
					ACLMessage response = msg.createReply();
					response.setContent(msgContent.toJson());
					myAgent.send(response);
				}
			}
				
		}
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}

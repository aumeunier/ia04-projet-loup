package ia04.projet.loup.roles;

import ia04.projet.loup.Debugger;
import ia04.projet.loup.communication.AgtVote;
import ia04.projet.loup.messages.mMessage;
import ia04.projet.loup.messages.mStartGame;
import ia04.projet.loup.messages.mVote;
import ia04.projet.loup.messages.mVoteResult;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BehaviourWerewolf extends RoleBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5093693108424533789L;

	@Override
	public void roleAction(ACLMessage msg) {
		/** Waits the nightfall */
		// Message reception
		String msgString = msg.getContent();	

		mVote msgContent = (mVote)mMessage.parseJson(msgString, mVote.class);
		/** Votes for the victim of the night */
		//Debugger.println("BehaviourWerewolf: vote to eat somebody");
		if(msgContent!=null){
			if (msgContent.getType() == AgtVote.voteType.VOTE_WW){
				//Debugger.println(myAgent.getLocalName()+" received call for a Werewolf Vote.");
				/** if this isn't the first turn updates the confidence levels */
				if(msgContent.getWhoVotesForWho()==null){
					((AgtWerewolf)myAgent).setLastVote(msgContent.getWhoVotesForWho());
					//((AgtWerewolf)myAgent).updateConfidenceVoteWerewolf();
				}
				msgContent.setChoice(((AgtWerewolf) myAgent).eatSomebody(msgContent.getCandidates()));
				ACLMessage response = msg.createReply();
				response.setPerformative(ACLMessage.INFORM);
				response.setContent(msgContent.toJson());
				myAgent.send(response);
			}
		}
		else {
			/** Gets votes of all the werewolves */
			//Debugger.println("BehaviourWerewolf: get the result of the vote");
			mVoteResult msgResultContent = (mVoteResult)mMessage.parseJson(msgString, mVoteResult.class);
			if(msgResultContent != null){
				//Debugger.println("Result of the werewolves' vote");
				if (msgResultContent.getType() == AgtVote.voteType.VOTE_WW){
					((AgtWerewolf)myAgent).setLastVote(msgResultContent.getWhoVotesForWho());
					//((AgtWerewolf)myAgent).updateConfidenceVoteWerewolf();
				}
			}
		}
	}
}

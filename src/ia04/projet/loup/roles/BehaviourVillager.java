package ia04.projet.loup.roles;

import com.hp.hpl.jena.sparql.procedure.library.debug;

import ia04.projet.loup.Debugger;
import ia04.projet.loup.messages.mAction;
import ia04.projet.loup.messages.mMessage;
import ia04.projet.loup.messages.mVote;
import ia04.projet.loup.messages.mVoteResult;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class BehaviourVillager extends RoleBehaviour {

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
	public void roleAction(ACLMessage msg) {
		/** Waits the sunrise */
		String msgString = msg.getContent();

		ACLMessage response = msg.createReply();
		mMessage message = mMessage.parseJson(msgString, mVote.class);
		// Vote requesting a vote for Mayor or Paysan
		if(message!=null){
			mVote msgContent = (mVote)message;
			switch (msgContent.getType()){
			/** TODO beta1 mayor election - Message can come from the AgtAdvice */		
			case ELECT_MAYOR: 
				msgContent.setChoice(((AgtRole) myAgent).electMayor(msgContent.getCandidates()));
				response.setContent(msgContent.toJson());
				response.setPerformative(ACLMessage.INFORM);
				myAgent.send(response);
				break;
			/** Votes for the victim of the  day */
			case VOTE_PAYSAN: 
				msgContent.setNumbreOfVoices(((AgtRole)myAgent).getVoices());
				msgContent.setChoice(((AgtRole) myAgent).vote(msgContent.getCandidates()));
				response.setContent(msgContent.toJson());
				response.setPerformative(ACLMessage.INFORM);
				myAgent.send(response);				
				break;
			}
		}
		else {
			// Vote sending results
			message = mMessage.parseJson(msgString, mVoteResult.class);
			if(message!=null){
				mVoteResult msgContent = (mVoteResult)message;
				//Debugger.println("mvoteresult received");
				switch(msgContent.getType()){
				case ELECT_MAYOR:
					((AgtRole) myAgent).newMayorElected(msgContent.getChoiceResult());
					break;
				case VOTE_PAYSAN:
					((AgtRole) myAgent).setLastVote(msgContent.getWhoVotesForWho());
					break;
				case VOTE_WW:
					((AgtWerewolf) myAgent).setLastVoteWerewolf(msgContent.getWhoVotesForWho());
					break;
				case SUCCESSOR:
					if(msgContent.getChoiceResult() != null){ // It is the result
						((AgtRole) myAgent).newMayorElected(msgContent.getChoiceResult());
					}
					break;
				}
			}
			else{
				// Action requesting an action
				message = mMessage.parseJson(msgString, mAction.class);
				if(message!=null){
					mAction msgContent = (mAction)message;
					// TODO:
				}
				else {
					// Advice requesting an action 
					// TODO: advice	
				}
			}
		}
	}
}

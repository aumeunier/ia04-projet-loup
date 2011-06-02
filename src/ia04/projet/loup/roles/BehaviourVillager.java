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
		// Message reception
		String msgString = msg.getContent();
		AID msgSender = msg.getSender();
		int msgPerformative = msg.getPerformative();

		/** TODO Checks the source of the message */
		//if( msgSender == ACTION || ADVICE || VOTE)
		ACLMessage response = msg.createReply();
		mMessage message = mMessage.parseJson(msgString, mVote.class);
		// if msgSender == Vote
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
				System.out.println(msgContent.getCandidates().toString());
				msgContent.setChoice(((AgtRole) myAgent).vote(msgContent.getCandidates()));
				response.setContent(msgContent.toJson());
				response.setPerformative(ACLMessage.INFORM);
				myAgent.send(response);				
				break;
			}
		}
		else {
			message = mMessage.parseJson(msgString, mVoteResult.class);
			if(message!=null){
				mVoteResult msgContent = (mVoteResult)message;
				((AgtRole) myAgent).setLastVote(msgContent.getWhoVotesForWho());
		}
			else{
				message = mMessage.parseJson(msgString, mAction.class);
				// if msgSender == ACTION
				if(message!=null){
					mAction msgContent = (mAction)message;
					// TODO:
				}
				else {
					// TODO: advice	
				}
			}
		}
	}
}

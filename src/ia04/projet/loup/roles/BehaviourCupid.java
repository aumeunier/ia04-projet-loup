package ia04.projet.loup.roles;

import ia04.projet.loup.Global.Roles;
import ia04.projet.loup.communication.AgtVote;
import ia04.projet.loup.messages.mAction;
import ia04.projet.loup.messages.mMessage;
import ia04.projet.loup.messages.mVote;
import ia04.projet.loup.messages.mVoteResult;
import jade.lang.acl.ACLMessage;

public class BehaviourCupid extends RoleBehaviour {
	@Override
	public void roleAction(ACLMessage msg) {
		/** Waits the nightfall */
		// Message reception
		String msgString = msg.getContent();	

		mAction msgContent = (mAction)mMessage.parseJson(msgString, mAction.class);
		/** Votes for the victim of the night */
		//Debugger.println("BehaviourCupid: choose the lovers");
		if(msgContent!=null){
			if (msgContent.getRole() == Roles.CUPID){
				//Debugger.println(myAgent.getLocalName()+" received call for a Cupid action.");
				msgContent = ((AgtCupid)myAgent).selectLovers(msgContent);
				msgContent.setPerformer(myAgent.getLocalName()); 
				ACLMessage response = msg.createReply();
				response.setPerformative(ACLMessage.INFORM);
				response.setContent(msgContent.toJson());
				myAgent.send(response);
			}
		}
		else {
		}
	}
}

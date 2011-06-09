package ia04.projet.loup.roles;

import ia04.projet.loup.Global.Roles;
import ia04.projet.loup.messages.mAction;
import ia04.projet.loup.messages.mActionClairvoyant;
import ia04.projet.loup.messages.mMessage;
import jade.lang.acl.ACLMessage;

public class BehaviourClairvoyant extends RoleBehaviour {
	@Override
	public void roleAction(ACLMessage msg) {
		/** Waits the nightfall */
		// Message reception
		String msgString = msg.getContent();	

		mAction msgContent = (mAction)mMessage.parseJson(msgString, mAction.class);
		/** Votes for the victim of the night */
		//Debugger.println("BehaviourClairvoyant: sees someone s role");
		if(msgContent!=null){
			if (msgContent.getRole() == Roles.CLAIRVOYANT){
				//Debugger.println(myAgent.getLocalName()+" received call for a clairvoyant action.");
				mActionClairvoyant replyContent = ((AgtClairvoyant)myAgent).seeARole(msgContent);
				replyContent.setRole(Roles.CLAIRVOYANT);
				ACLMessage response = new ACLMessage(ACLMessage.REQUEST);
				response.setContent(replyContent.toJson());
				response.addReceiver(msg.getSender());
				myAgent.send(response);
				
			}
		}
		else {
			mActionClairvoyant msgReplyContent = (mActionClairvoyant)mMessage.parseJson(msgString, mActionClairvoyant.class);
			if(msgReplyContent!=null)
				((AgtClairvoyant)myAgent).hasSeen(msgReplyContent.getChosenPlayer(), msgReplyContent.getRole());
		}
	}
}

package ia04.projet.loup.roles;

import ia04.projet.loup.Debugger;
import ia04.projet.loup.Global.Roles;
import ia04.projet.loup.messages.mAction;
import ia04.projet.loup.messages.mMessage;
import jade.lang.acl.ACLMessage;

public class BehaviourGuardian extends RoleBehaviour {
	@Override
	public void roleAction(ACLMessage msg) {
		/** Waits the nightfall */
		// Message reception
		String msgString = msg.getContent();	

		mAction msgContent = (mAction)mMessage.parseJson(msgString, mAction.class);
		/** Votes for the victim of the night */
		//Debugger.println("BehaviourGuardian: special action : save somebody");
		if(msgContent!=null){
			if (msgContent.getRole().equals(Roles.GUARDIAN)){
				//Debugger.println(myAgent.getLocalName()+" received call for a guardian action.");
				msgContent = ((AgtGuardian)myAgent).saveSomebody(msgContent);
				msgContent.setPerformer(myAgent.getLocalName()); 
				ACLMessage response = msg.createReply();
				response.setPerformative(ACLMessage.INFORM);
				response.setContent(msgContent.toJson());
				myAgent.send(response);
			}
		}
		else {//other messages
		}
	}
}
package ia04.projet.loup.roles;

import ia04.projet.loup.Global.Roles;
import ia04.projet.loup.messages.mAction;
import ia04.projet.loup.messages.mMessage;
import jade.lang.acl.ACLMessage;

public class BehaviourWitch extends RoleBehaviour {
	@Override
	public void roleAction(ACLMessage msg) {
		/** Waits the nightfall */
		// Message reception
		String msgString = msg.getContent();	

		mAction msgContent = (mAction)mMessage.parseJson(msgString, mAction.class);
		/** Votes for the victim of the night */
		//Debugger.println("BehaviourWitch: special action : kill somebody");
		if(msgContent!=null){
			if (msgContent.getRole() == Roles.WITCH){
				//Debugger.println(myAgent.getLocalName()+" received call for a witch action.");
				msgContent.setTargetKilled(((AgtWitch)myAgent).useDeathlyPot());
				if(!((AgtWitch)myAgent).useRevivePot(msgContent.getTargetSaved()))
					msgContent.setTargetSaved(null);
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
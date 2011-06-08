package ia04.projet.loup.roles;

import ia04.projet.loup.Global.Roles;
import ia04.projet.loup.messages.mAction;
import ia04.projet.loup.messages.mMessage;
import ia04.projet.loup.messages.mPlayerDied;
import ia04.projet.loup.messages.mStartGame;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class BehaviourHunter extends RoleBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6532922686753799749L;

		
	@Override
	public void roleAction(ACLMessage msg) {
		/** Waits the nightfall */
		// Message reception
		String msgString = msg.getContent();	

		mAction msgContent = (mAction)mMessage.parseJson(msgString, mAction.class);
		/** Votes for the victim of the night */
		//Debugger.println("BehaviourHunter: special action : kill somebody");
		if(msgContent!=null){
			if (msgContent.getRole() == Roles.HUNTER){
				//Debugger.println(myAgent.getLocalName()+" received call for a guardian action.");
				msgContent = ((AgtHunter)myAgent).killSomebody(msgContent);
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

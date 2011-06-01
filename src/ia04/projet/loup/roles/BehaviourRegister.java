package ia04.projet.loup.roles;

import ia04.projet.loup.messages.mVoteRegister;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class BehaviourRegister extends Behaviour {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6789903342996110835L;

	/**
	 * Deals with common messages : beginning/end of the game, players death...
	 *  
	 * @author claquette
	 */
	@Override
	public void action() {
		/** launched at the beginning of the game */
		ACLMessage msg = new ACLMessage(ACLMessage.SUBSCRIBE);
		msg.setContent(new mVoteRegister(((AgtRole)myAgent).getRole()).toJson());
		myAgent.send(msg);
	}

	@Override
	public boolean done() {
		return true;
	}

}

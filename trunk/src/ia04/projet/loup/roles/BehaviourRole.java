package ia04.projet.loup.roles;

import ia04.projet.loup.messages.mMessage;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class BehaviourRole extends RoleBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 707900326212180696L;

	/**
	 * Deals with common messages : beginning/end of the game, players death...
	 *  
	 * @author claquette
	 */
	@Override
	public void roleAction(ACLMessage msg) {
		System.out.println("Role received message");
		if(true){
			return; // I don't want to remove the behaviours at each tick of action() ..
		}
		/** TODO if the player died, remove every behavior and add the deadBehaviour */
		for(Behaviour aBehaviour: ((AgtRole)myAgent).behaviours){
			myAgent.removeBehaviour(aBehaviour);
		}
		myAgent.addBehaviour(new BehaviourDead());
		/** TODO wait for the end of the game */
	}
}

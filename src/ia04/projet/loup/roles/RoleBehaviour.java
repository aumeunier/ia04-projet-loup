package ia04.projet.loup.roles;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class RoleBehaviour extends Behaviour {

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if(msg != null){
			((AgtRole)myAgent).dispatchMessageToBehaviours(msg);
		}
	}
	
	public void roleAction(ACLMessage msg){
		
	}

	@Override
	public boolean done() {
		return false;
	}
}

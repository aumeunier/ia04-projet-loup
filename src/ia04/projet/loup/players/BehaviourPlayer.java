package ia04.projet.loup.players;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class BehaviourPlayer extends Behaviour {

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if(msg != null){
			// Reception du message
			String msgString = msg.getContent();
			int msgPerformative = msg.getPerformative();
			// TODO:
			
			// A - Le message peut venir de l'AgtStoryteller:			
			// 1. Validation d'inscription
			// 2. Debut de partie, demande de participation
			// 3. Attribution du role 
			// 4. Eventuels messages supplementaires pendant l'attribution des roles (ex. voleur)
			// 5. Fin de partie
			
			// B - Le message peut venir d'un AgtRole
			// 1. Reception d'informations liees a stocker en bdd et reutilisables dans le futur
		}
	}

	@Override
	public boolean done() {
		return false;
	}

}

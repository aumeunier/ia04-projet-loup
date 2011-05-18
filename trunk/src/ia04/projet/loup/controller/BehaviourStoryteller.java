package ia04.projet.loup.controller;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class BehaviourStoryteller extends Behaviour {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1058995384097785392L;

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if(msg != null){
			// Reception du message
			String msgString = msg.getContent();
			int msgPerformative = msg.getPerformative();
			// TODO:
			
			// A - Le message peut venir d'un AgtPlayer:			
			// 1. Enregistrement d'un joueur aupres de myAgent
			// 2. Desinscription d'un joueur deja inscrit
			// 3. Lancement d'une partie, participation ou non du joueur a la partie
			// 4. Eventuelles reponses pendant l'attribution des roles (ex. voleur)
			// 5. Apres indication des roles, joueur previent s'il est pret
			
			// B - Le message peut venir d'un AgtCommunication (Vote / Advice / Action)
			// 1. Reponse a une action
			// 2. Reponse a un vote 
		}
	}

	@Override
	/**
	 * Our Storyteller agent will be running constantly. 
	 * Its behaviour should also be running at all times.
	 */
	public boolean done() {
		return false;
	}

}

package ia04.projet.loup.communication;

import jade.core.Agent;

public class AgtVote extends Agent {
	
	/**
	 * Possible types of vote
	 * @author pcervera
	 */
	public static enum voteType {
		KILL_PAYSAN, KILL_WW, ELECT_MAYOR, SUCCESSOR 
	}
	
	public AgtVote(){
		super();
		this.addBehaviour(new BehaviourVote());
	}
}

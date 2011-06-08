package ia04.projet.loup.roles;

import ia04.projet.loup.messages.mAction;
import jade.core.AID;

public class AgtGuardian extends AgtRole {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5298416363088874020L;
	/** last turned saved player */
	protected String lastTarget;
	
	public AgtGuardian(AID guiID) {
		super(guiID);
		addAndSaveBehaviour(new BehaviourCupid());
	}
	
	public mAction saveSomebody(mAction msgContent){ //TODO better guardian strategy
		switch (currentStrategy){
		case RABBIT:
			if(lastTarget!=this.getLocalName())
				msgContent.setTargetSaved(this.getLocalName());
			else
				msgContent.setTargetSaved(players.get(random.nextInt(players.size())));
			return msgContent;
		case BASIC:
			if(lastTarget!=this.getLocalName())
				msgContent.setTargetSaved(this.getLocalName());
			else
				msgContent.setTargetSaved(getHighestConfidence(players));
			return msgContent;
		case DUMMIE:
			if(lastTarget!=this.getLocalName())
				msgContent.setTargetSaved(this.getLocalName());
			else
				msgContent.setTargetSaved(getLowestConfidence(players));
			return msgContent;
		case SHEEP:
			if(lastVote==null || lastTarget!=this.getLocalName())
				msgContent.setTargetSaved(this.getLocalName());
			else
				msgContent.setTargetSaved(getLastLeastVoted(players, lastVote));
			return msgContent;
		default: return null;
		}
	}
}

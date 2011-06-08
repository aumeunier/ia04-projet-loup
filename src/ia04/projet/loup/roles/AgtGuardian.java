package ia04.projet.loup.roles;

import ia04.projet.loup.Global.Roles;
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
		addAndSaveBehaviour(new BehaviourGuardian());
	}
	/** initialize the role of the agent */
	protected void initializeRole(){
		role=Roles.GUARDIAN;
	}
	public mAction saveSomebody(mAction msgContent){ //TODO better guardian strategy
		switch (currentStrategy){
		case RABBIT:
			if(lastTarget!=this.getLocalName()){
				msgContent.setTargetSaved(this.getLocalName());
				this.lastTarget = getLocalName();
			}
			else {
				String targetSaved = players.get(random.nextInt(players.size()));
				msgContent.setTargetSaved(targetSaved);
				this.lastTarget = targetSaved;
			}
			return msgContent;
		case BASIC:
			if(lastTarget!=this.getLocalName()){
				msgContent.setTargetSaved(this.getLocalName());
				this.lastTarget = getLocalName();
			}
			else {
				String targetSaved = getHighestConfidence(players);
				msgContent.setTargetSaved(targetSaved);
				this.lastTarget = targetSaved;
			}
			return msgContent;
		case DUMMIE:
			if(lastTarget!=this.getLocalName()){
				msgContent.setTargetSaved(this.getLocalName());
				this.lastTarget = getLocalName();
			}
			else {
				String targetSaved = getLowestConfidence(players);
				msgContent.setTargetSaved(targetSaved);
				this.lastTarget = targetSaved;
			}
			return msgContent;
		case SHEEP:
			if(lastVote==null || lastTarget!=this.getLocalName()){
				msgContent.setTargetSaved(this.getLocalName());
				this.lastTarget = getLocalName();
			}
			else {
				String targetSaved = getLastLeastVoted(players, lastVote);
				msgContent.setTargetSaved(targetSaved);
				this.lastTarget = targetSaved;
			}
			return msgContent;
		default: return null;
		}
	}
}

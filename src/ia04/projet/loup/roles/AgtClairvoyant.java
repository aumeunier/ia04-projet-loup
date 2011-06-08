package ia04.projet.loup.roles;

import java.util.HashMap;

import ia04.projet.loup.Global.Roles;
import ia04.projet.loup.messages.mAction;
import jade.core.AID;

public class AgtClairvoyant extends AgtRole {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6010600631978031883L;
	/** Map containing the roles the clairvoyant already knows */
	protected HashMap<String, Roles> playersRole = new HashMap<String, Roles>();
	
	public AgtClairvoyant(AID guiID) {
		super(guiID);
		addAndSaveBehaviour(new BehaviourClairvoyant());
	}
	
	public mAction seeARole (mAction msgContent){
		switch (currentStrategy){
		case RABBIT:
			msgContent.setTargetSaved(players.get(random.nextInt(players.size())));
			return msgContent;
		case BASIC:
			msgContent.setTargetSaved(getLowestConfidence(players));
			return msgContent;
		case DUMMIE:
				msgContent.setTargetSaved(getHighestConfidence(players));
			return msgContent;
		case SHEEP:
			if(lastVote==null){//TODO something else
				msgContent.setTargetSaved(players.get(random.nextInt(players.size())));
			}
			else{
				msgContent.setTargetSaved(getLastMostVoted(players, lastVote));
			}
			return msgContent;
		default: return null;
		}
	}
	
	protected void setLover(String myLover, Roles hisRole){
		lover = myLover;
		confidenceLevel.get(lover).update(ConfidenceLevel.ILOVEHIM);
		playersRole.put(myLover, hisRole);
	}
}

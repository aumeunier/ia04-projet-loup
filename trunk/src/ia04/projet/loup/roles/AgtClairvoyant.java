package ia04.projet.loup.roles;

import java.util.HashMap;

import ia04.projet.loup.Global.Roles;
import ia04.projet.loup.messages.mAction;
import ia04.projet.loup.messages.mActionClairvoyant;
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
	
	public mActionClairvoyant seeARole (mAction msgContent){
		mActionClairvoyant msgReply = new mActionClairvoyant();
		switch (currentStrategy){
		case RABBIT:
			msgReply.setChosenPlayer(players.get(random.nextInt(players.size())));
			return msgReply;
		case BASIC:
			msgReply.setChosenPlayer(getLowestConfidence(players));
			return msgReply;
		case DUMMIE:
			msgReply.setChosenPlayer(getHighestConfidence(players));
			return msgReply;
		case SHEEP:
			if(lastVote==null){//TODO something else
				msgReply.setChosenPlayer(players.get(random.nextInt(players.size())));
			}
			else{
				msgReply.setChosenPlayer(getLastMostVoted(players, lastVote));
			}
			return msgReply;
		default: return null;
		}
	}

	protected void setLover(String myLover, Roles hisRole){
		lover = myLover;
		confidenceLevel.get(lover).update(ConfidenceLevel.ILOVEHIM);
		playersRole.put(myLover, hisRole);
	}
	
	protected void hasSeen(String player, Roles hisRole){
		playersRole.put(player, hisRole);
		if (hisRole == Roles.WEREWOLF)
			confidenceLevel.get(lover).update(ConfidenceLevel.ISWEREWOLF);
		else
			confidenceLevel.get(player).update(ConfidenceLevel.ISVILLAGER);
	}
}

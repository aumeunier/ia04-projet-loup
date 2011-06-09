package ia04.projet.loup.roles;

import ia04.projet.loup.Global.Roles;
import ia04.projet.loup.messages.mAction;
import ia04.projet.loup.messages.mActionClairvoyant;
import jade.core.AID;

import java.util.HashMap;

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
	/** initialize the role of the agent */
	protected void initializeRole(){
		role=Roles.CLAIRVOYANT;
	}	
	public mActionClairvoyant seeARole (mAction msgContent){
		if(human)
			return seeARoleHuman(msgContent);
		else
			return seeARoleBot(msgContent);
	}

	public mActionClairvoyant seeARoleHuman (mAction msgContent){//TODO human method
		return null;
	}

	public mActionClairvoyant seeARoleBot (mAction msgContent){
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
		confidenceLevelManager.update(lover, ConfidenceLevel.ILOVEHIM);
		playersRole.put(myLover, hisRole);
	}
	
	protected void hasSeen(String player, Roles hisRole){
		playersRole.put(player, hisRole);
		if (hisRole == Roles.WEREWOLF)
			confidenceLevelManager.update(player, ConfidenceLevel.ISWEREWOLF);
		else
			confidenceLevelManager.update(player, ConfidenceLevel.ISVILLAGER);
	}
}

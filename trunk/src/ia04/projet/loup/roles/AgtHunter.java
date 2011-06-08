package ia04.projet.loup.roles;

import ia04.projet.loup.Debugger;
import ia04.projet.loup.messages.mAction;

import jade.core.AID;

import java.util.ArrayList;

public class AgtHunter extends AgtRole{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3604198445786611966L;
	
	public AgtHunter(AID guiID) {
		super(guiID);
		addAndSaveBehaviour(new BehaviourHunter());
	}
	
	/** chooses to kill */
	public mAction killSomebody(mAction msgContent){
		if(lover!=null){ //Do not vote for your lover !
			players.remove(lover);
		}
		switch (currentStrategy){
		case RABBIT:
			msgContent.setTargetKilled(players.get(random.nextInt(players.size())));
			return msgContent;
		case BASIC:
			msgContent.setTargetKilled(getLowestConfidence(players));
			return msgContent;
		case DUMMIE:
			msgContent.setTargetKilled(getHighestConfidence(players));
			return msgContent;
		case SHEEP:
			msgContent.setTargetKilled(getLastMostVoted(players, lastVote));
			return msgContent;
		default: return null;
		}
	}
	
}

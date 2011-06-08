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
		String target;
		switch (currentStrategy){
		case RABBIT:
			target = players.get(random.nextInt(players.size()));
		case BASIC:
			target = getLowestConfidence(players);
		case DUMMIE:
			target = getHighestConfidence(players);
		case SHEEP:
			if(lastVote==null){//todo something else
				target = players.get(random.nextInt(players.size()));
			}
			else {
				target = getLastMostVoted(players, lastVote);
			}
		default: target = null;
		}
		if(lover!=null){ //Do not vote for your lover !
			players.add(lover);
		}
		msgContent.setTargetKilled(target);
		return msgContent;
	}
	
}

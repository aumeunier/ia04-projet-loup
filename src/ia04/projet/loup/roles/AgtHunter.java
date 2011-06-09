package ia04.projet.loup.roles;

import ia04.projet.loup.Debugger;
import ia04.projet.loup.Global.Roles;
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
	
	/** initialize the role of the agent */
	protected void initializeRole(){
		role=Roles.HUNTER;
	}
	
	/** chooses to kill */
	public mAction killSomebody(mAction msgContent){
		players.remove(this.getLocalName());
		if(lover!=null){ //Do not vote for your lover !
			players.remove(lover);
		}
		String target;
		switch (currentStrategy){
		case RABBIT:
			target = players.get(random.nextInt(players.size()));
			break;
		case BASIC:
			target = getLowestConfidence(players);
			break;
		case DUMMIE:
			target = getHighestConfidence(players);
			break;
		case SHEEP:
			if(lastVote==null){//todo something else
				target = players.get(random.nextInt(players.size()));
			}
			else {
				target = getLastMostVoted(players, lastVote);
			}
			break;
		default: target = null;
		}
		if(lover!=null){ //Do not vote for your lover !
			players.add(lover);
		}
		players.add(this.getLocalName());
		msgContent.setTargetKilled(target);
		Debugger.println(msgContent.toJson());
		return msgContent;
	}
	
}

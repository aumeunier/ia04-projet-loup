package ia04.projet.loup.roles;

import java.util.ArrayList;

import ia04.projet.loup.Global.Roles;
import ia04.projet.loup.messages.mAction;
import jade.core.AID;

public class AgtHunter extends AgtRole{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3604198445786611966L;
	
	public AgtHunter(AID guiID) {
		super(guiID);
		addAndSaveBehaviour(new BehaviourHunter());
		//Debugger.println("*****************agtHunter created");
	}
	
	/** initialize the role of the agent */
	protected void initializeRole(){
		role=Roles.HUNTER;
	}
	
	/** chooses to kill */
	public mAction killSomebody(mAction msgContent){
		if(human)
			return killSomebodyHuman(msgContent);
		else
			return killSomebodyBot(msgContent);
	}

	public mAction killSomebodyHuman(mAction msgContent){//TODO human method
		String choice = askGUI(new ArrayList<String>());
		msgContent.setTargetKilled(choice);
		return msgContent;
	}

	public mAction killSomebodyBot(mAction msgContent){
		System.out.println("Hunter list of choices :: "+players);
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
		return msgContent;
	}
	
}

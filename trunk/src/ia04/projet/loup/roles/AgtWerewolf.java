package ia04.projet.loup.roles;

import ia04.projet.loup.Debugger;
import ia04.projet.loup.Global;
import ia04.projet.loup.Global.Roles;
import ia04.projet.loup.messages.mVote;

import jade.core.AID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AgtWerewolf extends AgtRole {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6633110253810476936L;
	/** Map containing the last werewolf vote results */
	protected HashMap<String, mVote> lastVoteWerewolf;
	
	public AgtWerewolf (AID guiID) {
		super(guiID);
		addAndSaveBehaviour(new BehaviourWerewolf());
	}
	/** initialize the role of the agent */
	protected void initializeRole(){
		role=Roles.WEREWOLF;
	}
	/** chooses somebody to eat */
	public String eatSomebody(ArrayList<String> candidates){
		switch (currentStrategy){
		case RABBIT:
			//Debugger.println("AgtWerewofl: eatSomedy-RABBIT");
			return candidates.get(random.nextInt(candidates.size()));
		case BASIC:
			//Debugger.println(this.getLocalName()+": vote-BASIC: "+getLowestConfidence(candidates));
			return getLowestConfidence(candidates);
		case DUMMIE:
			//Debugger.println(this.getLocalName()+": vote-BASIC: "+getLowestConfidence(candidates));
			return getHighestConfidence(candidates);
		case SHEEP:
			if(lastVote==null){
				//TODO something else
					return candidates.get(random.nextInt(candidates.size()));
			}
			else{
				//Debugger.println(this.getLocalName()+": vote-SHEEP: "+getLastMostVoted(candidates,lastVote));
				return getLastMostVoted(candidates, lastVote);
			}
		default: return null;
		}
	}
	/** Update confidence levels after a WW vote */
	protected void updateConfidenceVoteWerewolf(){
		for (String elector : this.lastVoteWerewolf.keySet()) {
			if (!elector.equals(this.getLocalName())){
				/** if this isn't my vote */
				confidenceLevel.get(lastVoteWerewolf.get(elector).getChoice()).update(ConfidenceLevel.FRIENDWANTSTOEATHIM);
			}
		}
	}
	public void setLastVoteWerewolf(HashMap<String, mVote> lastVoteWW) {
		this.lastVoteWerewolf = lastVoteWW;
	}
}

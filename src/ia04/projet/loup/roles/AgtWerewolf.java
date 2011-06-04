package ia04.projet.loup.roles;

import ia04.projet.loup.Debugger;
import ia04.projet.loup.Global;
import ia04.projet.loup.Global.Roles;
import ia04.projet.loup.messages.mVote;

import java.util.ArrayList;
import java.util.Random;

public class AgtWerewolf extends AgtRole {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6633110253810476936L;
	
	public AgtWerewolf () {
		super();
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
		default: return null;
		}
	}
	/** Update confidence levels after a WW vote */
	protected void updateConfidenceVoteWerewolf(){
		for (String elector : this.lastVote.keySet()) {
			if (!elector.equals(this.getLocalName())){
				/** if this isn't my vote */
				confidenceLevel.get(lastVote.get(elector).getChoice()).update(ConfidenceLevel.FRIENDWANTSTOEATHIM);
			}
		}
	}
}

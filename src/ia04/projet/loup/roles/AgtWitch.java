package ia04.projet.loup.roles;

import ia04.projet.loup.Global.Roles;
import jade.core.AID;

public class AgtWitch extends AgtRole {
	private boolean deathlyPot = true;
	private boolean revivePot = true;
	
	public AgtWitch(AID guiID) {
		super(guiID);
		addAndSaveBehaviour(new BehaviourWitch());
	}
	
	/** initialize the role of the agent */
	protected void initializeRole(){
		role=Roles.WITCH;
	}
	
	protected String useDeathlyPot(){
		if(!deathlyPot)
			return null;
		if(lover!=null){ //Do not kill your lover !
			players.remove(lover);
		}
		String target;
		switch (currentStrategy){
		case RABBIT:
			if(random.nextBoolean()){
				target = players.get(random.nextInt(players.size()));
			}
			else 
				return null;
			break;
		case BASIC:
			if(random.nextBoolean()){
				target = getLowestConfidence(players);
			}
			else 
				return null;
			break;
		case DUMMIE:
			if(random.nextBoolean()){
				target = getHighestConfidence(players);
			}
			else
				return null;
			break;
		case SHEEP:
			if(random.nextBoolean()){
				if(lastVote==null){//todo something else
				target = players.get(random.nextInt(players.size()));
				}
				else {
					target = getLastMostVoted(players, lastVote);
				}
			}else 
				return null;
			break;
		default: target = null;
		}
		if(lover!=null){ //Do not vote for your lover !
			players.add(lover);
		}
		
		return target;
	}
	
	protected boolean useRevivePot(String dead){
		switch (currentStrategy){
		case RABBIT:
			return (random.nextBoolean());
		case BASIC:
			return (confidenceLevelManager.getLevel(dead)>60);
		case DUMMIE:
			return (confidenceLevelManager.getLevel(dead)<40);
		case SHEEP:
			if(lastVote==null){//todo something else
				return (random.nextBoolean());
			}
			else {
				return(getLastLeastVoted(players, lastVote) == dead);
			}
		default: return false;
		}
	}
}

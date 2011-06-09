package ia04.projet.loup.roles;

import java.util.ArrayList;

import ia04.projet.loup.Debugger;
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
	
	protected String useDeathlyPot(String dead){
		String result;
		if(human)
			result = useDeathlyPotHuman(dead);
		else 
			result = useDeathlyPotBot(dead);
		if(result != null)
			deathlyPot = false;
		return result;
	}
	
	protected String useDeathlyPotHuman(String dead){
		ArrayList<String> candidates = players;
		candidates.remove(dead);
		return this.askGUI(candidates);
	}

	protected String useDeathlyPotBot(String dead){
		if(!deathlyPot)
			return null;
		if(lover!=null){ //Do not kill your lover !
			players.remove(lover);
		}
		players.remove(dead);
		if (players.isEmpty()){
			if(lover!=null){ 
				players.add(lover);
				
			}
			players.add(dead);
			return null;
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
		players.add(dead);
		return target;
	}
	
	protected boolean useRevivePot(String dead){
		boolean result;
		if(human)
			result = useRevivePotHuman(dead);
		else
			result = useRevivePotBot(dead);
		if(result)
			revivePot = false;
		return result;
	}
	
	protected boolean useRevivePotHuman(String dead){
		ArrayList<String> candidates = new ArrayList<String>();
		candidates.add(dead);
		String result = this.askGUI(candidates);
		return result != null;
			
	}

	protected boolean useRevivePotBot(String dead){
		if(!revivePot)
			return false;
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

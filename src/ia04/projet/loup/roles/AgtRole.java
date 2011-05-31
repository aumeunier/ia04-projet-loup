package ia04.projet.loup.roles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.xml.bind.annotation.XmlElementDecl.GLOBAL;

import ia04.projet.loup.Global;
import ia04.projet.loup.Global.Strategies;
import ia04.projet.loup.messages.mVote;
import jade.core.AID;
import jade.core.Agent;

/**
 * This agent is the core of a role. Each role agent has to be a subclass of AgtRole.
 * It implements the main actions of a role: begining/end of a game, death of the player, etc...
 * @author claquette
 *
 */

public class AgtRole extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1226925844951644365L;
	/** role of the agent */
	protected Global.Roles role;
	/** The strategy in use */
	protected Global.Strategies currentStrategy = Strategies.RABBIT;
	/** Array of the players */
	protected java.util.List<AID> players = new ArrayList<AID>();
	/** Map of the players with the corresponding confidence level */
	protected HashMap<String, ConfidenceLevel> confidenceLevel = new HashMap<String, ConfidenceLevel>();
	/** Map containing the last vote results */
	protected HashMap<String, mVote> lastVote;
	/**
	 * The default constructor. Starts the agent and attach its behaviors (core + villager).
	 * Gets an access to his player's GUI
	 */
	public AgtRole() {
		super();
		initializeRole();
		this.addBehaviour(new BehaviourRegister());
		this.addBehaviour(new BehaviourRole());
		this.addBehaviour(new BehaviourVillager());
		initializeConfidenceLevel();
		//TODO get the GUI
	}
	/** initialize the role of the agent */
	protected void initializeRole(){
		role = Global.Roles.VILLAGER;
	}
	/**
	 * Initializes the different level of confidence at the beginning of a new game.
	 * alpha - no confidence level
	 * TODO beta2 - first implementation
	 * TODO beta4 - initial state depending on previous games
	 * 
	 */
	protected void initializeConfidenceLevel(){
		//alpha: do nothing
	}
	
	/**
	 * during the day every role has the same action to do :
	 * vote to kill somebody in the village 
	 */
	protected String vote(ArrayList<String> candidates){
		switch (currentStrategy){
		case RABBIT:
			Random random = new Random();
			return candidates.get(random.nextInt(candidates.size()));
		default: return null;
		}
	}

	protected String electMayor(ArrayList<String> candidates){
		switch (currentStrategy){
		case RABBIT:
			Random random = new Random();
			return candidates.get(random.nextInt(candidates.size()));
		default: return null;
		}
	}
	
	protected String nameSuccessor(ArrayList<String> candidates){
		switch (currentStrategy){
		case RABBIT:
			Random random = new Random();
			return candidates.get(random.nextInt(candidates.size()));
		default: return null;
		}
	}

	protected String resolveEquality(ArrayList<String> candidates){
		switch (currentStrategy){
		case RABBIT:
			Random random = new Random();
			return candidates.get(random.nextInt(candidates.size()));
		default: return null;
		}
	}
	
	protected void updateConfidenceVotePaysan(String victimName, Global.Roles victimRole){
		for (String elector : this.lastVote.keySet()) {
			if (lastVote.get(elector).getChoice().equals(this.getLocalName())){
				confidenceLevel.get(elector).update(ConfidenceLevel.VOTEFORME);
			}
			//TODO update confidence lvl according to the victim's role
		}
	}

	public void setLastVote(HashMap<String, mVote> lastVote) {
		this.lastVote = lastVote;
	}
	public Global.Roles getRole() {
		return role;
	}
	public void setRole(Global.Roles role) {
		this.role = role;
	}
}

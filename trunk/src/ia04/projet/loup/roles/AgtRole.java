package ia04.projet.loup.roles;

import ia04.projet.loup.Global;
import jade.core.Agent;

/**
 * This agent is the core of a role. Each role agent has to be a subclass of AgtRole.
 * It implements the main actions of a role: begining/end of a game, death of the player, etc...
 * @author claquette
 *
 */

public abstract class AgtRole extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1226925844951644365L;
	private Global.Strategies currentStrategy;
	/**
	 * The default constructor. Starts the agent and attach its behaviour.
	 * Gets an acces to his player's GUI
	 */
	public AgtRole() {
		super();
		this.addBehaviour(new BehaviourRole());
		initializeConfidenceLevel();
		//TODO get the GUI
	}
	
	/**
	 * Initializes the different level of confidence at the beginning of a new game.
	 * alpha - no confidence level
	 * TODO beta2 - first implementation
	 * TODO beta4 - initial state depending on previous games
	 * 
	 */
	protected void initializeConfidenceLevel(){
		
	}
	
	/**
	 * during the day every role has the same action to do :
	 * vote to kill somebody in the village 
	 */
	protected void vote(){
		switch (currentStrategy){
		case RABBIT:
			//TODO random vote
		}
	}
}

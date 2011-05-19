package ia04.projet.loup.controller;

import ia04.projet.loup.Global;
import jade.core.Agent;
import jade.util.leap.HashMap;

/**
 * This agent is the core of our program. Each game room must have its own Storyteller.
 * The storyteller in the Werewolfs of Miller's Hollow is the player who will tell the story, 
 * who knows who is who, who will call for the actions and verify that the rules are respected.
 * In our program it is the AgtStoryteller that will regulate the game.
 * @author aurelien
 *
 */
public class AgtStoryteller extends Agent {
	/**
	 * The various error codes we could have
	 * @author aurelien
	 *
	 */
	public enum GameExitErrorCodes {
		/** This would be bad. */
		UNKNOWN_REASON,
		/** This should not happening but we knew it could happen. */
		BUG,
		/** PhaseClock.startNextPhase() - should not be happening. */
		NO_POSSIBLE_PHASE,
		/** There aren't enough players to play this game right now. */
		TOO_FEW_PLAYERS,
		/** Some people won or everyone died. Either way, this game is over. */
		GAME_OVER		
	}
	private static final long serialVersionUID = -1537520826022941930L;
	/** The map of the players with their role. */
	private HashMap playersMap = new HashMap();
	/** The clock that regulates the game phases and game speed. */
	private PhaseClock phaseClock;
	
	/**
	 * The default constructor. Start an AgtStoryteller.
	 * Attach its behaviour and initialize the internal clock.
	 */
	public AgtStoryteller(){
		super();
		this.addBehaviour(new BehaviourStoryteller(this));
		this.phaseClock = new PhaseClock(this);
	}
	
	/**
	 * Method called when a new game should start
	 */
	public void startGame(){
		this.phaseClock.startTimer();
	}
	
	/**
	 * This method is called by the internal clock to know 
	 * if the phase it wants to start should be skipped
	 * @param phase The phase the clock wants to start
	 * @return whether it should be skipped or not
	 */
	public boolean shouldSkipPhase(Global.GamePhases phase){
		// TODO: dumb. We don't want to be dump, do we ?
		return (this.phaseClock.getNbOfTurns() >= 10); // A bomb explodes after 10 turns. Everyone die.
	}
	/**
	 * This method is called by the internal clock when the new phase is about to begin.
	 * @param phase The phase to begin.
	 */
	public void willStartPhase(Global.GamePhases phase){
		// TODO: lots of stuff
		System.out.print("Storyteller says: It is now the ");
		switch(phase){
		case NONE:
			break;
		case NEW_TURN:
			System.out.println("beginning of turn "+this.phaseClock.getNbOfTurns()+".");
			break;
		case DAY:
			System.out.println("day. The village wakes up.");
			break;
		case NIGHT:
			System.out.println("night. The village goes to sleep.");
			break;
		default:
			break;
		}		
	}
	/**
	 * This method is called by the internal clock when the current phase should stop.
	 * The clock is actually stopped until the Storyteller does something.
	 * @param phase The phase that should be ending.
	 */
	public void shouldEndPhase(Global.GamePhases phase){
		// TODO: lots of stuff
		switch(phase){
		case NONE:
			break;
		case DAY:
			System.out.println("Storyteller says: The day is now over.");
			break;
		case NIGHT:
			System.out.println("Storyteller says: The night is now over.");
			break;
		default:
			break;
		}
		phaseClock.startNextPhase();
	}
	/**
	 * Method called when the current game should stop. 
	 * Can be cause by an internal error or because the game is over (there is a winner or everyone is dead).
	 */
	public void endGameWithState(GameExitErrorCodes errorCode){
		this.phaseClock.stopTimer();
		switch(errorCode){
		case UNKNOWN_REASON:
			System.out.println("I don't know why the game stopped. And I fuck you all!");
			break;
		case BUG:
			System.out.println("Nyeeeee... There is a problem here. Sorry for the inconvenience =(");
			break;
		case NO_POSSIBLE_PHASE:
			System.out.println("There is no possible phase. Weird.");
			break;
		case TOO_FEW_PLAYERS:
			System.out.println("Hey guys! We need more people!");
			break;
		case GAME_OVER:
			System.out.println("Game is over! Next game in 3..2..1.... ... .. .");
			break;
		default:
			System.out.println("Laule. I don't even have an error code for that.");
			break;
		}
	}
	
	/**
	 * Prepare a message for a Communication Agent
	 * @param messageType The type of message to send (is it a vote, an advice, an action)
	 * @return
	 */
	public String createMessageForCommunicationAgent(String messageType){
		// TODO: everything
		// A - Message for Vote: ask for a vote
		// B - Message for Advice: ask for an advice turn
		// C - Message for Action: ask for the realisation of a phase/action, notify a player's death, the beginning of a game
		return "";
	}
	/**
	 * Prepare a message to send to all the registered agents
	 * @param messageType
	 * @return
	 */
	public String createMessageForRegisteredAgents(String messageType){
		// TODO: everything
		// A - Start a game
		// B - Roles attribution
		// C - End of the current game
		return "";
	}
}

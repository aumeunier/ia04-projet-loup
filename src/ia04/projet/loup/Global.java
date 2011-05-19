package ia04.projet.loup;

/**
 * This class contains variables and methods that can be used anywhere in the program
 * @author aurelien
 *
 */
public class Global {
	/**
	 * This enumeration contains the different game phases we have in the game.
	 * These phases are used to modify the player's interface.
	 * @author aurelien
	 *
	 */
	public enum GamePhases{
		/** When there is no game (between games or while starting game) */
		NONE,
		/** Beginning of a new turn */
		NEW_TURN,
		/** Beginning of the day phase */
		DAY, 
		/** Beginning of the night phase */
		NIGHT
	}
	/**
	 * This enumeration contains the roles we have implemented in our game
	 * A description of each role can be found either in its related class or on the project'swiki
	 * @author aurelien
	 *
	 */
	public enum Roles {
		
	}
}

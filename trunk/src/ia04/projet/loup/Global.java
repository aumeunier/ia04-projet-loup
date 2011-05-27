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
		/** Beginning of the night phase */
		NIGHT,
		/** Only during the first night: choose 2 people that will be lovers */
		CUPID,
		/** Only during the first night: the lovers chosen by Cupid reveal to each other their role */
		LOVERS,
		/** Only during the first night: the thief can choose his role between two (has to choose Wolf if there is one) */
		THIEF,
		/** The guardian can choose a person that won't die this turn if it's the victim*/
		GUARDIAN,
		/** The clairvoyant can see the role of a person every turn */
		CLAIRVOYANT,
		/** The were-wolves gather and pick their victim */
		WEREWOLVES,
		/** The witch can use a revive pot and a deathly pot */
		WITCH,
		/** The white wolf can kill a wolf every two turns */
		WHITE_WOLF,
		/** The raven can put two votes on a person before the turn even begins */
		RAVEN,
		/** The flute player can charm two people per day */
		FLUTE_PLAYER,
		/** The charmed players recognize each other */
		CHARMED,
		
		/** Beginning of the day phase */
		DAY, 
		/** The people who died during the night are revealed */
		VICTIMS_REVELATION,
		/** If the victims have special actions at their death */
		VICTIMS_EVENT,
		/** The victims die */
		VICTIMS_RESOLUTION,
		/** Only during the first day: the mayor is elected */
		MAYOR_ELECTION,
		/** The village select the villager to hang */
		HUNG_VOTE,
		/** The role of the person to be hung is revealed */
		HUNG_REVELATION,
		/** If the hung has a special action at his hanging... */
		HUNG_EVENT,
		/** The hung is hung */
		HUNG_RESOLUTION		
	}
	/**
	 * This enumeration contains the possible roles in the game
	 * A description of each role can be found either in its related class or on the project's wiki
	 * @author aurelien
	 *
	 */
	public enum Roles {
		/** A player who is not playing but can watch the game */
		DEAD,
		/** A player who wants to play but whose role hasn't been assigned yet */
		UNASSIGNED,
		/** Simple villager (no special action) */
		VILLAGER,
		/** Werewolf : a villager that wakes up during the night to eat somebody */
		WEREWOLF,
		/** Not implemented yet*/
		CUPID,
		THIEF,
		GUARDIAN,
		CLAIRVOYANT,
		WITCH,
		WHITE_WOLF,
		RAVEN,
		FLUTE_PLAYER,
		HUNTER,
		SCAPEGOAT,
		VILLAGE_IDIOT,
		VILLAGE_SAGE
	}
	
	/**
	 * This enumeration contains the strategies we have implemented in our game
	 * A description of each strategy can be found either in its related class or on the project's wiki
	 * @author aurelien
	 *
	 */
	public enum Strategies {
		/** Strategy used for the very beginning of the project: random behavior */
		RABBIT
	}
}

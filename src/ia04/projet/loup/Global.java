package ia04.projet.loup;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * This class contains variables and methods that can be used anywhere in the program
 * @author aurelien
 *
 */
public class Global {
	/** Is the gui opened ? Useful to test without opening guis */
	public static final boolean IS_GUI_ACTIVATED = true;
	/** The speed of the game (clock) */
	public static final float AVERAGE_SPEED = 1.0f/4.0f;
	/** The number of times we can repeat a phase before stopping it  */
	public static final int MAX_REPEATED_TIMES = 10;
	/** The name of the Storyteller agent on a platform */
	public static final String LOCALNAME_STORYTELLER = "Storyteller";
	/** The name of the Vote agent on a platform */
	public static final String LOCALNAME_VOTE = "Vote";
	/** The name of the Action agent on a platform */
	public static final String LOCALNAME_ACTION = "Action";
	/** The name of the Advice agent on a platform */
	public static final String LOCALNAME_ADVICE = "Advice";
	/** The name of the Rest agent on a platform */
	public static final String LOCALNAME_REST = "Rest";
	/** The suffix to append to the name of the Player agent to name its Role agent */
	public static final String LOCALNAME_SUFFIX_ROLE = "Role";
	/** The suffix to append to the name of the Player agent to name its Gui agent */
	public static final String LOCALNAME_SUFFIX_GUI = "Gui";
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
		WHITEWOLF,
		/** The raven can put two votes on a person before the turn even begins */
		RAVEN,
		/** The flute player can charm two people per day */
		FLUTEPLAYER,
		/** The charmed players recognize each other */
		CHARMED,
		
		/** Beginning of the day phase */
		DAY, 
		/** The people who died during the night are revealed */
		VICTIMSREVELATION,
		/** If the victims have special actions at their death */
		VICTIMSEVENT,
		/** The victims die */
		VICTIMSRESOLUTION,
		/** Only during the first day: the mayor is elected */
		MAYORELECTION,
		/** The village select the villager to hang */
		HUNGVOTE,
		/** The role of the person to be hung is revealed */
		HUNGREVELATION,
		/** If the hung has a special action at his hanging... */
		HUNGEVENT,
		/** The hung is hung */
		HUNGRESOLUTION		
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
		WHITEWOLF,
		RAVEN,
		FLUTEPLAYER,
		HUNTER,
		SCAPEGOAT,
		VILLAGEIDIOT,
		VILLAGESAGE
	}
	
	public final static HashMap<Roles, Roles> BASICROLECORRESONDANCE = new HashMap<Roles, Roles>();
	static
	{
 		BASICROLECORRESONDANCE.put(Roles.VILLAGER, 		Roles.VILLAGER);
 		BASICROLECORRESONDANCE.put(Roles.VILLAGEIDIOT, 	Roles.VILLAGER);
 		BASICROLECORRESONDANCE.put(Roles.VILLAGESAGE, 	Roles.VILLAGER);
 		BASICROLECORRESONDANCE.put(Roles.CLAIRVOYANT, 	Roles.VILLAGER);
 		BASICROLECORRESONDANCE.put(Roles.CUPID, 		Roles.VILLAGER);
 		BASICROLECORRESONDANCE.put(Roles.GUARDIAN, 		Roles.VILLAGER);
 		BASICROLECORRESONDANCE.put(Roles.HUNTER, 		Roles.VILLAGER);
 		BASICROLECORRESONDANCE.put(Roles.RAVEN, 		Roles.VILLAGER);
 		BASICROLECORRESONDANCE.put(Roles.SCAPEGOAT, 	Roles.VILLAGER);
 		BASICROLECORRESONDANCE.put(Roles.WITCH, 		Roles.VILLAGER);
 		
	 	BASICROLECORRESONDANCE.put(Roles.WEREWOLF, 		Roles.WEREWOLF);
	 	BASICROLECORRESONDANCE.put(Roles.WHITEWOLF,		Roles.WEREWOLF);
	 	
	 	BASICROLECORRESONDANCE.put(Roles.FLUTEPLAYER,	Roles.FLUTEPLAYER);
	}
	
	/**
	 * This enumeration contains the strategies we have implemented in our game
	 * A description of each strategy can be found either in its related class or on the project's wiki
	 * @author aurelien
	 *
	 */
	public enum Strategies {
		/** Strategy used for the very beginning of the project: random behavior */
		RABBIT,
		/** First real strategy, votes for the confidence lvl */
		BASIC,
		/** Exacly the opposite choice of a BASIC strategy */
		DUMMIE,
		/** Votes for the player with the highest number of votes during the last vote of the same type */
		SHEEP
	}

	public static final List<Strategies> STATEGY_VALUES =
	    Collections.unmodifiableList(Arrays.asList(Strategies.values()));

	
	public static Random random = new Random(System.currentTimeMillis());
}

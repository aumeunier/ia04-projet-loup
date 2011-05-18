package ia04.projet.loup.messages;
import ia04.projet.loup.Global;

/**
 * This class is used to create message between the Storyteller and the Player.
 * @author aurelien
 *
 */
public class mStorytellerPlayer extends mMessage {
	/**
	 * Message types we can send using this class
	 * @author aurelien
	 *
	 */
	enum mType {
		// Following are usually Conteur -> Joueur
		DIE, ACCEPT_PLAYER, END_GAME, ATTRIBUTE_ROLE, PHASE, 
		// Following are used both ways
		START_GAME,
		// Following are usually Joueur -> Conteur
		REGISTER, LEAVE_GAME
	}
	private mType type;
	private Global.Roles role;
	private Global.GamePhases phase;
	private boolean participateInGame;
	
	/**
	 * Transform the message to a human-understandable form
	 */
	@Override
	public String toString(){
		return "";
	}
	/**
	 * Message format is:
	 * {
	 * 	type: YOU_DIE, ACCEPT_PLAYER, START_GAME, ATTRIBUTE_ROLE, END_GAME, PHASE, 
	 * 			.. , REGISTER, START_GAME, LEAVE_GAME (obligatoire)
	 * 	r™le: .. (optionnel)
	 * 	phase: JOUR, NUIT (optionnel)
	 * 	value (START_GAME): YES, NO
	 * }
	 * @return The message in a JSON form
	 */
	@Override
	public String toJson(){
		return 
		"{\n" +
		"\t \"type\":"+type.toString()+",\n"+
		((role!=null)?("\t \"role\":"+role.toString()+",\n"):(""))+
		((phase!=null)?("\t \"phase\":"+phase.toString()+",\n"):(""))+
		"\t \"value\":"+String.valueOf(participateInGame)+"\n"+
		"}";
	}
}
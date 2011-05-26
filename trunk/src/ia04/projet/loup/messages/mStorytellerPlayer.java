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
	public static enum mType {
		// Following are usually Conteur -> Joueur
		DIE, ACCEPT_PLAYER, END_GAME, ATTRIBUTE_ROLE, STORYTELLING, 
		// Following are used both ways
		START_GAME,
		// Following are usually Joueur -> Conteur
		REGISTER, LEAVE_GAME
	}
	public mType type;
	public Global.Roles role;
	public Global.GamePhases phase;
	public boolean participateInGame;
	public String storyTelling;

	public mStorytellerPlayer()
	{
		super();
	}
	
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
		"\t \"type\":\""+type.toString()+"\",\n"+
		((role!=null)?("\t \"role\":\""+role.toString()+"\",\n"):(""))+
		((phase!=null)?("\t \"phase\":\""+phase.toString()+"\",\n"):(""))+
		"\t \"participateInGame\":\""+String.valueOf(participateInGame)+"\",\n"+
		"\t \"storyTelling\":\""+storyTelling+"\"\n"+
		"}";
	}
	
	
	public mType getType() {
		return type;
	}
	public void setType(mType type) {
		this.type = type;
	}
	public Global.Roles getRole() {
		return role;
	}
	public void setRole(Global.Roles role) {
		this.role = role;
	}
	public Global.GamePhases getPhase() {
		return phase;
	}
	public void setPhase(Global.GamePhases phase) {
		this.phase = phase;
	}
	public boolean isParticipateInGame() {
		return participateInGame;
	}
	public void setParticipateInGame(boolean participateInGame) {
		this.participateInGame = participateInGame;
	}
	public String getStoryTelling() {
		return storyTelling;
	}
	public void setStoryTelling(String storyTelling) {
		this.storyTelling = storyTelling;
	}
}
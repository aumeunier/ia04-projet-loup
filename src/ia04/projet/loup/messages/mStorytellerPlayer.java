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
		ACCEPT_PLAYER, ATTRIBUTE_ROLE, STORYTELLING, 
		// Following are used both ways
		START_GAME,
		// Following are usually Joueur -> Conteur
		REGISTER, LEAVE_GAME,
	}
	private mType type;
	private Global.Roles role;
	private Global.GamePhases phase;
	private boolean participateInGame;
	private String storyTelling;

	public mStorytellerPlayer()
	{
		super();
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
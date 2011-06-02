package ia04.projet.loup.messages;

import ia04.projet.loup.Global;
import ia04.projet.loup.messages.mStorytellerPlayer.mType;

public class mToGui extends mMessage {

	/**
	 * Message types we can send using this class
	 * @author Guillaume
	 *
	 */
	public static enum mType {
		// Following are usually player/gui exchange
		STATUS, PLAYERS_LIST, ROLE, LEAVE_GAME, 
		// Following are used both ways
		STORYTELLING,
		// Following are usually role/gui exchange
		VOTE_TO, 
	}
	
	private mType type;
	private Global.Roles role;
	private Global.GamePhases phase;
	private String value;
	
	public mToGui (){
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}

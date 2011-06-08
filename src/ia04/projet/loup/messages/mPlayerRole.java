package ia04.projet.loup.messages;

import ia04.projet.loup.roles.ConfidenceLevel;

public class mPlayerRole extends mMessage {
	/** Message between player and his role containing the confidence levels */
	public static enum mType {
		/** This message is used at the begining or the end of the game */
		START,END
		}
	private java.util.HashMap<String, ConfidenceLevel> confidenceLevel;
	private mType type;
	
	public mType getType() {
		return type;
	}

	public void setType(mType type) {
		this.type = type;
	}

	public mPlayerRole (){
		super();
	}

	public java.util.HashMap<String, ConfidenceLevel> getConfidenceLevel() {
		return confidenceLevel;
	}

	public void setConfidenceLevel(
			java.util.HashMap<String, ConfidenceLevel> confidenceLevel) {
		this.confidenceLevel = confidenceLevel;
	}
	
}

package ia04.projet.loup.messages;

import jade.core.AID;

public class mCommunicationRole extends mMessage {
	/**
	 * Message types we can send using this class
	 * @author aurelien
	 *
	 */
	enum mType {
		KILL_PAYSAN, KILL_WEREWOLF, ELECT_MAYOR, NAME_SUCCESSOR
	}
	private mType type;
	private int voiceNumber;
	private AID choice;
	
	public mCommunicationRole(mType _type, int _voiceNumber, AID _choice){
		this.type = _type;
		this.voiceNumber = _voiceNumber;
		this.choice = _choice;
	}
}
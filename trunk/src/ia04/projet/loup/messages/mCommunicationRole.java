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
	
	@Override
	public String toString(){
		return "";
	}
	/**
	 * Message format is:
	 * {
	 * 	type: KILL_PAYSAN, KILL_LG, ELECT_MAYOR, SUCCESSOR
	 * 	voiceNumber: number of voices the player has
	 * 	choice: aid of the player's against whom the player voted (optional, blank vote if nothing)
	 * }
	 * @return The message in a JSON form
	 */
	@Override
	public String toJson(){
		return 
		"{\n" +
		"\t \"type\":"+type.toString()+",\n"+
		"\t \"voiceNumber\":"+String.valueOf(voiceNumber)+"\n"+
		((choice!=null)?("\t \"choice\":"+choice.toString()+",\n"):(""))+
		"}";
	}
}
package ia04.projet.loup.messages;

import jade.core.AID;

public class mCommunicationRole extends Message {
	enum mType {
		KILL_PAYSAN, KILL_WEREWOLF, ELECT_MAYOR, NAME_SUCCESSOR
	}
	private mType type;
	private int nbVoix;
	private AID choice;
	
	public mCommunicationRole(mType _type, int _nbVoix, AID _choice){
		this.type = _type;
		this.nbVoix = _nbVoix;
		this.choice = _choice;
	}
	
	/**
	 * Transforme le message sous une forme compréhensible par un humain
	 */
	@Override
	public String toString(){
		return "";
	}
	/**
	 * Le message est de la forme:
	 * {
	 * 	type: KILL_PAYSAN, KILL_LG, ELECT_MAYOR, SUCCESSOR
	 * 	nbVoix: le nombre de voix dont le joueur dispose
	 * 	choix: aid du joueur pour qui on vote (optionnel, considéré comme blanc si non présent)
	 * }
	 * @return The message in a JSON form
	 */
	@Override
	public String toJson(){
		return 
		"{\n" +
		"\t \"type\":"+type.toString()+",\n"+
		"\t \"nbVoix\":"+String.valueOf(nbVoix)+"\n"+
		((choice!=null)?("\t \"role\":"+choice.toString()+",\n"):(""))+
		"}";
	}
}

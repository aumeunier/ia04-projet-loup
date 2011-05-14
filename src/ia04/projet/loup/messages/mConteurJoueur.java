package ia04.projet.loup.messages;
import ia04.projet.loup.Global;

/**
 * Cette classe est utilisée pour créer les messages entre le Conteur et le Joueur.
 * @author aurelien
 *
 */
public class mConteurJoueur extends Message {
	/**
	 * Les types de message qu'on peut envoyer via cette classe. Chaque type correspond à un 
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
	 * Transforme le message sous une forme compréhensible par un humain
	 */
	@Override
	public String toString(){
		return "";
	}
	/**
	 * Le message est de la forme:
	 * {
	 * 	type: YOU_DIE, ACCEPT_PLAYER, START_GAME, ATTRIBUTE_ROLE, END_GAME, PHASE, 
	 * 			.. , REGISTER, START_GAME, LEAVE_GAME (obligatoire)
	 * 	rôle: .. (optionnel)
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
/*
*/
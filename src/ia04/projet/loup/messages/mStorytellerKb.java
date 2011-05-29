package ia04.projet.loup.messages;

import ia04.projet.loup.Global;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is used to create message between the Storyteller its Kb Agent.
 * @author aurelien
 *
 */
public class mStorytellerKb extends mMessage {
	//TODO: comment class
	/**
	 * Message types we can send using this class
	 * @author aurelien
	 *
	 */
	public static enum mType {
		GET_ROLE, GET_GAME_COMPOSITION,
		GET_FILTER_COMPOSITION
	}
	private mType type;
	private int nbPlayers;
	private HashMap<String,ArrayList<Global.Roles>> compositions = new HashMap<String,ArrayList<Global.Roles>>();
	private ArrayList<Global.Roles> requiredRoles = new ArrayList<Global.Roles>();
	private ArrayList<Global.Roles> forbiddenRoles = new ArrayList<Global.Roles>();
	private ArrayList<Global.Roles> possibleRoles = new ArrayList<Global.Roles>();
	
	public mStorytellerKb(){
		super();
	}
	public mStorytellerKb(mType _type){
		this.type = _type;
	}
	public mStorytellerKb(mType _type, int _nbPlayers){
		this.type = _type;
		this.nbPlayers = _nbPlayers;
	}

	public void setType(mType _type){
		this.type = _type;
	}
	public void setNbPlayers(int _nbPlayers){
		this.nbPlayers = _nbPlayers;
	}
	public void setCompositions(HashMap<String,ArrayList<Global.Roles>> _compositions){
		this.compositions = _compositions;
	}
	public void setRequiredRole(ArrayList<Global.Roles> _requiredRoles){
		this.requiredRoles = _requiredRoles;
	}
	public void setForbiddenRole(ArrayList<Global.Roles> _forbiddenRoles){
		this.forbiddenRoles = _forbiddenRoles;
	}
	public void setPossibleRole(ArrayList<Global.Roles> _possibleRoles){
		this.possibleRoles = _possibleRoles;
	}
	
	public void addComposition(String compositionName, ArrayList<Global.Roles> composition){
		compositions.put(compositionName, composition);
	}
	public void addRequiredRole(Global.Roles role){
		requiredRoles.add(role);
	}
	public void addForbiddenRole(Global.Roles role){
		forbiddenRoles.add(role);
	}
	public void addPossibleRole(Global.Roles role){
		possibleRoles.add(role);
	}
	
	public mType getType(){
		return this.type;
	}
	public int getNbPlayers(){
		return this.nbPlayers;
	}
	public HashMap<String, ArrayList<Global.Roles>> getCompositions(){
		return this.compositions;
	}
	public ArrayList<Global.Roles> getRequiredRoles(){
		return this.requiredRoles;
	}
	public ArrayList<Global.Roles> getForbiddenRoles(){
		return this.forbiddenRoles;
	}
	public ArrayList<Global.Roles> getPossibleRoles(){
		return this.possibleRoles;
	}
}

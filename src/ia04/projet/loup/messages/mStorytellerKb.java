package ia04.projet.loup.messages;

import ia04.projet.loup.Global;
import ia04.projet.loup.messages.mCommunicationRole.mType;

import jade.core.AID;

import java.util.ArrayList;

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
	private ArrayList<ArrayList<Global.Roles>> compositions = new ArrayList<ArrayList<Global.Roles>>();
	private ArrayList<Global.Roles> requiredRoles = new ArrayList<Global.Roles>();
	private ArrayList<Global.Roles> forbiddenRoles = new ArrayList<Global.Roles>();
	private ArrayList<Global.Roles> possibleRoles = new ArrayList<Global.Roles>();
	
	public mStorytellerKb(){
		super();
	}
	public mStorytellerKb(mType _type){
		this.type = _type;
	}

	public void setType(mType _type){
		this.type = _type;
	}
	public void setCompositions(ArrayList<ArrayList<Global.Roles>> _compositions){
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
	
	public void addComposition(ArrayList<Global.Roles> composition){
		compositions.add(composition);
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
	public ArrayList<ArrayList<Global.Roles>> getCompositions(){
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

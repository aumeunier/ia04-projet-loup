package ia04.projet.loup.messages;

import ia04.projet.loup.roles.ConfidenceLevel;

import java.util.HashMap;

/**
 * 
 * @author paul
 */
public class mToGui extends mMessage {

	/**
	 * Message types we can send using this class
	 * @author Guillaume
	 */
	public static enum mType {
		// Following are usually player/gui exchange
		PLAYERS_LIST
	}
	
	private HashMap<String, ConfidenceLevel> confidenceLevelMap = new HashMap<String, ConfidenceLevel>();
	private mType type;
//	private Global.Roles role;
//	private String value;
	
	public static mToGui parseJson(String jsonString){
		return (mToGui)mMessage.parseJson(jsonString, mToGui.class);
	}
	
	public mToGui (){
		super();
	}

	public mType getType() {
		return type;
	}

	public void setType(mType type) {
		this.type = type;
	}

//	public Global.Roles getRole() {
//		return role;
//	}
//
//	public void setRole(Global.Roles role) {
//		this.role = role;
//	}

//	public String getValue() {
//		return value;
//	}
//
//	public void setValue(String value) {
//		this.value = value;
//	}

	/**
	 * @param confidenceLevelMap the confidenceLevelMap to set
	 */
	public void setConfidenceLevelMap(HashMap<String, ConfidenceLevel> confidenceLevelMap) {
		this.confidenceLevelMap = confidenceLevelMap;
	}

	/**
	 * @return the confidenceLevelMap
	 */
	public HashMap<String, ConfidenceLevel> getConfidenceLevelMap() {
		return confidenceLevelMap;
	}
	
	
}

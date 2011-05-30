package ia04.projet.loup.messages;

import ia04.projet.loup.Global.Roles;

/**
 * Message used for the communication between AgtStoryteller and AgtAction
 * @author paul
 */
public class mActionRequest extends mMessage {

	private Roles role;
	
	public mActionRequest(){}
	
	public mActionRequest(Roles role){
		this.setRole(role);
	}

	public static mActionRequest parseJson(String json){
		return (mActionRequest)mMessage.parseJson(json, mActionRequest.class);
	}
	
	/**
	 * @param role the role to set
	 */
	public void setRole(Roles role) {
		this.role = role;
	}

	/**
	 * @return the role
	 */
	public Roles getRole() {
		return role;
	}
}

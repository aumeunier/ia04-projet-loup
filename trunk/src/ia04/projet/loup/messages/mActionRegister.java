package ia04.projet.loup.messages;

import ia04.projet.loup.Global.Roles;

public class mActionRegister extends mMessage{

	private Roles role;
	
	public mActionRegister(){}
	
	public mActionRegister(Roles role){
		this.setRole(role);
	}

	public static mActionRegister parseJson(String json){
		return (mActionRegister)mMessage.parseJson(json, mActionRegister.class);
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


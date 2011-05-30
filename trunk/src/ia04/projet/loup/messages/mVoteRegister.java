package ia04.projet.loup.messages;

import ia04.projet.loup.Global.Roles;

public class mVoteRegister extends mMessage{

	private Roles role;
	
	public mVoteRegister(){}
	
	public mVoteRegister(Roles role){
		this.setRole(role);
	}

	public static mVoteRegister parseJson(String json){
		return (mVoteRegister)mMessage.parseJson(json, mVoteRegister.class);
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

package ia04.projet.loup.messages;

import ia04.projet.loup.Global.Roles;

public class mAction extends mMessage {

	private Roles role;
	private String target;
	private String performer;
	
	public mAction() {
	}
	public mAction(Roles role){
		this.setRole(role);
	}

	public static mAction parseJson(String json) {
		return (mAction) mMessage.parseJson(json, mAction.class);
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

	
	public void setTarget(String _target){
		this.target = _target;
	}
	
	public String getTarget(){
		return target;
	}
	
	public void setPerformer(String _performer){
		this.performer = _performer;
	}
	
	public String getPerformer(){
		return performer;
	}
}

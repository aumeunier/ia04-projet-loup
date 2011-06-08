package ia04.projet.loup.messages;

import ia04.projet.loup.Global.Roles;

public class mAction extends mMessage {

	private Roles role;
	private String targetKilled;
	private String targetSaved;
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


	public void setTargetKilled(String _target){
		this.targetKilled = _target;
	}
	
	public String getTargetKilled(){
		return targetKilled;
	}
	public void setTargetSaved(String _target){
		this.targetSaved = _target;
	}
	
	public String getTargetSaved(){
		return targetSaved;
	}
	
	public void setPerformer(String _performer){
		this.performer = _performer;
	}
	
	public String getPerformer(){
		return performer;
	}
}

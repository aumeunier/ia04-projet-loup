package ia04.projet.loup.messages;

import ia04.projet.loup.Global;

import java.util.ArrayList;

public class mPlayerDied extends mMessage {
	private boolean isOver = false;
	private String deadName;
	private Global.Roles role;
	private boolean isHungVictim = false;
	
	public Global.Roles getRole() {
		return role;
	}
	public void setRole(Global.Roles role) {
		this.role = role;
	}
	public boolean getIsOver(){
		return isOver;
	}
	public void setIsOver(Boolean state){
		this.isOver = state;
	}
	
	public String getDeadName(){
		return deadName;
	}
	public void setDeadName(String _deadName){
		this.deadName = _deadName;
	}

	public void setIsHungVictim(boolean flag){
		this.isHungVictim = flag;
	}
	public boolean getIsHungVictim(){
		return isHungVictim;
	}
		
	/**
	 * Build a mPlayerDied object by parsing a JSON string, return NULL in case of error
	 * @param jsonString
	 * @return mRunVote
	 */
	public static mPlayerDied parseJson(String jsonString){
		return (mPlayerDied)mMessage.parseJson(jsonString, mPlayerDied.class);
	}
}

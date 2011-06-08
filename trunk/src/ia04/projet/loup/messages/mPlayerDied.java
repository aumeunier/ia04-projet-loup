package ia04.projet.loup.messages;

import java.util.ArrayList;

public class mPlayerDied extends mMessage {
	private boolean isOver = false;
	private String deadName;
	private boolean isHungVictim = false;
	
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

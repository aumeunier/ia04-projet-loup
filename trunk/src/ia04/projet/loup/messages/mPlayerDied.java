package ia04.projet.loup.messages;

import java.util.ArrayList;

public class mPlayerDied extends mMessage {
	private int nbPeopleDead;
	private ArrayList<String> deadNames = new ArrayList<String>();
	
	public int getNbPeopleDead(){
		return nbPeopleDead;
	}
	public void setNbPeopleDead(int nb){
		this.nbPeopleDead = nb;
	}
	
	public ArrayList<String> getDeadNames(){
		return deadNames;
	}
	public void setDeadNames(ArrayList<String> _deadNames){
		this.deadNames = _deadNames;
	}
	public void addDead(String deadName){
		this.deadNames.add(deadName);
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

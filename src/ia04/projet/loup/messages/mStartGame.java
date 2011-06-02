package ia04.projet.loup.messages;

import jade.core.AID;

import java.util.ArrayList;

public class mStartGame extends mMessage {
	private boolean startGame;
	private ArrayList<String> localNames = new ArrayList<String>();

	public void setLocalNames(ArrayList<String> _names) {
		this.localNames = _names;
	}

	public ArrayList<String> getLocalNames() {
		return localNames;
	}
	
	public void setStartGame(boolean _start){
		this.startGame = _start;
	}
	
	public boolean getStartGame(){
		return startGame;
	}

	/**
	 * Return an instance of mVote build on a JSON string
	 * @param jsonString
	 * @return mVote
	 */
	public static mStartGame parseJson(String jsonString){
		return (mStartGame)mMessage.parseJson(jsonString, mStartGame.class);
	}

}

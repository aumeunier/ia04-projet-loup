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

}

package ia04.projet.loup.messages;

import java.util.ArrayList;
import java.util.HashMap;

public class mPlayerKb extends mMessage {

	public static enum mType{
		GET_CONFIDENCE,PUT_CONFIDENCE
	};
	
	mType type;
	ArrayList<String> players;
	HashMap<String,String> confidences;
	public mType getType() {
		return type;
	}
	public void setType(mType type) {
		this.type = type;
	}
	public ArrayList<String> getPlayers() {
		return players;
	}
	public void setPlayers(ArrayList<String> players) {
		this.players = players;
	}
	public HashMap<String, String> getConfidences() {
		return confidences;
	}
	public void setConfidences(HashMap<String, String> confidences) {
		this.confidences = confidences;
	}
	
	
}

package ia04.projet.loup.messages;

import java.util.ArrayList;

public class mGuiAction extends mMessage{
	
	private String choice;
	private ArrayList<String> candidates = new ArrayList<String>();
	
	public static mGuiAction parseJson(String jsonString){
		return (mGuiAction)mMessage.parseJson(jsonString, mGuiAction.class);
	}
	
	/**
	 * @param choice the choice to set
	 */
	public void setChoice(String choice) {
		this.choice = choice;
	}

	/**
	 * @return the choice
	 */
	public String getChoice() {
		return choice;
	}

	/**
	 * @param candidates the candidates to set
	 */
	public void setCandidates(ArrayList<String> candidates) {
		this.candidates = candidates;
	}

	/**
	 * @return the candidates
	 */
	public ArrayList<String> getCandidates() {
		return candidates;
	}
}

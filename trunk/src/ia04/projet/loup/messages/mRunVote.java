package ia04.projet.loup.messages;

import java.util.ArrayList;
import ia04.projet.loup.communication.AgtVote;

/**
 * This class is used to ask the launch of an election
 * @author pcervera
 */
public class mRunVote extends mMessage{

	/**
	 * Type of the vote to run
	 */
	private AgtVote.voteType type;
	
	/**
	 * List of the electors
	 */
	private ArrayList<String> electors;
	
	/**
	 * List of the candidates
	 */
	private ArrayList<String> candidates;
	
	/**
	 * Result of the election
	 */
	private String choice;
	
	/**
	 * Constructor by default, useful for the serialization
	 */
	public mRunVote(){}
	
	/**
	 * Constructor
	 * @param type
	 */
	public mRunVote(AgtVote.voteType type){
		this.type = type;
	}
	
	/**
	 * Build a mRunVote object by parsing a JSON string, return NULL in case of error
	 * @param jsonString
	 * @return mRunVote
	 */
	public static mRunVote parseJson(String jsonString){
		return (mRunVote)mMessage.parseJson(jsonString, mRunVote.class);
	}
	
	/**
	 * @param type
	 */
	public void setType(AgtVote.voteType type) {
		this.type = type;
	}

	/**
	 * @return
	 */
	public AgtVote.voteType getType() {
		return type;
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
	 * @param electors the electors to set
	 */
	public void setElectors(ArrayList<String> electors) {
		this.electors = electors;
	}

	/**
	 * @return the electors
	 */
	public ArrayList<String> getElectors() {
		return electors;
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

package ia04.projet.loup.messages;

import ia04.projet.loup.communication.AgtVote;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is used to create a vote message between the AgtVote and the AgtRoles
 * @author paul
 */
public class mVote extends mMessage{
	
	/** Type of the election */
	private AgtVote.voteType type;
	
	/** Number of voices Useful when the mayor votes */
	private int numbreOfVoices;
	
	/** List of the candidates */
	private ArrayList<String> candidates;
	
	/** LocalName of the chosen AgtRole */
	private String choice;
	
	/** The results of the previous election turn */
	private HashMap<String, mVote> whoVotesForWho;
	
	/**
	 * Constructor
	 */
	public mVote(){
		this.numbreOfVoices = 1;
		this.candidates = new ArrayList<String>();
	}
	
	/**
	 * Constructor
	 * @param aType
	 */
	public mVote(AgtVote.voteType aType){
		this.type = aType;
		this.numbreOfVoices = 1;
	}
	
	/**
	 * Return an instance of mVote build on a JSON string
	 * @param jsonString
	 * @return mVote
	 */
	public static mVote parseJson(String jsonString){
		return (mVote)mMessage.parseJson(jsonString, mVote.class);
	}
	
	/**
	 * @param numbreOfVoices the numbreOfVoices to set
	 */
	public void setNumbreOfVoices(int numbreOfVoices) {
		this.numbreOfVoices = numbreOfVoices;
	}

	/**
	 * @return the numbreOfVoices
	 */
	public int getNumbreOfVoices() {
		return numbreOfVoices;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(AgtVote.voteType type) {
		this.type = type;
	}

	/**
	 * @return the type
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

	/**
	 * @param whoVotesForWho the whoVotesForWho to set
	 */
	public void setWhoVotesForWho(HashMap<String, mVote> whoVotesForWho) {
		this.whoVotesForWho = whoVotesForWho;
	}

	/**
	 * @return the whoVotesForWho
	 */
	public HashMap<String, mVote> getWhoVotesForWho() {
		return whoVotesForWho;
	}
}

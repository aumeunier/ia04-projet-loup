package ia04.projet.loup.messages;

/**
 * This class is used to create a vote message between the AgtVote and the AgtRoles
 * @author paul
 */
public class mVote extends mMessage{
	
	public static enum mType {
		KILL_PAYSAN, KILL_WW, ELECT_MAYOR, SUCCESSOR 
	}
	
	/**
	 * Type of the election
	 */
	private mType type;
	
	/**
	 * Number of voices
	 * Useful when the mayor votes
	 */
	private int numbreOfVoices;
	
	/**
	 * LocalName of the chosen AgtRole
	 */
	private String choice;
	
	/**
	 * Constructor
	 */
	public mVote(){
		this.numbreOfVoices = 1;
	}
	
	/**
	 * Constructor
	 * @param aType
	 */
	public mVote(mType aType){
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
	public void setType(mType type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public mType getType() {
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
	
}

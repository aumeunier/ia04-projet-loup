package ia04.projet.loup.messages;

import java.util.HashMap;

import ia04.projet.loup.communication.AgtVote;

public class mVoteResult extends mMessage{
	
	/** Type of the election */
	private AgtVote.voteType type;
	
	/** To differentiate from the mVote message during serialization */
	private boolean isFinalElection;
	
	/** The results of the previous election turn */
	private HashMap<String, mVote> whoVotesForWho;
	
	/** The final choice */
	private String choiceResult;

	/**
	 *  Transform the Json String into a mResultVote instance
	 * @param jsonString
	 * @return mResultVote
	 */
	public static mVoteResult parseJson(String jsonString){
		return (mVoteResult)mMessage.parseJson(jsonString, mVoteResult.class);
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
	
	public void setIsFinalElection(boolean flag){
		this.isFinalElection = flag;
	}
	
	public boolean getIsFinalElection(){
		return isFinalElection;
	}
	
	public void setChoiceResult(String choice){
		this.choiceResult = choice;
	}
	public String getChoiceResult(){
		return choiceResult;
	}
}

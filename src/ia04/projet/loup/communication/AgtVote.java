package ia04.projet.loup.communication;

import ia04.projet.loup.Debugger;
import ia04.projet.loup.Global;
import ia04.projet.loup.Global.Roles;
import ia04.projet.loup.messages.mPlayerDied;
import ia04.projet.loup.messages.mStartGame;
import ia04.projet.loup.messages.mVote;
import ia04.projet.loup.messages.mVoteResult;
import ia04.projet.loup.messages.mVoteRun;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

/**
 * Agent which handles all the elections
 * 
 * @author paul
 */
public class AgtVote extends Agent {

	private static final long serialVersionUID = 1L;

	/** Possible types of vote */
	public static enum voteType {
		VOTE_PAYSAN, VOTE_WW, ELECT_MAYOR, SUCCESSOR, EQUALITY
	}

	/** Map of the registered players */
	private HashMap<AID, Roles> playersMap = new HashMap<AID, Roles>();

	/** AID of the Mayor */
	private AID mayor;

	/** AID of the StoryTeller */
	private AID storyTeller;

	/** Who votes for who */
	private HashMap<AID, mVote> whoVotesForWho = new HashMap<AID, mVote>();

	/** Result of the previous election */
	private HashMap<String, Integer> lastElectionResult = new HashMap<String, Integer>();

	/** Last elected person */
	private mVoteRun lastVote = null;

	/** Number of electors who haven't vote yet */
	private int remainingVotes;

	/**
	 * Constructor
	 */
	public AgtVote() {
		super();
		this.addBehaviour(new BehaviourVote(this));
	}

	/**
	 * Registers its service into the DF
	 */
	public void registerServiceToDf(){
		/*
		// FIXME: DFInterface problems...
		ServiceDescription sd = new ServiceDescription();
		sd.setType("AgtVote");
		sd.setName(this.getLocalName());
		DFInterface.registerService(this, sd);
		System.out.println("AgtVote registered to DF");
		 */
	}

	/**
	 * Run an election
	 */
	public void election(mVoteRun runVote, boolean newVote) {
		mVote aVote = new mVote(runVote.getType());
		ACLMessage voteMessage = new ACLMessage(ACLMessage.REQUEST);

		if (newVote) {
			this.whoVotesForWho = new HashMap<AID, mVote>();
			this.lastVote = runVote;
			this.lastElectionResult.clear();
		} else {
			HashMap<String, mVote> temp = new HashMap<String, mVote>();
			for (Entry<AID, mVote> entry : this.whoVotesForWho.entrySet()) {
				mVote tempVote = entry.getValue();
				tempVote.setWhoVotesForWho(null);
				temp.put(entry.getKey().getLocalName(), tempVote);
			}
			aVote.setWhoVotesForWho(temp);
		}

		this.remainingVotes = 0;

		switch (runVote.getType()) {
		case VOTE_PAYSAN: {
			for (Entry<AID, Roles> entry : this.playersMap.entrySet()) {
				AID aid = entry.getKey();
				lastElectionResult.put(aid.getLocalName(), 0); // ?
				if(entry.getValue() != Roles.DEAD){
					aVote.getCandidates().add(aid.getLocalName());
					this.remainingVotes++;
				}
				voteMessage.addReceiver(aid);
			}
		}	break;
		case ELECT_MAYOR: {
			for (Entry<AID, Roles> entry : this.playersMap.entrySet()) {
				AID aid = entry.getKey();
				if(entry.getValue() != Roles.DEAD){
					aVote.getCandidates().add(aid.getLocalName());
					lastElectionResult.put(aid.getLocalName(), 0);
					this.remainingVotes++;
				}
				voteMessage.addReceiver(aid);
			}
		}	break;
		case VOTE_WW:{
			for (Entry<AID, Roles> entry : this.playersMap.entrySet()) {
				AID aid = entry.getKey();
				if (entry.getValue() == Global.Roles.WEREWOLF) {
					voteMessage.addReceiver(aid);
					this.remainingVotes++;
				}
				else if(entry.getValue() != Roles.DEAD){
					aVote.getCandidates().add(aid.getLocalName());
					lastElectionResult.put(aid.getLocalName(), 0);
				}
			}
		}	break;
		case SUCCESSOR:{
			for (Entry<AID, Roles> entry : this.playersMap.entrySet()) {
				AID aid = entry.getKey();
				Roles role = entry.getValue();
				if(!aid.getLocalName().equals(mayor.getLocalName())){
					if(role != Roles.DEAD){
						aVote.getCandidates().add(aid.getLocalName());
					}
				}				
			}
			this.remainingVotes++;
			voteMessage.addReceiver(mayor);
			break;
		}
		default:
			break;
		}

		voteMessage.setContent(aVote.toJson());
		this.send(voteMessage);
	}

	/*
	 * Add a vote to the currentElection
	 */
	public void addVote(AID aid, mVote aVote) {	
		if (remainingVotes < 0)
			Debugger.println("Should Never Happen: More votes than expected.");
		else {
			this.whoVotesForWho.put(aid, aVote);	
			this.remainingVotes--;
			if (remainingVotes == 0) {
				this.calculateResults();

				if (uniqueWinner(lastElectionResult)) {
					endOfVote(lastElectionResult);
				} else {
					// There is no unique winner
					switch(lastVote.getType()){
					// If it is a vote to kill a were wolf, the mayor will choose one among the winners
					case VOTE_PAYSAN : 
						tieATie();
						break;
					// If it is a vote to elect the mayor (first turn), choose randomly
					case ELECT_MAYOR:
						tieRandomly();
						break;
					// Otherwise, another turn will be run
					default: 
						election(lastVote, false);
						break;
					}
				}
			}
		}
	}

	/**
	 * Ask the mayor to tie a tie
	 */
	private void tieATie() {
		mVote tieVote = new mVote();
		tieVote.setType(AgtVote.voteType.EQUALITY);
		tieVote.setCandidates(this.getWinners());

		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		message.setContent(tieVote.toJson());
		message.addReceiver(this.mayor);
		this.send(message);
	}
	
	/**
	 * The tie is broken randomly among the ones with the biggest number of voices
	 */
	private void tieRandomly(){
		ArrayList<String> winners = getWinners();
		Random r = new Random(Global.random.nextLong());
		String winner = winners.get(r.nextInt(winners.size()));	
		this.mayor = new AID(winner, AID.ISLOCALNAME); 	
		this.informElectorsFinalResult(winner, lastVote.getType());	
	}

	private ArrayList<String> getWinners(){
		ArrayList<String> winners = new ArrayList<String>();
		int maxVote = 0;

		for (String aCandidates : lastElectionResult.keySet()) {
			if (lastElectionResult.get(aCandidates) > maxVote) {
				maxVote = lastElectionResult.get(aCandidates);
				winners.clear();
				winners.add(aCandidates);
			}
			else if(lastElectionResult.get(aCandidates) == maxVote){
				winners.add(aCandidates);
			}
		}

		return winners;
	}

	/*
	 * Calculate the results of this last election
	 */
	private void calculateResults() {
		for (Entry<AID, mVote> entry : this.whoVotesForWho.entrySet()) {
			int previousScore = lastElectionResult.get(entry.getValue().getChoice());
			lastElectionResult.put(entry.getValue().getChoice(), previousScore
					+ entry.getValue().getNumbreOfVoices());
		}
	}

	/**
	 * Check if there is an unique winner
	 * 
	 * @param voteResults
	 * @return boolean
	 */
	private boolean uniqueWinner(HashMap<String, Integer> voteResults) {
		int maxVote = 0;
		int nbMaxVote = 0;

		for (String aCandidates : voteResults.keySet()) {
			if (voteResults.get(aCandidates) > maxVote) {
				maxVote = voteResults.get(aCandidates);
				nbMaxVote = 1;
			}
			else if(voteResults.get(aCandidates) == maxVote){
				nbMaxVote++;
			}
		}

		return (nbMaxVote==1);
	}

	/**
	 * Return the localName of the last elected agent
	 * 
	 * @return String
	 */
	private void endOfVote(HashMap<String, Integer> voteResults) {
		int maxVote = 0;
		String winner = null;

		/* Determine who was elected */
		for (String aCandidates : voteResults.keySet()) {
			if (voteResults.get(aCandidates) > maxVote) {
				maxVote = voteResults.get(aCandidates);
				winner = aCandidates;
			}
		}
		
		switch(lastVote.getType()){
		case ELECT_MAYOR: 
			this.mayor = new AID(winner, AID.ISLOCALNAME); 
			break;
		}

		this.informElectorsFinalResult(winner, lastVote.getType());
	}

	/**
	 * Get the choice of the mayor in case of equality in the vote of the paysans
	 * @param aVote
	 */
	public void endOfEquality(mVote aVote) {
		int oldScore = this.lastElectionResult.get(aVote.getChoice());
		this.lastElectionResult.put(aVote.getChoice(), oldScore + 1);

		this.informElectorsFinalResult(aVote.getChoice(), voteType.EQUALITY);
	}
	
	/**
	 * Get the choice of the mayor regarding his successor
	 * @param aVote 
	 */
	public void endOfSuccessor(mVote aVote){
		this.mayor = new AID(aVote.getChoice(), AID.ISLOCALNAME); 
		this.informAllFinalResult(mayor.getLocalName(),	voteType.SUCCESSOR);
	}

	/**
	 * Inform everyone of the final results
	 * @param winner
	 */
	private void informAllFinalResult(String winner, voteType type){
		/* Inform the electors of the final result */
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		mVoteResult aResultVote = new mVoteResult();
		aResultVote.setType(type);
		aResultVote.setIsFinalElection(true);
		aResultVote.setChoiceResult(winner);
		for (Entry<AID, Roles> entry : this.playersMap.entrySet()) {
			message.addReceiver(entry.getKey());
		}
		message.setContent(aResultVote.toJson());
		this.send(message);

		/* Inform StoryTeller of the final result */
		message = new ACLMessage(ACLMessage.INFORM);
		this.lastVote.setChoice(winner.replace(Global.LOCALNAME_SUFFIX_ROLE, ""));
		message.setContent(this.lastVote.toJson());
		message.addReceiver(this.storyTeller);
		this.send(message);
	}

	/**
	 * Inform the electors the final results
	 * @param winner
	 */
	private void informElectorsFinalResult(String winner, voteType type){
		/* Inform the electors of the final result */
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		mVoteResult aResultVote = new mVoteResult();
		aResultVote.setType(type);
		aResultVote.setIsFinalElection(true);

		HashMap<String, mVote> temp = new HashMap<String, mVote>();
		for (Entry<AID, mVote> entry : this.whoVotesForWho.entrySet()) {
			temp.put(entry.getKey().getLocalName(), entry.getValue());
		}
		aResultVote.setWhoVotesForWho(temp);
		aResultVote.setChoiceResult(winner);

		message.setContent(aResultVote.toJson());
		for (Entry<AID, mVote> entry : this.whoVotesForWho.entrySet()) {
			message.addReceiver(entry.getKey());
		}

		this.send(message);

		/* Inform StoryTeller of the final result */
		message = new ACLMessage(ACLMessage.INFORM);
		this.lastVote.setChoice(winner.replace(Global.LOCALNAME_SUFFIX_ROLE, ""));
		message.setContent(this.lastVote.toJson());
		message.addReceiver(this.storyTeller);
		this.send(message);
	}

	/** 
	 * Send the start message to the role agents with the names of the other players
	 */
	public void startGame(){
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		mStartGame mContent = new mStartGame();
		mContent.setStartGame(true);
		ArrayList<String> names = new ArrayList<String>();
		for(AID aid: this.playersMap.keySet()){
			names.add(aid.getLocalName());
			message.addReceiver(aid);
		}
		mContent.setLocalNames(names);
		message.setContent(mContent.toJson());
		this.send(message);
	}

	/**
	 * Method called when one or several players have been killed.
	 * The agent needs to purge its playersMap role and notify the other players about his death.
	 * @param deathMessage the mPlayerDied sent to this agent
	 */
	public void deaths(mPlayerDied deathMessage){
		// Change the player's role in the map
		String deadPlayer = deathMessage.getDeadName();
		AID deathAid = new AID(deadPlayer,AID.ISLOCALNAME);
		this.playersMap.put(deathAid, Global.Roles.DEAD);

		// Notify the other players
		/*
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setContent(deathMessage.toJson());
		for(AID aid: playersMap.keySet()){
			if(aid != deathAid){
				message.addReceiver(aid);
			}
		}
		this.send(message);
		 */
	}
	/**
	 * Add a player to the playerMap
	 */
	public void addPlayer(AID aid, Roles role) {
		this.playersMap.put(aid, role);
	}

	// /////////////////////////////// GETTERS & SETTERS
	// ///////////////////////////////////////////
	// /**
	/**
	 * @param lastVote
	 *            the lastVote to set
	 */
	public void setLastVote(mVoteRun lastVote) {
		this.lastVote = lastVote;
	}

	/**
	 * @return the lastVote
	 */
	public mVoteRun getLastVote() {
		return lastVote;
	}

	/**
	 * @param playersMap
	 *            the playersMap to set
	 */
	public void setPlayersMap(HashMap<AID, Roles> playersMap) {
		this.playersMap = playersMap;
	}

	/**
	 * @return the playersMap
	 */
	public HashMap<AID, Roles> getPlayersMap() {
		return playersMap;
	}

	/**
	 * @param storyTeller
	 *            the storyTeller to set
	 */
	public void setStoryTeller(AID storyTeller) {
		this.storyTeller = storyTeller;
	}

	/**
	 * @return the storyTeller
	 */
	public AID getStoryTeller() {
		return storyTeller;
	}
}

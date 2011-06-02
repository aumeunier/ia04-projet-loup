package ia04.projet.loup.communication;

import ia04.projet.loup.DFInterface;
import ia04.projet.loup.Debugger;
import ia04.projet.loup.Global;
import ia04.projet.loup.Global.Roles;
import ia04.projet.loup.messages.mVote;
import ia04.projet.loup.messages.mVoteResult;
import ia04.projet.loup.messages.mVoteRun;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;
import java.util.Map.Entry;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Agent which handles all the elections
 * 
 * @author paul
 */
public class AgtVote extends Agent {

	private static final long serialVersionUID = 1L;

	/** Possible types of vote */
	public static enum voteType {
		VOTE_PAYSAN, VOTE_WW, ELECT_MAYOR, SUCCESSOR
	}

	/** Map of the registered players */
	private HashMap<AID, Roles> playersMap = new HashMap<AID, Roles>();

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
		// TODO: DFInterface problems...
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
		} else {
			HashMap<String, mVote> temp = new HashMap<String, mVote>();
			for (Entry<AID, mVote> entry : this.whoVotesForWho.entrySet()) {
				temp.put(entry.getKey().getLocalName(), entry.getValue());
			}
			aVote.setWhoVotesForWho(temp);
		}

		this.remainingVotes = 0;

		switch (runVote.getType()) {
		case VOTE_PAYSAN:
			for (Entry<AID, Roles> entry : this.playersMap.entrySet()) {
				AID aid = entry.getKey();
				aVote.getCandidates().add(aid.getLocalName());
				lastElectionResult.put(aid.getLocalName(), 0);
				voteMessage.addReceiver(aid);
				this.remainingVotes++;
			}

			break;
		case VOTE_WW:
			for (Entry<AID, Roles> entry : this.playersMap.entrySet()) {
				AID aid = entry.getKey();
				if (entry.getValue() == Global.Roles.WEREWOLF) {
					voteMessage.addReceiver(aid);
					Debugger.println("Wolf added:"+aid.getLocalName());
					this.remainingVotes++;
				} else {
					aVote.getCandidates().add(aid.getLocalName());
					lastElectionResult.put(aid.getLocalName(), 0);
				}
			}

			break;
		default:
			throw new NotImplementedException();
		}

		voteMessage.setContent(aVote.toJson());
		this.send(voteMessage);
	}

	/*
	 * Add a vote to the currentElection
	 */
	public void addVote(AID aid, mVote aVote) {
		Debugger.println("Vote received:"+aVote.toJson());		
		if (remainingVotes < 0)
			Debugger.println("Should Never Happened: More votes than expected.");
		else {
			this.whoVotesForWho.put(aid, aVote);
			this.remainingVotes--;
			if (remainingVotes == 0) {
				this.calculateResults();

				if (uniqueWinner(lastElectionResult)) {
					endOfVote(lastElectionResult);
				} else {
					election(lastVote, false);
				}
			}
		}
	}

	private void calculateResults() {
		Debugger.println("Results"+this.whoVotesForWho.toString());
		for (Entry<AID, mVote> entry : this.whoVotesForWho.entrySet()) {
			int previousScore = lastElectionResult.get(entry.getValue()
					.getChoice());
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

		for (String aCandidates : voteResults.keySet()) {
			if (voteResults.get(aCandidates) > maxVote) {
				maxVote = voteResults.get(aCandidates);
			} else {
				if (voteResults.get(aCandidates) > maxVote && maxVote != 0) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Return the localName of the last elected agent
	 * 
	 * @return String
	 */
	private void endOfVote(HashMap<String, Integer> voteResults) {
		int maxVote = 0;
		String winner = null;

		/* Determines who was elected */
		for (String aCandidates : voteResults.keySet()) {
			if (voteResults.get(aCandidates) > maxVote) {
				maxVote = voteResults.get(aCandidates);
				winner = aCandidates;
			}
		}

		/* Inform the electors of the final result */
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		mVoteResult aResultVote = new mVoteResult();
		aResultVote.setType(lastVote.getType());

		HashMap<String, mVote> temp = new HashMap<String, mVote>();
		for (Entry<AID, mVote> entry : this.whoVotesForWho.entrySet()) {
			temp.put(entry.getKey().getLocalName(), entry.getValue());
		}
		aResultVote.setWhoVotesForWho(temp);

		message.setContent(aResultVote.toJson());
		for (Entry<AID, mVote> entry : this.whoVotesForWho.entrySet()) {
			message.addReceiver(entry.getKey());
		}

		this.send(message);

		/* Inform StoryTeller of the final result */
		message = new ACLMessage(ACLMessage.INFORM);
		this.lastVote.setChoice(winner);
		message.setContent(this.lastVote.toJson());
		message.addReceiver(this.storyTeller);
		this.send(message);
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

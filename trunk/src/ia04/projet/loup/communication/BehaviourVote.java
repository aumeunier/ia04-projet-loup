package ia04.projet.loup.communication;

import ia04.projet.loup.Debugger;
import ia04.projet.loup.messages.mRunVote;
import ia04.projet.loup.messages.mVote;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ControllerException;

import java.util.ArrayList;
import java.util.HashMap;

public class BehaviourVote extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	private HashMap<String, Integer> lastVoteResults = null;
	private mRunVote lastVote = null;

	public void action() {
		ACLMessage startMessage = myAgent.receive();
		if (startMessage != null) {
			String strContent = startMessage.getContent();
			mRunVote runVote = mRunVote.parseJson(strContent);
			if (runVote != null) {
				// Initialize the arrays if not done, to avoid NullPointerExceptions later
				if(runVote.getCandidates() == null){
					runVote.setCandidates(new ArrayList<String>());
				}
				if(runVote.getElectors() == null){
					runVote.setElectors(new ArrayList<String>());
				}
				
				// Message treatment
				Debugger.println("Starting election of type: "+ runVote.getType().toString());
				this.lastVoteResults = election(runVote);
				Debugger.println("End of the election of type: "+ runVote.getType().toString());
				this.setLastVote(runVote);
				Debugger.println("Winner is: "+ this.getWinner());
				runVote.setChoice(this.getWinner());
				
				// Answer
				ACLMessage reply = startMessage.createReply();
				reply.setContent(runVote.toJson());
				myAgent.send(reply);
			}
		}
	}

	/**
	 * Handles the vote to kill a paysan
	 * 
	 * @param runVote
	 */
	private HashMap<String, Integer> election(mRunVote runVote) {
		mVote aVote = new mVote(runVote.getType());
		aVote.setCandidates(runVote.getCandidates());

		HashMap<String, Integer> voteResults = new HashMap<String, Integer>();

		/*
		 * Fill the Hashmap containing the result, with all the candidates
		 */
		for (int i = 0; i < runVote.getCandidates().size(); i++) {
			voteResults.put(runVote.getCandidates().get(i), 0);
		}

		for (int i = 0; i < runVote.getElectors().size(); i++) {

			ACLMessage aMessage = new ACLMessage();
			Agent r;
			try {
				r = (Agent) myAgent.getContainerController().getAgent(
						runVote.getElectors().get(i));
				aMessage.addReceiver(r.getAID());
				aMessage.setContent(aVote.toJson());
				aMessage.setPerformative(ACLMessage.REQUEST);
				myAgent.send(aMessage);

				ACLMessage answerMessage = myAgent.blockingReceive();
				String answerContent = answerMessage.getContent();

				mVote answerVote = mVote.parseJson(answerContent);
				voteResults.put(answerVote.getChoice(),
						voteResults.get(answerVote.getChoice()) + 1);

			} catch (ControllerException e) {
				e.printStackTrace();
			}
		}

		if (uniqueWinner(voteResults)) {
			return voteResults;
		}

		return election(runVote);

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
	private String getWinner() {
		int maxVote = 0;
		String winner = null;

		for (String aCandidates : this.lastVoteResults.keySet()) {
			if (this.lastVoteResults.get(aCandidates) > maxVote) {
				maxVote = this.lastVoteResults.get(aCandidates);
				winner = aCandidates;
			}
		}
		return winner;
	}

	/**
	 * @param lastVote
	 *            the lastVote to set
	 */
	public void setLastVote(mRunVote lastVote) {
		this.lastVote = lastVote;
	}

	/**
	 * @return the lastVote
	 */
	public mRunVote getLastVote() {
		return lastVote;
	}
}

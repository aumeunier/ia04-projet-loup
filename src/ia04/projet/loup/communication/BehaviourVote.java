package ia04.projet.loup.communication;

import java.util.ArrayList;
import java.util.HashMap;

import ia04.projet.loup.messages.mRunVote;
import ia04.projet.loup.messages.mVote;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ControllerException;
import jade.core.Agent;

public class BehaviourVote extends CyclicBehaviour {

	private HashMap<String, Integer> voteResults = null;
	private mRunVote runVote = null;
	
	public void action(){
		ACLMessage startMessage = myAgent.receive();
		if (startMessage != null) {
			String strContent = startMessage.getContent();
			runVote = mRunVote.parseJson(strContent);
			if(runVote != null){
				switch(runVote.getType()){
					case KILL_PAYSAN: this.voteKillPaysan(); break;
					case KILL_WW: break;
					case ELECT_MAYOR: break;
					case SUCCESSOR: break;
				}
			}
		}
	}
	
	/**
	 * Handles the vote to kill a paysan
	 * @param runVote
	 */
	private void voteKillPaysan(){
		mVote aVote = new mVote(AgtVote.voteType.KILL_PAYSAN);
		aVote.setCandidates(runVote.getCandidates());
		
		voteResults = new HashMap<String, Integer>();
		
		for(int i=0; i<runVote.getCandidates().size(); i++){
			voteResults.put(runVote.getCandidates().get(i), 0);
		}
		
		for(int i=0; i<runVote.getElectors().size(); i++){
			
			ACLMessage aMessage = new ACLMessage();
			Agent r;
			try {
				r = (Agent) myAgent.getContainerController().getAgent(runVote.getElectors().get(i));
				aMessage.addReceiver(r.getAID());
				aMessage.setContent(aVote.toJson());
				aMessage.setPerformative(ACLMessage.REQUEST);
				myAgent.send(aMessage);
				
				ACLMessage answerMessage = myAgent.blockingReceive();
				String answerContent = answerMessage.getContent();
				mVote answerVote = mVote.parseJson(answerContent);
				voteResults.put(answerVote.getChoice(), voteResults.get(answerVote.getChoice()));
				
			} catch (ControllerException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @TODO
	 * TRAITER LES CASE AU IL YA DES CANDIDATES AVEC LES LE MEME DE NOMBRE DE VOIX
	 * @return the last elected agent
	 */
	private String getWinner(){
		int maxVote = 0;
		ArrayList<String> winners = new ArrayList<String>();
		
		for(int i =0; i<this.runVote.getCandidates().size(); i++){
			if(this.voteResults.get(this.runVote.getCandidates().get(i)) > maxVote){
				maxVote = this.voteResults.get(this.runVote.getCandidates().get(i));
				winners.clear();
				winners.add(this.runVote.getCandidates().get(i));
			}else if(this.voteResults.get(this.runVote.getCandidates().get(i)) == maxVote){
				winners.add(this.runVote.getCandidates().get(i));
			}
		}
		
		return null;
	}
}

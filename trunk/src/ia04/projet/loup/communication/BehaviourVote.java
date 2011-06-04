package ia04.projet.loup.communication;

import ia04.projet.loup.Debugger;
import ia04.projet.loup.messages.mPlayerDied;
import ia04.projet.loup.messages.mStartGame;
import ia04.projet.loup.messages.mVote;
import ia04.projet.loup.messages.mVoteRegister;
import ia04.projet.loup.messages.mVoteRun;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage; 

public class BehaviourVote extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	private AgtVote agtVote = null;

	public BehaviourVote(Agent _myAgent) {
		super(_myAgent);
		this.agtVote = (AgtVote) myAgent;
	}

	public void action() {
		ACLMessage startMessage = myAgent.receive();
		if (startMessage != null) {
			switch (startMessage.getPerformative()) {
			case ACLMessage.SUBSCRIBE:
				mVoteRegister aVoteRegister = mVoteRegister.parseJson(startMessage.getContent());
				this.agtVote.addPlayer(startMessage.getSender(),
						aVoteRegister.getRole());
				break;
			case ACLMessage.REQUEST:
				mVoteRun runVote = mVoteRun.parseJson(startMessage.getContent());
				if (runVote != null) {
					this.agtVote.setStoryTeller(startMessage.getSender());
					this.agtVote.election(runVote, true);
				}
				break;
			case ACLMessage.INFORM:
				mVote aVote = mVote.parseJson(startMessage.getContent());
				if (aVote != null) {
					switch(aVote.getType()){
						case EQUALITY: 
							this.agtVote.endOfEquality(aVote); 
							break;
						case SUCCESSOR:
							this.agtVote.endOfSuccessor(aVote);
							break;
						default: 
							this.agtVote.addVote(startMessage.getSender(), aVote); 
							break;
					}
				}
				else {
					mPlayerDied aDeath = mPlayerDied.parseJson(startMessage.getContent());
					if(aDeath != null){
						this.agtVote.deaths(aDeath);
					}
					else {
						mStartGame generalMessage = mStartGame.parseJson(startMessage.getContent());
						if(generalMessage != null){
							this.agtVote.startGame();
						}
					}
				}
				break;
			}
		}
	}
}

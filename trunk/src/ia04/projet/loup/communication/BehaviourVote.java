package ia04.projet.loup.communication;

import ia04.projet.loup.messages.mRunVote;
import ia04.projet.loup.messages.mVote;
import ia04.projet.loup.messages.mVoteRegister;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class BehaviourVote extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	private AgtVote agtVote = null;

	public BehaviourVote() {
		this.agtVote = (AgtVote) myAgent;
	}

	public void action() {
		ACLMessage startMessage = myAgent.receive();
		if (startMessage != null) {
			switch (startMessage.getPerformative()) {
			case ACLMessage.SUBSCRIBE:
				mVoteRegister aVoteRegister = mVoteRegister
						.parseJson(startMessage.getContent());
				this.agtVote.addPlayer(startMessage.getSender(),
						aVoteRegister.getRole());
				break;
			case ACLMessage.REQUEST:
				mRunVote runVote = mRunVote
						.parseJson(startMessage.getContent());
				if (runVote != null) {
					this.agtVote.setStoryTeller(startMessage.getSender());
					this.agtVote.election(runVote, true);
				}
				break;
			case ACLMessage.INFORM:
				mVote aVote = mVote.parseJson(startMessage.getContent());
				if (aVote != null) {
					this.agtVote.addVote(startMessage.getSender(), aVote);
				}
				break;
			}
		}
	}
}

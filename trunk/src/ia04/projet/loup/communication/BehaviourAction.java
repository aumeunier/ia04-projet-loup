package ia04.projet.loup.communication;

import ia04.projet.loup.messages.mAction;
import ia04.projet.loup.messages.mActionRegister;
import ia04.projet.loup.messages.mActionRequest;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class BehaviourAction extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;

	/** My AgtAction */
	private AgtAction agtAction;

	/**
	 * Constructor
	 */
	public BehaviourAction(AgtAction _myAgent){
		super(_myAgent);
		this.agtAction = (AgtAction)myAgent;
	}

	public void action() {
		ACLMessage message = agtAction.receive();
		if(message!=null){
			switch(message.getPerformative()){
			case ACLMessage.SUBSCRIBE: 
				mActionRegister anActionRegister = mActionRegister.parseJson(message.getContent());
				if(anActionRegister != null)
					this.agtAction.addPlayer(message.getSender(), anActionRegister.getRole());
				break;
			case ACLMessage.REQUEST: 
				mAction anActionRequest = mAction.parseJson(message.getContent());
				if(anActionRequest != null){
					this.agtAction.setAgtStoryteller(message.getSender());
					this.agtAction.performAction(anActionRequest);
				}
				break;
			case ACLMessage.INFORM: 
				mAction anAction = mAction.parseJson(message.getContent());
				if(anAction != null){
					this.agtAction.addAction(anAction, message.getSender());
				}
				break;
			}
		}
	}
}

package ia04.projet.loup.players;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;


public class AgtPlayer extends Agent {
	
	private static final long serialVersionUID = -3215896432211766320L;

	public AgtPlayer(){
		super();
		this.addBehaviour(new BehaviourPlayer());
	}
	
	public void Register(AID stryTeller){
		//Send identification to storyTeller
		String Jmsg ="{type : REGISTER}";
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setContent(Jmsg);
		msg.addReceiver(stryTeller);
		this.send(msg);
	}
	
	public void JoinGame(ACLMessage msg, String value){
		//Accept to join the beginning game
		ACLMessage response = msg.createReply();
		String Jmsg = "{type : REGISTER , value :" + value +"}";
		response.setContent(Jmsg);
		this.send(response);
	}

	public void RoleInstance(){
		//RoleAgent instanciation
		
	}
	
}
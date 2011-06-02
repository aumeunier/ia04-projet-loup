package ia04.projet.loup.players;

import ia04.projet.loup.messages.mMessage;
import ia04.projet.loup.messages.mPlayerRole;
import ia04.projet.loup.messages.mStorytellerPlayer;
import ia04.projet.loup.messages.mToGui.mType;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.StaleProxyException;

public class BehaviourPlayer extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Wait messages then acts in consequence
	 * @author Guillaume
	 * */
	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if(msg != null){
			// Message reception
			String msgString = msg.getContent();
			if(msg.getSender()== ((AgtPlayer) myAgent).getRoleID())
			{
				// B - Message can come from an AgtRole
				// 1. Reception of information concerning the db (stats, can be reused in future games)
				
				mPlayerRole msgObj = (mPlayerRole) mMessage.parseJson(msgString, mPlayerRole.class);
				System.out.println("reçu" + msgObj.type);
			} else {
				// C - Message can come from an AgtStoryteller:			
				// 1. Inscription validation
				// 2. Beginning of a game, participation request
				// 3. Role attribution
				// 4. Eventual additional messages concerning the role attribution
				// 5. End of the game

				mStorytellerPlayer msgObj = (mStorytellerPlayer) mMessage.parseJson(msgString, mStorytellerPlayer.class);
				switch(msgObj.getType()){
				case START_GAME :
					((AgtPlayer)myAgent).JoinGame(msg, msgObj);
					break;
				case ATTRIBUTE_ROLE :
					try {
						((AgtPlayer) myAgent).RoleInstance(msgObj.getRole());
						ACLMessage reply = msg.createReply();
						reply.setContent(msgObj.toJson());
						myAgent.send(reply);
					} catch (StaleProxyException e) {
						e.printStackTrace();
					}
					((AgtPlayer) myAgent).TransfertToGui(mType.ROLE,msgObj.getRole().toString());
					break;
				case STORYTELLING :
					((AgtPlayer) myAgent).TransfertToGui(mType.STORYTELLING,msgObj.getStoryTelling());
					break;
				case DIE:
					break;
				default : break;
				}			
			}

		}
	}
	
	/** This is a cyclic behavior
	 * */
	@Override
	public boolean done() {
		return false;
	}

}

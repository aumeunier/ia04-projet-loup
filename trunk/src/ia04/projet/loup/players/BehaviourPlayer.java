package ia04.projet.loup.players;

import ia04.projet.loup.Global;
import ia04.projet.loup.messages.mMessage;
import ia04.projet.loup.messages.mPlayerDied;
import ia04.projet.loup.messages.mPlayerRole;
import ia04.projet.loup.messages.mStorytellerPlayer;
import jade.core.AID;
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
			// B - Message can come from an AgtRole
			// 1. Reception of information concerning the db (stats, can be reused in future games)

			mPlayerRole msgRole = (mPlayerRole) mMessage.parseJson(msgString, mPlayerRole.class);
			if(msgRole!=null){

			}
			else {
				mPlayerDied msgDied = (mPlayerDied) mMessage.parseJson(msgString, mPlayerDied.class);
				if(msgDied!=null){
					boolean isDone = msgDied.getIsOver();
					if(isDone){
						// Role finished the dying stuff, notify AgtStoryteller
						ACLMessage msgForRole = new ACLMessage(ACLMessage.INFORM);
						msgForRole.setContent(msgDied.toJson());
						AID aidStoryteller = new AID(Global.LOCALNAME_STORYTELLER,AID.ISLOCALNAME);
						msgForRole.addReceiver(aidStoryteller);
						myAgent.send(msgForRole);
					}
					else {
						// Start doing the cleaning do to death, notify AgtRole
						ACLMessage msgForRole = new ACLMessage(ACLMessage.INFORM);
						msgForRole.setContent(msgDied.toJson());
						msgForRole.addReceiver(((AgtPlayer) myAgent).getRoleID());
						myAgent.send(msgForRole);
					}
				} 
				else {
					// C - Message can come from an AgtStoryteller:			
					// 1. Inscription validation
					// 2. Beginning of a game, participation request
					// 3. Role attribution
					// 4. Eventual additional messages concerning the role attribution
					// 5. End of the game

					mStorytellerPlayer msgObj = (mStorytellerPlayer) mMessage.parseJson(msgString, mStorytellerPlayer.class);
					if(msgObj!=null){
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
							break;
						case STORYTELLING :
							//((AgtPlayer) myAgent).StoryTransfertToGui(msg);
							break;
						default : break;
						}			
					}
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

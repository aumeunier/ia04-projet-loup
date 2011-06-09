package ia04.projet.loup.players;

import ia04.projet.loup.Global;
import ia04.projet.loup.messages.mMessage;
import ia04.projet.loup.messages.mPlayerDied;
import ia04.projet.loup.messages.mPlayerRole;
import ia04.projet.loup.messages.mStorytellerPlayer;
import ia04.projet.loup.messages.mToGui;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.StaleProxyException;

public class BehaviourPlayer extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;

	/**
	 * Wait messages then acts in consequences
	 * 
	 * @author Guillaume
	 * */
	@Override
	public void action() {
		
		mPlayerRole msgRole = null;
		mPlayerDied msgDied = null;
		mStorytellerPlayer msgStoryTeller = null;
		mToGui msgToGui = null;
		
		ACLMessage msg = myAgent.receive();
		if (msg != null) {
			String msgContent = msg.getContent();
			// B - Message can come from an AgtRole
			// 1. Reception of information concerning the db (stats, can be
			// reused in future games)

			if ((msgRole = (mPlayerRole) mMessage.parseJson(msgContent,mPlayerRole.class)) != null) {
				handleMsgRole(msgRole);
			}else if(null != (msgDied = (mPlayerDied) mMessage.parseJson(msgContent, mPlayerDied.class))) {
				handleMsgDied(msgDied);
			} else if((msgStoryTeller = (mStorytellerPlayer) mMessage.parseJson(msgContent, mStorytellerPlayer.class))!= null) {
				handleMsgStoryTeller(msgStoryTeller, msg);
			} else if((msgToGui = mToGui.parseJson(msgContent)) != null){
				handleMsgGuiLevelConfidence(msgToGui);
			}
		}
	}

	/**
	 * @param msgGuiConfidenceLevel
	 */
	private void handleMsgGuiLevelConfidence(mToGui msgToGui) {
		((AgtPlayer) myAgent).setConfidenceLevel(msgToGui.getConfidenceLevelMap());
	}

	/**
	 * @todo TO COMPLETE
	 * @param msgRole
	 */
	private void handleMsgRole(mPlayerRole msgRole){
		
	}
	
	
	/**
	 * @param msgStoryTeller
	 * @param msg
	 */
	private void handleMsgStoryTeller(mStorytellerPlayer msgStoryTeller, ACLMessage msg) {
		// C - Message can come from an AgtStoryteller:
		// 1. Inscription validation
		// 2. Beginning of a game, participation request
		// 3. Role attribution
		// 4. Eventual additional messages concerning the role
		// attribution
		// 5. End of the game

		switch (msgStoryTeller.getType()) {
			case START_GAME:
				((AgtPlayer) myAgent).JoinGame(msg, msgStoryTeller);
				break;
			case ATTRIBUTE_ROLE:
				try {
					((AgtPlayer) myAgent).RoleInstance(msgStoryTeller.getRole());
					ACLMessage reply = msg.createReply();
					reply.setContent(msgStoryTeller.toJson());
					//((AgtPlayer) myAgent).TransfertToGui(mToGui.mType.ROLE, msgStoryTeller.getRole());
					((AgtPlayer) myAgent).setRole(msgStoryTeller.getRole());
					//((AgtPlayer) myAgent).TransfertToGui(mToGui.mType.STATUS, "ALIVE");
					((AgtPlayer) myAgent).setStat("ALIVE");
					myAgent.send(reply);
				} catch (StaleProxyException e) {
					e.printStackTrace();
				}
				break;
			case STORYTELLING:
				//((AgtPlayer) myAgent).TransfertToGui(mToGui.mType.STORYTELLING,msgStoryTeller.getStoryTelling());
				((AgtPlayer) myAgent).setStoryView(msgStoryTeller.getStoryTelling());
				break;
			case END_GAME:
//				((AgtPlayer) myAgent).TransfertToGui(mToGui.mType.STORYTELLING, msgStoryTeller.getStoryTelling());
				((AgtPlayer) myAgent).setStoryView(msgStoryTeller.getStoryTelling());
				break;
			default:
				break;
		}
	}

	/**
	 * @param msgDied
	 */
	private void handleMsgDied(mPlayerDied msgDied) {
		boolean isDone = msgDied.getIsOver();
		if (isDone) {
			// Role finished the dying stuff, notify AgtStoryteller
			ACLMessage msgForRole = new ACLMessage(ACLMessage.INFORM);
			msgForRole.setContent(msgDied.toJson());
			AID aidStoryteller = new AID(Global.LOCALNAME_STORYTELLER, AID.ISLOCALNAME);
			msgForRole.addReceiver(aidStoryteller);
			myAgent.send(msgForRole);
		} else {
			// Start doing the cleaning do to death, notify AgtRole
			ACLMessage msgForRole = new ACLMessage(ACLMessage.INFORM);
			msgForRole.setContent(msgDied.toJson());
			msgForRole.addReceiver(((AgtPlayer) myAgent).getRoleID());
			myAgent.send(msgForRole);
			((AgtPlayer) myAgent).setStat("MORT");
		}
	}
}

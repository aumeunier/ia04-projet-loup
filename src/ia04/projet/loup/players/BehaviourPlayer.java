package ia04.projet.loup.players;

import ia04.projet.loup.Global;
import ia04.projet.loup.messages.mGuiAction;
import ia04.projet.loup.messages.mMessage;
import ia04.projet.loup.messages.mPlayerDied;
import ia04.projet.loup.messages.mPlayerKb;
import ia04.projet.loup.messages.mPlayerKb.mType;
import ia04.projet.loup.messages.mStartGame;
import ia04.projet.loup.messages.mStorytellerPlayer;
import ia04.projet.loup.messages.mToGui;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.StaleProxyException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BehaviourPlayer extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;

	/**
	 * Wait messages then acts in consequences
	 * 
	 * @author Guillaume
	 * */
	@Override
	public void action() {
		
		mStartGame msgRole = null;
		mPlayerDied msgDied = null;
		mPlayerKb msgPlayList = null;
		mStorytellerPlayer msgStoryTeller = null;
		mToGui msgToGui = null;
		mGuiAction msgGuiAction = null;
		
		ACLMessage msg = myAgent.receive();
		if (msg != null) {
			String msgContent = msg.getContent();
			// B - Message can come from an AgtRole
			// 1. Reception of information concerning the db (stats, can be
			// reused in future games)

			if ((msgRole = (mStartGame) mMessage.parseJson(msgContent,mStartGame.class)) != null) {
				handleMsgRole(msgRole);
			}else if(null != (msgPlayList = (mPlayerKb) mMessage.parseJson(msgContent, mPlayerKb.class))) {
				handleMsgKb(msgPlayList);
			}else if(null != (msgDied = (mPlayerDied) mMessage.parseJson(msgContent, mPlayerDied.class))) {
				handleMsgDied(msgDied);
			} else if((msgStoryTeller = (mStorytellerPlayer) mMessage.parseJson(msgContent, mStorytellerPlayer.class))!= null) {
				try {
					handleMsgStoryTeller(msgStoryTeller, msg);
				} catch (StaleProxyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if((msgToGui = mToGui.parseJson(msgContent)) != null){
				handleMsgGuiLevelConfidence(msgToGui);
			}else if((msgGuiAction = mGuiAction.parseJson(msgContent)) != null){
				handleMsgGuiAction(msgGuiAction);
			}
		}
	}

	private void handleMsgGuiAction(mGuiAction msgGuiAction) {
		((AgtPlayer) myAgent).waitForAction();
	}

	private void handleMsgKb(mPlayerKb msgPlayList) {
		HashMap<String,String> list = msgPlayList.getConfidences();
		HashMap<String,Integer> confLvl =((AgtPlayer)myAgent).getConfidences();
		for(Map.Entry<String, String> e : list.entrySet()){
			if(e.getValue().equals("Newcomer")){confLvl.put(e.getKey(), 45);}
			else if(e.getValue().equals("Betrayer")){confLvl.put(e.getKey(), 5);}
			else if(e.getValue().equals("Trustworthy")){confLvl.put(e.getKey(), 80);}
			else if(e.getValue().equals("Friend")){confLvl.put(e.getKey(), 65);}
			else if(e.getValue().equals("Dislike")){confLvl.put(e.getKey(), 30);}
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
	private void handleMsgRole(mStartGame msgRole){
		ArrayList<String> players = msgRole.getLocalNames();
		mPlayerKb msgTrans = new mPlayerKb();
		msgTrans.setPlayers(players);
		msgTrans.setType(mType.GET_CONFIDENCE);
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setContent(msgTrans.toJson());
		msg.addReceiver(((AgtPlayer)myAgent).getMyKB());
		myAgent.send(msg);
	}
	
	
	/**
	 * @param msgStoryTeller
	 * @param msg
	 * @throws IOException 
	 * @throws StaleProxyException 
	 */
	private void handleMsgStoryTeller(mStorytellerPlayer msgStoryTeller, ACLMessage msg) throws IOException, StaleProxyException {
		// C - Message can come from an AgtStoryteller:
		// 1. Inscription validation
		// 2. Beginning of a game, participation request
		// 3. Role attribution
		// 4. Eventual additional messages concerning the role
		// attribution
		// 5. End of the game

		switch (msgStoryTeller.getType()) {
			case START_GAME:
				AgtKBPlayer newKb = new AgtKBPlayer(myAgent.getLocalName());
				((AgtPlayer) myAgent).getContainerController().acceptNewAgent(newKb.getAgtName()+"KB", newKb).start();
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

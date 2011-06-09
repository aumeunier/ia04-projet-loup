package ia04.projet.loup.roles;

import ia04.projet.loup.Debugger;
import ia04.projet.loup.messages.mActionLover;
import ia04.projet.loup.messages.mMessage;
import ia04.projet.loup.messages.mPlayerDied;
import ia04.projet.loup.messages.mStartGame;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class BehaviourRole extends RoleBehaviour {

	private static final long serialVersionUID = 707900326212180696L;

	/**
	 * Deals with common messages : beginning/end of the game, players death...
	 *  
	 * @author claquette
	 */
	@Override
	public void roleAction(ACLMessage msg) {
		String msgString = msg.getContent();

		ACLMessage response = msg.createReply();
		mMessage message = mMessage.parseJson(msgString, mStartGame.class);
		// if msgder == Vote
		if(message!=null){
			//Debugger.println(myAgent.getLocalName().replace("Role", ""));
			//TODO Send a message with the list roles localnames to the player
			msg.clearAllReceiver();
			msg.addReceiver(new AID(myAgent.getLocalName().replace("Role", ""), AID.ISLOCALNAME));
			myAgent.send(msg);
			mStartGame msgContent = (mStartGame)message;
			((AgtRole)myAgent).players=msgContent.getLocalNames();
		} else {
			message = mMessage.parseJson(msgString, mPlayerRole.class);
			if()for(String player : msgContent.getLocalNames()){
				((AgtRole)myAgent).confidenceLevelManager.put(player, new ConfidenceLevel(((AgtRole)myAgent).random.nextInt(10)));
			}
		} else {
			
			message = mMessage.parseJson(msgString, mPlayerDied.class);
			if(message != null){
				mPlayerDied msgcontent = mPlayerDied.parseJson(msgString);
				if(msgcontent != null){
					if(msgcontent.getRole()==null)
						((AgtRole)myAgent).iAmDead(msg);
					else{ 
						((AgtRole)myAgent).confidenceLevelManager.remove(msgcontent.getDeadName());
						((AgtRole)myAgent).players.remove(msgcontent.getDeadName());
						if(msgcontent.getIsHungVictim())
						((AgtRole)myAgent).updateConfidenceVotePaysan(msgcontent.getDeadName(), msgcontent.getRole());
					}
				}
			}
			else {
				message = mMessage.parseJson(msgString, mActionLover.class);
				if(message != null) { 
					mActionLover msgcontent = (mActionLover)message;
					if(msgcontent.getLover1()!=myAgent.getLocalName()){
						((AgtRole)myAgent).setLover(msgcontent.getLover1(),msgcontent.getLover1Role());
					}
					else {
						((AgtRole)myAgent).setLover(msgcontent.getLover2(),msgcontent.getLover2Role());
					}
				}
			}
		}
		/** TODO wait for the end of the game */

	}
}

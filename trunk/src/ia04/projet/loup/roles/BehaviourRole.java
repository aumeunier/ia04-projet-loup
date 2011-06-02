package ia04.projet.loup.roles;

import ia04.projet.loup.messages.mMessage;
import ia04.projet.loup.messages.mPlayerDied;
import ia04.projet.loup.messages.mStartGame;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

public class BehaviourRole extends RoleBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 707900326212180696L;

	/**
	 * Deals with common messages : beginning/end of the game, players death...
	 *  
	 * @author claquette
	 */
	@Override
	public void roleAction(ACLMessage msg) {
		String msgString = msg.getContent();

		/** TODO Checks the source of the message */
		//if( msgSender == ACTION || ADVICE || VOTE)
		ACLMessage response = msg.createReply();
		mMessage message = mMessage.parseJson(msgString, mStartGame.class);
		// if msgSender == Vote
		if(message!=null){
			mStartGame msgContent = (mStartGame)message;
			for(String player : msgContent.getLocalNames()){
				((AgtRole)myAgent).confidenceLevel.put(player, new ConfidenceLevel());
			}
		} else {
			message = mMessage.parseJson(msgString, mPlayerDied.class);
			if(message != null){
				mPlayerDied msgcontent = mPlayerDied.parseJson(msgString);
				if(msgcontent != null){
					((AgtRole)myAgent).iAmDead(msg);
				}
			}
		}
		/** TODO wait for the end of the game */

	}
}

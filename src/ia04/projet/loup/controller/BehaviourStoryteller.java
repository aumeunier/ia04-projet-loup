package ia04.projet.loup.controller;

import ia04.projet.loup.Global.Roles;
import ia04.projet.loup.messages.mMessage;
import ia04.projet.loup.messages.mStorytellerCommunication;
import ia04.projet.loup.messages.mStorytellerPlayer;
import ia04.projet.loup.messages.mVote;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class BehaviourStoryteller extends Behaviour {
	private static final long serialVersionUID = 1058995384097785392L;

	public BehaviourStoryteller(AgtStoryteller a){
		super(a);
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if(msg != null){
			// Message reception
			String msgString = msg.getContent();

			// A - Message can come from an AgtPlayer:
			mMessage generalMessage = mMessage.parseJson(msgString, mStorytellerPlayer.class);
			if(generalMessage != null){
				mStorytellerPlayer message = (mStorytellerPlayer)generalMessage;
				// 1. Player's registration
				if(message.type.equals(mStorytellerPlayer.mType.REGISTER)) {
					System.out.println(msg.getSender().getLocalName()+" registered.");
					((AgtStoryteller)(this.myAgent)).addPlayerToParty(msg.getSender());
				}
				
				// 2. Player's unregistration
				else if(message.type.equals(mStorytellerPlayer.mType.LEAVE_GAME)) {
					//TODO:
				}
				
				// 3. Participation in the new game
				else if(message.type.equals(mStorytellerPlayer.mType.START_GAME)) {
					if(message.participateInGame){
						System.out.println(msg.getSender().getLocalName()+" wants to participate in the game.");
						((AgtStoryteller)(this.myAgent)).playerWantsToParticipate(msg.getSender());						
					}
					else {
						System.out.println(msg.getSender().getLocalName()+" doesn't want to participate in the game.");
						((AgtStoryteller)(this.myAgent)).playersDoesntParticipate(msg.getSender());
					}
				}
				
				// 4. Eventual answers to role attribution
				// 5. Is the player ready to play (has finished role initialization)
				else if(message.type.equals(mStorytellerPlayer.mType.ATTRIBUTE_ROLE)) {		
					//TODO:	answers from role attribution (ex: Thief)
					System.out.println(msg.getSender().getLocalName()+" now has its role assigned:"+message.role+".");
					((AgtStoryteller)(this.myAgent)).addRoleToPlayer(message.role, msg.getSender());
				}

			}
			
			// B - Message can come from an AgtCommunication (Vote / Advice / Action)
			else {
				generalMessage = mMessage.parseJson(msgString, mStorytellerCommunication.class);
				// 1. Answer to an action
				if(generalMessage !=null){
					//TODO:
				}
				
				// 2. Answer to a vote
				else {
					generalMessage = mMessage.parseJson(msgString, mVote.class);
					//TODO:
				}
			}

		}
	}

	@Override
	/**
	 * Our Storyteller agent will be running constantly. 
	 * Its behaviour should also be running at all times.
	 */
	public boolean done() {
		return false;
	}

}

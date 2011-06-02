package ia04.projet.loup.controller;

import ia04.projet.loup.Debugger;
import ia04.projet.loup.messages.mMessage;
import ia04.projet.loup.messages.mVoteRun;
import ia04.projet.loup.messages.mStorytellerKb;
import ia04.projet.loup.messages.mStorytellerPlayer;
import ia04.projet.loup.messages.mStorytellerPlayer.mType;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class BehaviourStoryteller extends Behaviour {
	private static final long serialVersionUID = 1058995384097785392L;

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if(msg != null){
			// Message reception
			String msgString = msg.getContent();

			// A - Message can come from an AgtPlayer:
			AgtStoryteller myAgt = ((AgtStoryteller)(this.myAgent));
			mMessage generalMessage = mMessage.parseJson(msgString, mStorytellerPlayer.class);
			if(generalMessage != null){
				mStorytellerPlayer message = (mStorytellerPlayer)generalMessage;
				switch(message.getType()){

				// 1. Player's registration
				case REGISTER:
					Debugger.println(msg.getSender().getLocalName()+" registered.");
					myAgt.addPlayerToParty(msg.getSender());
					break;

				// 2. Player's unregistration
				case LEAVE_GAME:
					Debugger.println(msg.getSender().getLocalName()+" unregistered.");
					myAgt.removePlayerFromParty(msg.getSender());
					// TODO: notify everyone that agent died
					// TODO: be sure it won't crash
					break;

				// 3. Participation in the new game
				case START_GAME:
					if(message.isParticipateInGame()){
						Debugger.println(msg.getSender().getLocalName()+" wants to participate in the game.");
						myAgt.playerWantsToParticipate(msg.getSender());						
					}
					else {
						Debugger.println(msg.getSender().getLocalName()+" doesn't want to participate in the game.");
						myAgt.playerDoesntParticipate(msg.getSender());
					}
					break;

				// 4. Eventual answers to role attribution
				// 5. Is the player ready to play (has finished role initialization)
				case ATTRIBUTE_ROLE:
					//TODO:	answers from role attribution (ex: Thief)
					Debugger.println(msg.getSender().getLocalName()+" now has its role assigned:"+message.getRole()+".");
					myAgt.addRoleToPlayer(message.getRole(), msg.getSender());
					break;
				}
			}

			// B - Message can come from an AgtKbStoryteller
			else {
				generalMessage = mMessage.parseJson(msgString, mStorytellerKb.class);
				if(generalMessage != null){
					Debugger.println("Received answer from Kb agent");
					mStorytellerKb message = (mStorytellerKb)generalMessage;
					switch(message.getType()){
					// Get the list of roles registered in the ontology
					case GET_ROLE:
						System.out.println(message.getPossibleRoles().toString());
						break;

					// If we wanted to get the possible compositions given a number of players, we choose one
					case GET_FILTER_COMPOSITION:	
					case GET_GAME_COMPOSITION:
						myAgt.chooseConfiguration(message.getCompositions(),message.getNbPlayers());
						break;				
					}
				}

				// C - Message can come from an AgtVote
				else {
					generalMessage = mMessage.parseJson(msgString, mVoteRun.class);
					if(generalMessage !=null){
						mVoteRun message = (mVoteRun)generalMessage;
						Debugger.println(message.toJson());
						switch(message.getType()){
						case VOTE_PAYSAN: {
							if(message.getChoice()!=null){
								AID victimAid = new AID(message.getChoice(), AID.ISLOCALNAME);
								myAgt.addVictim(victimAid);
							}
						}	break;
						case VOTE_WW:{
							if(message.getChoice()!=null){
								AID victimAid = new AID(message.getChoice(), AID.ISLOCALNAME);
								myAgt.addVictim(victimAid);
							}
						}	break;
						case ELECT_MAYOR:
							//TODO: notify all the roles about who the new mayor is
							//TODO: agent storyteller will be able to nextPhase when clock ticks
							break;
						case SUCCESSOR:
							//TODO: notify all the roles about who the new mayor is
							break;
						}
					}

					// D - Message can come from an AgtAction
					else {
						generalMessage = mMessage.parseJson(msgString, mVoteRun.class);
						//TODO:

						if(generalMessage!=null){

						}

						// E - Message can come from an AgtAdvice
						else {
							generalMessage = mMessage.parseJson(msgString, mVoteRun.class);
							//TODO:

							if(generalMessage!=null){

							}
						}
					}
				}
			}
		}
		// TODO: order the A B C D such that the most frequent messages go first
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

package ia04.projet.loup.controller;

import ia04.projet.loup.Debugger;
import ia04.projet.loup.Global.Roles;
import ia04.projet.loup.messages.mAction;
import ia04.projet.loup.messages.mMessage;
import ia04.projet.loup.messages.mPlayerDied;
import ia04.projet.loup.messages.mStorytellerKb;
import ia04.projet.loup.messages.mStorytellerPlayer;
import ia04.projet.loup.messages.mVoteRun;
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
			else {
				// Player is dead and has finished releasing his role
				mPlayerDied msgDied = (mPlayerDied) mMessage.parseJson(msgString, mPlayerDied.class);
				if(msgDied!=null){
					((AgtStoryteller)myAgent).playerFinishedBeingKilled();
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
							switch(message.getType()){
							case VOTE_PAYSAN: {
								if(message.getChoice()!=null){
									AID victimAid = new AID(message.getChoice(), AID.ISLOCALNAME);
									myAgt.paysansEndedWithChoice(victimAid);
								}
								else {
									myAgt.paysansEndedWithChoice(null);
								}
							}	break;
							case VOTE_WW:{
								if(message.getChoice()!=null){
									AID victimAid = new AID(message.getChoice(), AID.ISLOCALNAME);
									myAgt.werewolvesEndedWithChoice(victimAid);
								}
								else {
									myAgt.werewolvesEndedWithChoice(null);
								}
							}	break;
							case ELECT_MAYOR:
								if(message.getChoice()!=null){
									AID mayorAid = new AID(message.getChoice(), AID.ISLOCALNAME);
									myAgt.mayorElectionEndedWithChoice(mayorAid);
								}
								else {
									myAgt.mayorElectionEndedWithChoice(null);
								}
								break;
							case SUCCESSOR:
								if(message.getChoice()!=null){
									AID mayorAid = new AID(message.getChoice(), AID.ISLOCALNAME);
									myAgt.mayorElectionEndedWithChoice(mayorAid);
								}
								else {
									myAgt.mayorElectionEndedWithChoice(null);
								}
								break;
							}
						}

						// D - Message can come from an AgtAction
						else {
							generalMessage = mMessage.parseJson(msgString, mAction.class);
							if(generalMessage!=null){
								mAction message = (mAction)generalMessage;
								String performerName = message.getPerformer();
								AID performer = null;
								if(performerName != null) {
									new AID(performerName,AID.ISLOCALNAME);
								}
								String targetKilledName = message.getTargetKilled();
								AID targetKilled = null;
								if(targetKilledName!=null){
									targetKilled = new AID(targetKilledName,AID.ISLOCALNAME);
								}
								String targetSavedName = message.getTargetSaved();
								AID targetSaved = null;
								if(targetSavedName!=null){
									targetSaved = new AID(targetSavedName,AID.ISLOCALNAME);
								}
								Roles role = message.getRole();
								myAgt.actionDone(performer, targetKilled, targetSaved, role);								
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

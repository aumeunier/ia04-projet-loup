package ia04.projet.loup.controller;

import ia04.projet.loup.Global;
import ia04.projet.loup.Global.GamePhases;
import ia04.projet.loup.Global.Roles;
import ia04.projet.loup.messages.mStorytellerCommunication;
import ia04.projet.loup.messages.mStorytellerPlayer;
import ia04.projet.loup.players.AgtPlayer;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * This agent is the core of our program. Each game room must have its own Storyteller.
 * The storyteller in the Werewolfs of Miller's Hollow is the player who will tell the story, 
 * who knows who is who, who will call for the actions and verify that the rules are respected.
 * In our program it is the AgtStoryteller that will regulate the game.
 * @author aurelien
 *
 */
public class AgtStoryteller extends Agent {
	// For the GameExitErrorCodes, take a look at the END OF THE GAME section
	/** Auto generated serial id */
	private static final long serialVersionUID = -1537520826022941930L;
	/** The number of people required to start a game */
	public int nbOfRequiredPlayersToStartAGame = 9;
	/** The map of the players with their role. */
	private HashMap<AID,Roles> playersMap = new HashMap<AID,Roles>();
	/** A list of the players who are supposed to die next. */
	private ArrayList<AID> lastVictimsRoles = new ArrayList<AID>();
	/** The clock that regulates the game phases and game speed. */
	private PhaseClock phaseClock;
	/** The number of answers the controller is waiting for before going on. */
	private int nbWaitingAnswers;
	//TODO: lovers, charmed
	
	/**
	 * The default constructor. Start an AgtStoryteller.
	 * Attach its behaviour and initialize the internal clock.
	 */
	public AgtStoryteller(){
		super();
		this.addBehaviour(new BehaviourStoryteller(this));
		this.phaseClock = new PhaseClock(this);
		this.nbWaitingAnswers = 0;
	}

	/**
	 * This method will create several players on the same station.
	 * @param nbOfPlayers The number of players we wish to create
	 */
	public void populate(int nbOfPlayers){
		// Get the container controller to start the agents
		AgentContainer mc = this.getContainerController();
		AgentController ac;
		try {
			for(int i = 0; i < nbOfPlayers ; ++i){
				// Create a new Agent for the player
				AgtPlayer player = new AgtPlayer();
				ac = mc.acceptNewAgent("player"+(++i), player);
				ac.start();
				// Register the Agent to this AgtStoryteller
				player.Register(this.getAID());
			}			
		} catch (StaleProxyException e) {
			e.printStackTrace();
		} catch (ControllerException e) {
			e.printStackTrace();
		}		
	}

	
////////////////////////////////////////////////////////////////////////////////
/////////////// 	 START A GAME     
////////////////////////////////////////////////////////////////////////////////
	/**
	 * Enough players registered. Storyteller can ask all the players whether they are ready or not.
	 * If enough players are ready, the game will start.
	 */
	protected void askToStartGame(){
		// Ask all the players and wait for their answer
		this.nbWaitingAnswers = this.playersMap.size();
		mStorytellerPlayer message = new mStorytellerPlayer();
		message.type = mStorytellerPlayer.mType.START_GAME;
		message.storyTelling = "Do you want to participate in the new game ?";
		this.sendMessageToRegisteredAgents(message);
		
		// If it takes too long, cut the preparation phase
		this.phaseClock.startPreparationTimer();
	}
	/**
	 * Method called when a new game should start, after the assignation of the roles to the players.
	 */
	private void startGame(){
		// Start the game phases
		this.phaseClock.startGameTimer();		
	}

	
////////////////////////////////////////////////////////////////////////////////
/////////////// 	 ROLES     
////////////////////////////////////////////////////////////////////////////////
	/**
	 * Assign roles to the current players
	 */
	protected void assignRoles(){
		this.lastVictimsRoles.clear();
		// Remove players who do not participate
		// TODO: verify there are no problems in the iteration
		Set<AID> keySet = playersMap.keySet();
		for(AID aid: keySet){
			if(playersMap.get(aid).equals(Roles.DEAD)){
				playersMap.remove(aid);
			}
		}
		// TODO: should depend on the number of players
		// TODO: should use a KB
		Object[] keys = keySet.toArray();
		for(int i = 0; i < keys.length ; ++i){
			mStorytellerPlayer message = new mStorytellerPlayer();
			message.type = mStorytellerPlayer.mType.ATTRIBUTE_ROLE;
			switch(i){
			case 0:
				message.role = Roles.WEREWOLF;
				break;
			case 1:
				message.role = Roles.WEREWOLF;
				break;
			case 2:
				message.role = Roles.WEREWOLF;
				break;
			case 3:
				message.role = Roles.GUARDIAN;
				break;
			case 4:
				message.role = Roles.WITCH;
				break;
			case 5:
				message.role = Roles.CLAIRVOYANT;
				break;
			default:
				message.role = Roles.VILLAGER;
				break;
			}
			message.storyTelling = "Your role for this game is "+message.role;
			this.sendMessageToOneRegisteredAgent((AID)keys[i], message);
		}
	}
	/**
	 * Add a player to the party room. The players do not necessarily have to be "playing".
	 * They can watch the game and participate in a game if a new game starts.
	 * @param player The AID of the new player
	 */
	public void addPlayerToParty(AID player){
		// Add the player list of players in the room of this storyteller
		// Default Roles value for a player is "Dead" (Observer)
		this.playersMap.put(player, Roles.DEAD);
		
		// If enough players are in the room now, we can ask all the players if they want to start the game
		if(this.playersMap.size() >= this.nbOfRequiredPlayersToStartAGame && this.nbWaitingAnswers == 0){
			this.askToStartGame();
		}
	}
	/**
	 * Give a role to a player
	 * @param role The role to give to the player
	 * @param player The player to which we want to give the role
	 */
	public void addRoleToPlayer(Roles role, AID player){
		// Change the role of the given player
		this.playersMap.put(player, role);
		
		// Another player has answered, if it was the last to answer, we may want to start the game
		this.nbWaitingAnswers--;
		if(this.nbWaitingAnswers == 0){
			this.startGame();
		}
	}	
	/**
	 * Check if at least one player has the given role
	 * @param role The role to search for
	 * @return Whether one player at least has the given role
	 */
	public boolean roleStillInGame(Roles role){
		return this.playersMap.containsValue(role);
	}
	
	
////////////////////////////////////////////////////////////////////////////////
/////////////// 	 PARTICIPATION OF PLAYERS      
////////////////////////////////////////////////////////////////////////////////
	/**
	 * If all the players have answered to the Storyteller we may want to start the game.
	 * The game will only start if enough players are willing to participate.
	 */
	private void playersAllAnsweredParticipationCall(){	
		// Get the number of players who answered yes
		int numberOfPositiveAnswers = 0;
		for(Roles role: this.playersMap.values()){
			if(role.equals(Roles.UNASSIGNED)){
				numberOfPositiveAnswers++;
			}
		}
		
		// Now, we want to assign the roles to the players who participate in the game
		// and wait for their answer (they initialized their Role correctly and are ready to play)
		if(numberOfPositiveAnswers > this.nbOfRequiredPlayersToStartAGame){
			nbWaitingAnswers = numberOfPositiveAnswers;
			this.assignRoles();
		}
	}
	/**
	 * Mark the player as not willing to participate in the next game
	 */
	public void playersDoesntParticipate(AID player){
		// Mark the player has answered
		this.nbWaitingAnswers--;		
		// Mark the player's role as dead
		this.playersMap.put(player, Roles.DEAD);
		// If everyone answered
		if(this.nbWaitingAnswers == 0){
			this.playersAllAnsweredParticipationCall();
		}
	}
	/**
	 * Mark the player in the room as willing to participate in the game that is going to start
	 * @param player The player who wants to participate
	 */
	public void playerWantsToParticipate(AID player){
		// Mark the player has answered
		this.nbWaitingAnswers--;
		// The player's role will have to be assigned
		this.playersMap.put(player, Roles.UNASSIGNED);
		// If everyone answered
		if(this.nbWaitingAnswers == 0){
			this.playersAllAnsweredParticipationCall();
		}
	}
	/**
	 * If the players take too long to answer, we don't want to block the game.
	 * This method should be called by the PhaseClock.
	 * If enough players are willing to start, the game will start.
	 */
	public void playersParticipationTimeElapsed(){
		// Everyone should have answered by now, others are considered as 'No'
		this.nbWaitingAnswers = 0;
		this.playersAllAnsweredParticipationCall();
	}


////////////////////////////////////////////////////////////////////////////////
/////////////// 	 PHASES      
////////////////////////////////////////////////////////////////////////////////
	/**
	 * This method is called by the internal clock to know 
	 * if the phase it wants to start should be skipped
	 * @param phase The phase the clock wants to start
	 * @return whether it should be skipped or not
	 */
	public boolean shouldSkipPhase(GamePhases phase){
		boolean shouldSkip = false;
		boolean afterFirstRound = this.phaseClock.getNbOfTurns() > 1;
		switch(phase){ // We only need to do phase x..
		case CUPID: // during the first round and if someone has the cupid role
			shouldSkip = afterFirstRound || !roleStillInGame(Roles.CUPID);
			break;
		case LOVERS: // during the first round and if lovers were chosen
			// TODO: lovers and not cupid
			shouldSkip = afterFirstRound || !roleStillInGame(Roles.CUPID);
			break;
		case THIEF: // during the first round and if someone has the cupid role
			shouldSkip = afterFirstRound || !roleStillInGame(Roles.THIEF);
			break;
		case GUARDIAN: // if someone has the guardian role
			shouldSkip = !roleStillInGame(Roles.GUARDIAN);
			break;
		case CLAIRVOYANT: // if someone has the clairvoyant role
			shouldSkip = !roleStillInGame(Roles.CLAIRVOYANT);
			break;
		case WEREWOLVES: // if there is at least one were-wolf (otherwise isGameOver==true)
			break;
		case WITCH: // if someone has the witch role
			shouldSkip = !roleStillInGame(Roles.WITCH);
			break;
		case WHITE_WOLF: // if someone has the white-wolf role
			shouldSkip = !roleStillInGame(Roles.WHITE_WOLF);
			break;
		case RAVEN: // if someone has the raven role
			shouldSkip = !roleStillInGame(Roles.RAVEN);
			break;
		case FLUTE_PLAYER: // if someone has the flute player role
			shouldSkip = !roleStillInGame(Roles.FLUTE_PLAYER);
			break;
		case CHARMED: // if someone has been charmed
			// TODO: charmed
			shouldSkip = !roleStillInGame(Roles.FLUTE_PLAYER);
			break;
		case VICTIMS_REVELATION: // if someone has been killed during the night
			shouldSkip = this.lastVictimsRoles.isEmpty();
			break;
		case VICTIMS_EVENT: // if there is a lover, hunter or village sage night victim
			shouldSkip = (this.lastVictimsRoles.contains(Roles.HUNTER)
					//this.lastVictimsRoles.contains(Roles.LOVERS) //TODO:					
					|| this.lastVictimsRoles.contains(Roles.VILLAGE_SAGE));
			break;
		case VICTIMS_RESOLUTION:
			break;
		case MAYOR_ELECTION: // if it is the first round
			shouldSkip = afterFirstRound;
			break;
		case HUNG_VOTE:
			break;
		case HUNG_REVELATION:
			break;
		case HUNG_EVENT: // if the hung one is a scapegoat, hunter, lover, idiot or sage
			shouldSkip = (this.lastVictimsRoles.contains(Roles.SCAPEGOAT)
					|| this.lastVictimsRoles.contains(Roles.HUNTER)
					//|| this.lastVictimsRoles.contains(Roles.LOVERS) //TODO:
					|| this.lastVictimsRoles.contains(Roles.VILLAGE_IDIOT)
					|| this.lastVictimsRoles.contains(Roles.VILLAGE_SAGE));
			break;
		case HUNG_RESOLUTION: // if someone was hung
			shouldSkip = this.lastVictimsRoles.isEmpty();
			break;
		default:
			break;
		}	
		return shouldSkip;
	}
	/**
	 * This method is called by the internal clock when the new phase is about to begin.
	 * @param phase The phase to begin.
	 */
	public void willStartPhase(GamePhases phase){	
		// Initialize a message between using the Storyteller-Player Message template
		mStorytellerPlayer msg = new mStorytellerPlayer();
		msg.type = mStorytellerPlayer.mType.STORYTELLING;
		msg.phase = phase;
		
		// Storytelling depends on the phase
		String storytelling = "";
		switch(phase){
		case NONE:
			break;
		case NIGHT:
			storytelling = "It is now the night. The village goes to sleep.";
			break;
		case CUPID:
			storytelling = "Cupid wakes up. He can choose two people who will deeply fall in love.";
			break;
		case LOVERS:
			storytelling = "The lovers recognize each other.";
			break;
		case THIEF:
			storytelling = "The thief can choose between two roles.";
			break;
		case GUARDIAN:
			storytelling = "The guardian can protect one person for tonight.";
			break;
		case CLAIRVOYANT:
			storytelling = "The clairvoyant can detect someone's role.";
			break;
		case WEREWOLVES:
			storytelling = "The werewolves wake up and gather to select their victim for tonight.";
			break;
		case WITCH:
			storytelling = "The witch wakes up. She can use her revive pot to save the werewolves' victim " +
					"or her deathly pot to make someone die in terrible suffering.";
			break;
		case WHITE_WOLF:
			storytelling = "The white wolf wakes up and can select his wolf's victim.";
			break;
		case RAVEN:
			storytelling = "The raven wakes up and can point to the village who he thinks is suspicious.";
			break;
		case FLUTE_PLAYER:
			storytelling = "The flute player wakes up and can charm two other people in the village.";
			break;
		case CHARMED:
			storytelling = "The people charmed by the flute player wake up and gather.";
			break;
		case DAY:
			storytelling = "It is now the day. The village wakes up.";
			break;
		case VICTIMS_REVELATION:
			storytelling = "Tonight's victims are revealed.";
			break;
		case VICTIMS_EVENT:
			storytelling = "Before their last action the victims can try a desperate move.";
			break;
		case VICTIMS_RESOLUTION:
			storytelling = "The victims die.";
			break;
		case MAYOR_ELECTION:
			storytelling = "The mayor election can begin. The village needs someone to follow! " +
					"Choose wisely because the mayor has power.";
			break;
		case HUNG_VOTE:
			storytelling = "The hanged selection begins. Who will be hung on the place today ?";
			break;
		case HUNG_REVELATION:
			storytelling = "The hung role's revealed.";
			break;
		case HUNG_EVENT:
			storytelling = "Before his hunging the hung can try a desperate move.";
			break;
		case HUNG_RESOLUTION:
			storytelling = "The hung is dead.";
			break;
		default:
			break;
		}
		msg.storyTelling = storytelling;
		
		// Send the message to all the registered agents
		sendMessageToRegisteredAgents(msg);	
	}
	/**
	 * This method is called by the internal clock when the current phase stops.
	 * The clock is actually stopped until the Storyteller does something.
	 * @param phase The phase that should be ending.
	 */
	public void endOfPhase(GamePhases phase){
		boolean gameIsOver = false;
		// TODO: lots of stuff to do
		switch(phase){
		case NONE:
			break;
		case NIGHT:
			lastVictimsRoles.clear();
			break;
		case CUPID:
			break;
		case LOVERS:
			break;
		case THIEF:
			break;
		case GUARDIAN:
			break;
		case CLAIRVOYANT:
			break;
		case WEREWOLVES:
			break;
		case WITCH:
			break;
		case WHITE_WOLF:
			break;
		case RAVEN:
			break;
		case FLUTE_PLAYER:
			break;
		case CHARMED:
			gameIsOver = this.checkGameIsOver();
			break;
		case DAY:
			break;
		case VICTIMS_REVELATION:
			break;
		case VICTIMS_EVENT:
			break;
		case VICTIMS_RESOLUTION:
			gameIsOver = this.checkGameIsOver();
			break;
		case MAYOR_ELECTION:
			break;
		case HUNG_VOTE:
			lastVictimsRoles.clear();
			break;
		case HUNG_REVELATION:
			break;
		case HUNG_EVENT:
			break;
		case HUNG_RESOLUTION:
			gameIsOver = this.checkGameIsOver();
			break;
		default:
			break;
		}	
		
		// If the game is over we end the game
		if(gameIsOver){
			this.endGameWithState(GameExitErrorCodes.GAME_OVER);
		}
		
		// If the game is not over, we should start the next phase
		else {
			phaseClock.startNextPhase();
		}		
	}

	
////////////////////////////////////////////////////////////////////////////////
/////////////// 	 END OF THE GAME .. OR NOT       
////////////////////////////////////////////////////////////////////////////////
	/**
	 * The various error codes we could have
	 * @author aurelien
	 *
	 */
	public enum GameExitErrorCodes {
		/** This would be bad. */
		UNKNOWN_REASON,
		/** This should not happening but we knew it could happen. */
		BUG,
		/** PhaseClock.startNextPhase() - should not be happening. */
		NO_POSSIBLE_PHASE,
		/** There aren't enough players to play this game right now. */
		TOO_FEW_PLAYERS,
		/** Some people won or everyone died. Either way, this game is over. */
		GAME_OVER
	}
	/**
	 * Game is over the there is no werewolf alive
	 * or there are only werewolves alive 
	 * or only the two lovers are alive
	 * or all the players have been charmed
	 */
	public boolean checkGameIsOver(){
		int nWolf = 0, nMax = 0, nCharmed = 0;		
		for(Roles role: this.playersMap.values()){
			switch(role){
			case WEREWOLF: 
				nWolf++;
				nMax++;
				break;
			case WHITE_WOLF:
				nWolf++;
				nMax++;
				break;
			case DEAD:
				break;
			default:
				nMax++;
			}
			// TODO: if charmed -> nCharmed++
		}
		
		// TODO: ||Êtwo lovers only
		return ((nWolf == nMax) || (nWolf == 0) || (nCharmed == nMax-1)); 
	}
	/**
	 * Method called when the current game should stop. 
	 * Can be cause by an internal error or because the game is over (there is a winner or everyone is dead).
	 */
	public void endGameWithState(GameExitErrorCodes errorCode){
		// End the phase clock
		this.phaseClock.stopTimer();
		
		// Prepare a message for all the players using the Storyteller-Player Message template
		mStorytellerPlayer message = new mStorytellerPlayer();
		message.phase = Global.GamePhases.NONE;
		message.type = mStorytellerPlayer.mType.END_GAME;
		
		// Reason depends on the errorCode
		switch(errorCode){
		// Normal way to end a game
		case GAME_OVER:{
			// If everyone is charmed, the flute player wins the game
			//TODO: all charmed
			// If only the lovers are alive, they win the game
			//TODO: lovers
			// If any werewolf is alive then the werewolves win the game
			if(this.playersMap.containsValue(Roles.WEREWOLF)){
				message.storyTelling = "The werewolves win the game!";
			}
			// If the white wolf is the only player alive, he wins the game
			else if(this.playersMap.containsValue(Roles.WHITE_WOLF)){
				message.storyTelling = "The white wolf wins the game!";
			}
			// Otherwise, the villagers win the game
			else {
				message.storyTelling = "The villagers win the game!";
			}
		}break;
		case UNKNOWN_REASON:
			message.storyTelling = "I don't know why the game stopped.";
			break;
		case BUG:
			message.storyTelling = "Nyeeeee... There is a problem here. Sorry for the inconvenience =(";
			break;
		case NO_POSSIBLE_PHASE:
			message.storyTelling = "There is no possible phase. Weird.";
			break;
		case TOO_FEW_PLAYERS:
			message.storyTelling = "Hey guys! We need more people!";
			break;
		default:
			message.storyTelling = "Laule. I don't even have an error code for that.";
			break;
		}
		sendMessageToRegisteredAgents(message);
	}

	
////////////////////////////////////////////////////////////////////////////////
/////////////// 	 SEND MESSAGES      
////////////////////////////////////////////////////////////////////////////////
	/**
	 * Prepare a message for a Communication Agent
	 * @param messageType The type of message to send (is it a vote, an advice, an action)
	 * @return
	 */
	public void sendMessageToCommunicationAgent(mStorytellerCommunication message){
		// TODO: everything
		// A - Message for Vote: ask for a vote
		// B - Message for Advice: ask for an advice turn
		// C - Message for Action: ask for the realization of a phase/action, notify a player's death, the beginning of a game
	}
	/**
	 * Initialize a message for all the subscribed players. 
	 * @param message The message object to serialize and send as content
	 */
	public void sendMessageToRegisteredAgents(mStorytellerPlayer message){
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		for(AID aid: playersMap.keySet()){
			msg.addReceiver(aid);
		}
		msg.setContent(message.toJson());
		System.out.println(message.storyTelling);
		this.send(msg);
	}
	/**
	 * Initialize a message to one agent player.
	 * @param message The message object to serialize and send as content
	 */
	public void sendMessageToOneRegisteredAgent(AID aid, mStorytellerPlayer message){
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(aid);
		msg.setContent(message.toJson());
		System.out.println(message.storyTelling);
		this.send(msg);
	}
}

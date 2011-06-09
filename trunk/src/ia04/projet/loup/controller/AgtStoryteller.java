package ia04.projet.loup.controller;

import ia04.projet.loup.DFInterface;
import ia04.projet.loup.Debugger;
import ia04.projet.loup.Global;
import ia04.projet.loup.Global.GamePhases;
import ia04.projet.loup.Global.Roles;
import ia04.projet.loup.communication.AgtVote;
import ia04.projet.loup.messages.mAction;
import ia04.projet.loup.messages.mMessage;
import ia04.projet.loup.messages.mPlayerDied;
import ia04.projet.loup.messages.mStartGame;
import ia04.projet.loup.messages.mStorytellerKb;
import ia04.projet.loup.messages.mStorytellerPlayer;
import ia04.projet.loup.messages.mStorytellerPlayer.mType;
import ia04.projet.loup.messages.mTimeElapsed;
import ia04.projet.loup.messages.mVoteRun;
import ia04.projet.loup.players.AgtPlayer;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Random;
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
	/** The generator used to pick configurations and assign roles randomly */
	private Random generator = new Random();
	/** The number of answers the controller is waiting for before going on. */
	private int nbWaitingAnswers;
	/** The number of times the current phase has been repeated */
	private int nbRepeatedPhase;
	/** Remember when a request has been sent and not answered */
	private boolean hasSentRequestToKb = false;
	/** The AID of the AgtKbStoryteller this storyteller agent can use. */
	private AID kbStoryteller;
	/** The AID of the AgtVote this storyteller interacts with */
	private AID agentVoteAid;
	/** The AID of the AgtAction this storyteller interacts with */
	private AID agentActionAid;
	/** The AID of the AgtAdvice this storyteller interacts with */
	private AID agentAdviceAid;
	/** The AID of the AgtRole protected by the Guardian this turn */
	private AID guardianTarget;
	/** The AID of the AgtRole being a mayor. */
	private AID mayorAid;
	/** The AID of the first lover. */
	private AID firstLoverAid;
	/** The AID of the second lover. */
	private AID secondLoverAid;
	//TODO: charmed


	////////////////////////////////////////////////////////////////////////////////
	/////////////// 	 INITIALIZATIONS    
	////////////////////////////////////////////////////////////////////////////////
	/**
	 * The default constructor. Start an AgtStoryteller.
	 * Attach its behaviour and initialize the internal clock.
	 */
	public AgtStoryteller(){
		super();
		this.addBehaviour(new BehaviourStoryteller());
		this.phaseClock = new PhaseClock(this);
		this.nbWaitingAnswers = 0;
	}	
	/**
	 * Set the number of players needed to start a game
	 * @param nb The new value
	 */
	public void setNbRequiredPlayers(int nb){
		this.nbOfRequiredPlayersToStartAGame = nb;
	}
	/**
	 * Registers its service into the DF
	 */
	public void registerServiceToDf(){
		ServiceDescription sd = new ServiceDescription();
		sd.setType("AgtStoryteller");
		sd.setName(this.getLocalName());
		DFInterface.registerService(this, sd);
	}
	/**
	 * Initialize the Kb Agent used by the Storyteller agent if it hasn't been done before.
	 */
	public void createKbAgent(){		
		// If the Kb Agent wasn't created, create it
		if(this.kbStoryteller !=null){
			nbWaitingAnswers = 0;
			return;
		}

		// Create and register an agent KB used by this storyteller
		AgtKbStoryteller agtKbStoryteller = new AgtKbStoryteller();
		AgentContainer mc = this.getContainerController();
		AgentController ac;
		try {
			ac = mc.acceptNewAgent("AgtKbStoryteller", agtKbStoryteller);
			ac.start();
		} catch (StaleProxyException e) {
			Debugger.println("Could not initialize the AgtKbStoryteller.");
			Debugger.println(e.toString());
		}

		// Link the agent KB to this Storyteller agent
		this.kbStoryteller = agtKbStoryteller.getAID();
	}
	/** 
	 * Indicates who is the vote agent for this storyteller agent
	 * @param aid the AID of the vote agent to use
	 */
	public void setVoteAgent(AID aid){
		this.agentVoteAid = aid;
	}
	/** 
	 * Indicates who is the action agent for this storyteller agent
	 * @param aid the AID of the action agent to use
	 */
	public void setActionAgent(AID aid){
		this.agentActionAid = aid;
	}
	/** 
	 * Indicates who is the advice agent for this storyteller agent
	 * @param aid the AID of the advice agent to use
	 */
	public void setAdviceAgent(AID aid){
		this.agentAdviceAid = aid;
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
			for(int i = 1; i <= nbOfPlayers; ++i){
				// Create a new Agent for the player
				AgtPlayer player = new AgtPlayer();
				ac = mc.acceptNewAgent("player"+i, player);
				ac.start();
				player.GuiCreation();
				// Register the Agent to this AgtStoryteller
				player.Register(this.getAID());
			}			
		} catch (StaleProxyException e) {
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
		message.setType(mStorytellerPlayer.mType.START_GAME);
		message.setStoryTelling("Do you want to participate in the new game ?");
		this.sendMessageToRegisteredAgents(message);

		// If it takes too long, cut the preparation phase
		this.phaseClock.startPreparationTimer();
	}
	/**
	 * Pick one configuration and assign the roles.
	 * @param possibleConfigurations The possible configurations
	 */
	protected void chooseConfiguration(HashMap<String,ArrayList<Global.Roles>> possibleConfigurations, int nbPlayers){
		// If no possible configurations, we stop here. Nothing will happen until a new player joins
		if(possibleConfigurations.isEmpty()){
			return;
		}

		// Pick a configuration randomly
		int pickedConfiguration = generator.nextInt(possibleConfigurations.size());
		ArrayList<Global.Roles> rolesToAttribute =  possibleConfigurations.get(
				possibleConfigurations.keySet().toArray()[pickedConfiguration]);

		// Start the Roles assignation
		System.out.println("Starting Roles assignation");
		System.out.println("Possible Roles:"+rolesToAttribute.toString());
		this.lastVictimsRoles.clear();

		// Remove players who do not participate
		Set<AID> keySet = playersMap.keySet();
		HashSet<AID> playersAid = new HashSet<AID>();
		for(AID aid: keySet){
			if(!playersMap.get(aid).equals(Roles.DEAD)){
				playersAid.add(aid);
			}
		}

		// For each player
		for(AID aid: playersAid){
			// Pick a role randomly
			int roleIndex = generator.nextInt(rolesToAttribute.size());

			// Attribute the role to the player
			this.assignRoleToPlayer(rolesToAttribute.get(roleIndex), aid);

			// Remove role from list
			rolesToAttribute.remove(roleIndex);
		}			
		System.out.println("Remaining roles:"+rolesToAttribute.toString());
	}
	/**
	 * Method called when a new game should start, after the assignation of the roles to the players.
	 */
	private void startGame(){
		// Start the game phases
		this.phaseClock.startGameTimer();	

		// Notify the roles through the Vote agent
		mStartGame startGameMessage = new mStartGame();		
		startGameMessage.setStartGame(true);
		this.sendMessageToVoteAgent(startGameMessage, ACLMessage.INFORM);
	}


	////////////////////////////////////////////////////////////////////////////////
	/////////////// 	 PLAYERS     
	////////////////////////////////////////////////////////////////////////////////
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
	 * Remove a player from the party room. 
	 * @param player The AID of the player
	 */
	public void removePlayerFromParty(AID player){
		// Remove the player from list of players of this storyteller
		this.playersMap.remove(player);
	}


	////////////////////////////////////////////////////////////////////////////////
	/////////////// 	 ROLES     
	////////////////////////////////////////////////////////////////////////////////
	/**
	 * Assign a role to a player. Initialize a message to send to that player.
	 * Storyteller will wait for his answer.
	 */
	protected void assignRoleToPlayer(Global.Roles role, AID player){
		mStorytellerPlayer message = new mStorytellerPlayer();
		message.setType(mStorytellerPlayer.mType.ATTRIBUTE_ROLE);
		message.setRole(role);
		message.setStoryTelling("Your role for this game is "+message.getRole());
		this.sendMessageToOneRegisteredAgent(player, message, ACLMessage.REQUEST);
		this.nbWaitingAnswers++;
	}
	/**
	 * Give a role to a player. Called when a player has initialized its role.
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
		// Timer call while all players have already answered
		if(hasSentRequestToKb){
			return;
		}
		hasSentRequestToKb = false;

		// Get the number of players who answered yes
		int numberOfPositiveAnswers = 0;
		for(Roles role: this.playersMap.values()){
			if(role.equals(Roles.UNASSIGNED)){
				numberOfPositiveAnswers++;
			}
		}

		// Now, we want to find a configuration with this number of players
		// Then we will assign the roles to the players who participate in the game
		// and wait for their answer (they initialized their Role correctly and are ready to play)
		if(numberOfPositiveAnswers >= this.nbOfRequiredPlayersToStartAGame){
			// First we need to find the possible configurations. Ask the KB agent.
			mStorytellerKb msg = new mStorytellerKb(mStorytellerKb.mType.GET_GAME_COMPOSITION, 
					numberOfPositiveAnswers);		
			this.sendMessageToKbAgent(msg);
		}
	}
	/**
	 * Mark the player as not willing to participate in the next game
	 */
	public void playerDoesntParticipate(AID player){
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
	/////////////// 	 ACTIONS      
	////////////////////////////////////////////////////////////////////////////////
	/**
	 * Add a victim to the list of people who should be dying soon
	 * @param victimAid The new victim
	 */
	private void addVictim(AID victimAid){		
		// If the werewolves' target was the guardian's one, it is saved
		if((victimAid != null)
				&& !this.playersMap.get(victimAid).equals(Roles.DEAD)
				&& (this.phaseClock.getCurrentPhase()!=GamePhases.WEREWOLVES
						|| !victimAid.equals(this.guardianTarget))){
			this.lastVictimsRoles.add(victimAid);

			// Lovers 
			if(victimAid.equals(this.firstLoverAid)){
				this.lastVictimsRoles.add(this.secondLoverAid);
			}
			else if(victimAid.equals(this.secondLoverAid)){
				this.lastVictimsRoles.add(this.firstLoverAid);
			}
		}
	}
	/**
	 * The victim was saved by a special power and will not die
	 * @param victimAid The victim who has been saved
	 */
	private void saveVictim(AID victimAid){
		this.lastVictimsRoles.remove(victimAid);
	}
	/**
	 * Check whether the action functionnality is available
	 */
	private boolean isRoleActionAvailable(Roles role){
		//TODO: not to forget
		boolean result = false;
		switch(role){
		case GUARDIAN:
		case HUNTER:
		case CLAIRVOYANT:
		case WITCH:
			result = true;
			break;
		case VILLAGEIDIOT:
		case VILLAGESAGE:
		case SCAPEGOAT:
		case CUPID:
		case THIEF:
		case WHITEWOLF:
		case RAVEN:
		case FLUTEPLAYER:
		default:
			result = false;
			break;
		}
		return result;
	}
	/**
	 * The night's victims may have an action to perform before 
	 */
	public void nightVictimsEvent(){
		this.nbWaitingAnswers = 0;
		for(AID victim: this.lastVictimsRoles){
			Global.Roles victimRole = this.playersMap.get(victim);
			// Mayor
			if(victim.equals(this.mayorAid)){
				mVoteRun voteMsg = new mVoteRun(AgtVote.voteType.SUCCESSOR);
				this.sendMessageToVoteAgent(voteMsg, ACLMessage.REQUEST);
				this.nbWaitingAnswers++;
				Debugger.println("Mayor has been killed. He has to name his successor");
			}
			// Hunter
			if(victimRole.equals(Roles.HUNTER)){
				this.actionStart(Roles.HUNTER);
			}
			// Sage
			else if(victimRole.equals(Roles.VILLAGESAGE)){
				this.actionStart(Roles.VILLAGESAGE);
			}
		}		
	}
	/**
	 * The day's victims may have an action to perform before 
	 */
	public void dayVictimsEvent(){
		this.nbWaitingAnswers = 0;
		for(AID victim: this.lastVictimsRoles){
			Global.Roles victimRole = this.playersMap.get(victim);
			// Mayor
			if(victim.equals(this.mayorAid)){
				mVoteRun voteMsg = new mVoteRun(AgtVote.voteType.SUCCESSOR);
				this.sendMessageToVoteAgent(voteMsg, ACLMessage.REQUEST);
				this.nbWaitingAnswers++;
			}
			// Hunter
			if(victimRole.equals(Roles.HUNTER)){
				this.actionStart(Roles.HUNTER);
			}
			// Idiot
			else if(victimRole.equals(Roles.VILLAGEIDIOT)){
				this.actionStart(Roles.VILLAGEIDIOT);
			}
			// Scapegoat
			else if(victimRole.equals(Roles.SCAPEGOAT)){
				this.actionStart(Roles.SCAPEGOAT);
			}
		}		
	}
	/**
	 * The victims are actually killed. 
	 * The killed players are notified about their death.
	 * The communication agent is notified about that.
	 */
	public void killVictims(){
		// Kill the victims
		String storytelling = "";
		for(AID victim: this.lastVictimsRoles){
			Roles role = this.playersMap.get(victim);
			this.playersMap.put(victim, Global.Roles.DEAD);
			mPlayerDied message = new mPlayerDied();
			message.setIsOver(false);
			message.setDeadName(victim.getLocalName());
			message.setIsHungVictim(this.phaseClock.getCurrentPhase().
					equals(GamePhases.HUNGRESOLUTION));
			sendMessageToOneRegisteredAgent(victim, message, ACLMessage.INFORM);
			storytelling+=victim.getLocalName()+" ("+role+") died.\n";
		}

		// Wait for the players to clean their role
		this.nbWaitingAnswers = this.lastVictimsRoles.size();

		// Send a message to the agents
		if(this.lastVictimsRoles.size() > 0){
			storytelling = storytelling.substring(0, storytelling.length()-1);
		}
		else {
			storytelling = "No one died tonight.";
		}
		mStorytellerPlayer storytellingMsg = new mStorytellerPlayer();
		storytellingMsg.setType(mType.STORYTELLING);
		storytellingMsg.setPhase(this.phaseClock.getCurrentPhase());
		storytellingMsg.setStoryTelling(storytelling);
		this.sendMessageToRegisteredAgents(storytellingMsg);

	}
	/**
	 * A player finished his 'iAmDead' stuff
	 */
	public void playerFinishedBeingKilled(){
		this.nbWaitingAnswers--;
	}
	/**
	 * Launch an action for the given role if it is possible (check whether it is implemented)
	 * @param role The role we want to perform an action
	 */
	public void actionStart(Roles role){
		// If we can do the action (is implemented)
		if(isRoleActionAvailable(role)){
			mAction actionMsg = new mAction(role);
			if(role.equals(Roles.WITCH)){
				if(this.lastVictimsRoles.size() > 0){
					Debugger.println("Victim:"+this.lastVictimsRoles.get(0).getLocalName());
					actionMsg.setTargetSaved(this.lastVictimsRoles.get(0).getLocalName());					
				}
				else {
					return;
				}
			}
			this.sendMessageToActionAgent(actionMsg, ACLMessage.REQUEST);
			this.nbWaitingAnswers++;			
		}
	}
	/**
	 * When an action failed we don't want to stop the game.
	 * Usually called after a failed vote
	 */
	public void actionFailed(){
		this.nbWaitingAnswers--;
	}
	/**
	 * Role performed its action
	 * @param performer The agent who performed the action
	 * @param targetKilled The target to kill (optional)
	 * @param targetSaved The target to save (optional)
	 * @param role The role of the agent, of the action
	 */
	public void actionDone(AID performer, AID targetKilled, AID targetSaved, Roles role){
		switch(role){
		case GUARDIAN: {
			this.guardianTarget = targetSaved;
			Debugger.println("Protected:"+targetSaved.getLocalName());
		}	break;
		case HUNTER: {
			this.addVictim(targetKilled);
		}	break;
		case CLAIRVOYANT:
			break;
		case WITCH: {
			if(targetKilled != null){
				this.addVictim(targetKilled);
			}
			if(targetSaved != null){
				this.saveVictim(targetSaved);
			}
		}	break;
		case VILLAGEIDIOT:
			break;
		case VILLAGESAGE:
			break;
		case SCAPEGOAT:
			break;
		case CUPID:
			break;
		case THIEF:
			break;
		case WHITEWOLF:
			break;
		case RAVEN:
			break;
		case FLUTEPLAYER:
			break;
		default:
			break;
		}
		this.nbWaitingAnswers--;
	}

	//TODO: actions' reactions

	////////////////////////////////////////////////////////////////////////////////
	/////////////// 	 VOTES      
	////////////////////////////////////////////////////////////////////////////////
	/**
	 * Treatment of the werewolves' vote results
	 * @param choice The target
	 */
	public void werewolvesEndedWithChoice(AID choice){
		if(choice!=null){
			Debugger.println("Werewolves target: "+choice.getLocalName());
			this.addVictim(choice);
		}
		else {
			Debugger.println("Werewolves couldn't agree on a target");
		}
		this.nbWaitingAnswers=0;
	}
	/**
	 * Treatment of the paysans' vote results
	 * @param choice The target
	 */
	public void paysansEndedWithChoice(AID choice){
		Debugger.println("Future hung: "+choice.getLocalName());
		if(choice!=null){
			this.addVictim(choice);
		}
		this.nbWaitingAnswers=0;		
	}
	/**
	 * Treatment of the mayor's election's vote results
	 * @param choice The elected player
	 */
	public void mayorElectionEndedWithChoice(AID choice){
		Debugger.println("New mayor: "+choice.getLocalName());
		this.mayorAid = choice;
		this.nbWaitingAnswers=0;		
	}

	////////////////////////////////////////////////////////////////////////////////
	/////////////// 	 PHASES MANAGEMENT
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
		case WHITEWOLF: // if someone has the white-wolf role
			shouldSkip = !roleStillInGame(Roles.WHITEWOLF);
			break;
		case RAVEN: // if someone has the raven role
			shouldSkip = !roleStillInGame(Roles.RAVEN);
			break;
		case FLUTEPLAYER: // if someone has the flute player role
			shouldSkip = !roleStillInGame(Roles.FLUTEPLAYER);
			break;
		case CHARMED: // if someone has been charmed
			// TODO: charmed
			shouldSkip = !roleStillInGame(Roles.FLUTEPLAYER);
			break;
		case VICTIMSREVELATION: // if someone has been killed during the night
			shouldSkip = this.lastVictimsRoles.isEmpty();
			break;
		case VICTIMSEVENT: // if there is a lover, hunter or village sage night victim
			shouldSkip = (this.lastVictimsRoles.contains(Roles.HUNTER)
					//this.lastVictimsRoles.contains(Roles.LOVERS) //TODO:					
					|| this.lastVictimsRoles.contains(Roles.VILLAGESAGE));
			break;
		case VICTIMSRESOLUTION:
			break;
		case MAYORELECTION: // if it is the first round
			shouldSkip = afterFirstRound;
			break;
		case HUNGVOTE:
			break;
		case HUNGREVELATION:
			break;
		case HUNGEVENT: // if the hung one is a scapegoat, hunter, lover, idiot or sage
			shouldSkip = (this.lastVictimsRoles.contains(Roles.SCAPEGOAT)
					|| this.lastVictimsRoles.contains(Roles.HUNTER)
					//|| this.lastVictimsRoles.contains(Roles.LOVERS) //TODO:
					|| this.lastVictimsRoles.contains(Roles.VILLAGEIDIOT)
					|| this.lastVictimsRoles.contains(Roles.VILLAGESAGE));
			break;
		case HUNGRESOLUTION: // if someone was hung
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
		msg.setType(mStorytellerPlayer.mType.STORYTELLING);
		msg.setPhase(phase);
		this.nbRepeatedPhase = 0;

		// Storytelling depends on the phase
		String storytelling = "";
		switch(phase){
		case NONE:
			break;
		case NIGHT:
			Debugger.println("\n Turn "+this.phaseClock.getNbOfTurns()+" \n");
			if(Debugger.isOn() && this.phaseClock.getNbOfTurns() > 1){
				for(Entry<AID,Roles> player: this.playersMap.entrySet()){
					if(player.getKey().equals(this.mayorAid)){
						Debugger.println(player.getKey().getLocalName()+" has role: "+player.getValue()+" (Mayor).");						
					}
					else {
						Debugger.println(player.getKey().getLocalName()+" has role: "+player.getValue());
					}
				}
			}
			storytelling = "It is now the night. The village goes to sleep.";
			break;

		case CUPID:
			// TODO: action
			storytelling = "Cupid wakes up. He can choose two people who will deeply fall in love.";
			break;
		case LOVERS:
			// TODO: action
			storytelling = "The lovers recognize each other.";
			break;
		case THIEF:
			// TODO: action
			storytelling = "The thief can choose between two roles.";
			break;

		case GUARDIAN:{
			this.actionStart(Roles.GUARDIAN);
			storytelling = "The guardian can protect one person for tonight.";
		}	break;
		case CLAIRVOYANT:{
			this.actionStart(Roles.CLAIRVOYANT);
			storytelling = "The clairvoyant can detect someone's role.";
		}	break;

		case WEREWOLVES:{
			storytelling = "The werewolves wake up and gather to select their victim for tonight.";

			// Start a vote between the werewolves
			mVoteRun voteMsg = new mVoteRun(AgtVote.voteType.VOTE_WW);
			this.sendMessageToVoteAgent(voteMsg, ACLMessage.REQUEST);
			this.nbWaitingAnswers = 1;
		}	break;

		case WITCH:{
			this.actionStart(Roles.WITCH);
			storytelling = "The witch wakes up. She can use her revive pot or her deathly pot.";
		}	break;

		case WHITEWOLF:
			// TODO: action
			storytelling = "The white wolf wakes up and can select his wolf's victim.";
			break;
		case RAVEN:
			// TODO: action
			storytelling = "The raven wakes up and can point to the village who he thinks is suspicious.";
			break;
		case FLUTEPLAYER:
			// TODO: action
			storytelling = "The flute player wakes up and can charm two other people in the village.";
			break;
		case CHARMED:
			// TODO: ?? action ??
			storytelling = "The people charmed by the flute player wake up and gather.";
			break;
		case DAY:
			storytelling = "It is now the day. The village wakes up.";
			break;

		case VICTIMSREVELATION:
			// TODO: can be removed ?
			storytelling = "Tonight's victims are revealed.";
			break;			
		case VICTIMSEVENT: {
			nightVictimsEvent();
			storytelling = "Before their last action the victims can try a desperate move.";
		}	break;
		case VICTIMSRESOLUTION: {
			storytelling = "The victims die.";
			killVictims();
		}	break;

		case MAYORELECTION:{
			storytelling = "The mayor election can begin. The village needs someone to follow! " +
			"Choose wisely because the mayor has power.";
			// Start a vote for the mayor
			mVoteRun voteMsg = new mVoteRun(AgtVote.voteType.ELECT_MAYOR);
			this.sendMessageToVoteAgent(voteMsg, ACLMessage.REQUEST);
			this.nbWaitingAnswers = 1;
		}	break;

		case HUNGVOTE:{
			storytelling = "The hanged selection begins. Who will be hung on the place today ?";
			// Start a vote in the village
			mVoteRun voteMsg = new mVoteRun(AgtVote.voteType.VOTE_PAYSAN);
			this.sendMessageToVoteAgent(voteMsg, ACLMessage.REQUEST);
			this.nbWaitingAnswers = 1;
		}	break;
		case HUNGREVELATION:
			// TODO: can be removed ?
			storytelling = "The hung role's revealed.";
			break;
		case HUNGEVENT:{ 
			dayVictimsEvent();
			storytelling = "Before his hunging the hung can try a desperate move.";
		}	break;
		case HUNGRESOLUTION:{
			killVictims();
			storytelling = "The hung is dead.";
		}	break;
		default:
			break;
		}
		msg.setStoryTelling(storytelling);

		// Send the message to all the registered agents
		sendMessageToRegisteredAgents(msg);	
	}

	/**
	 * If we need to repeat a phase
	 */
	private boolean shouldRepeatPhase(GamePhases phase){
		boolean result = false;
		// Everyone answered, we're good
		if(this.nbWaitingAnswers == 0){
			result = false;
		}
		// Waiting for some answers but we have time
		else if(this.nbRepeatedPhase < Global.MAX_REPEATED_TIMES){
			this.nbRepeatedPhase++;
			result = true;
		}
		// Answered too late
		else if(this.nbRepeatedPhase == Global.MAX_REPEATED_TIMES){	
			// One last time, ask the Vote agent to finish
			if(phase.equals(GamePhases.WEREWOLVES)) {
				mTimeElapsed msg = new mTimeElapsed();
				msg.setTypeTimeElapsed(AgtVote.voteType.VOTE_WW.toString());
				this.sendMessageToVoteAgent(msg, ACLMessage.REQUEST);
				result = true;
			}	
			result = true;
			this.nbRepeatedPhase++;
		}
		// Wait for the result (should have asked to finish)
		else {
			result = true;
		}
		return result;
	}
	/**
	 * This method is called by the internal clock when the current phase stops.
	 * The clock is actually stopped until the Storyteller does something.
	 * @param phase The phase that should be ending.
	 */
	public void endOfPhase(GamePhases phase){
		boolean gameIsOver = false;
		// If it is needed, give more time
		if(shouldRepeatPhase(phase)){
			phaseClock.restartPhaseTimer();	
			return;
		}
		// TODO: lots of stuff to do
		switch(phase){
		case NONE:
			break;
		case NIGHT:
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
		case WHITEWOLF:
			break;
		case RAVEN:
			break;
		case FLUTEPLAYER:
			break;
		case CHARMED:
			gameIsOver = this.checkGameIsOver();
			break;
		case DAY:
			this.guardianTarget = null; // The target is not protected anymore
			break;
		case VICTIMSREVELATION:
			break;
		case VICTIMSEVENT:
			break;
		case VICTIMSRESOLUTION:
			gameIsOver = this.checkGameIsOver();
			lastVictimsRoles.clear();
			break;
		case MAYORELECTION:
			break;
		case HUNGVOTE:
			break;
		case HUNGREVELATION:
			break;
		case HUNGEVENT:
			break;
		case HUNGRESOLUTION:
			gameIsOver = this.checkGameIsOver();
			lastVictimsRoles.clear();
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
			case WHITEWOLF:
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
		message.setPhase(Global.GamePhases.NONE);
		message.setType(mStorytellerPlayer.mType.END_GAME);

		// Reason depends on the errorCode
		switch(errorCode){
		// Normal way to end a game
		case GAME_OVER:{
			// If everyone is charmed, the flute player wins the game
			//TODO: all charmed
			// If any werewolf is alive then the werewolves win the game
			if(this.playersMap.containsValue(Roles.WEREWOLF)){
				message.setStoryTelling("The werewolves win the game!");
			}
			// If the white wolf is the only player alive, he wins the game
			else if(this.playersMap.containsValue(Roles.WHITEWOLF)){
				message.setStoryTelling("The white wolf wins the game!");
			}
			// Otherwise, the villagers win the game
			else {
				int nbOfAlivePpl = 0;
				for(Roles role: playersMap.values()){
					if(!role.equals(Roles.DEAD)){
						nbOfAlivePpl++;
					}
				}
				// If only the lovers are alive, they win the game
				if(nbOfAlivePpl == 2 && !playersMap.get(this.firstLoverAid).equals(Roles.DEAD)){
					message.setStoryTelling("The lovers win the game!");					
				}
				else if(nbOfAlivePpl == 0){
					message.setStoryTelling("Everyone is dead!");								
				}
				else {
					message.setStoryTelling("The villagers win the game!");					
				}
			}
		}break;
		case UNKNOWN_REASON:
			message.setStoryTelling("I don't know why the game stopped.");
			break;
		case BUG:
			message.setStoryTelling("Nyeeeee... There is a problem here. Sorry for the inconvenience =(");
			break;
		case NO_POSSIBLE_PHASE:
			message.setStoryTelling("There is no possible phase. Weird.");
			break;
		case TOO_FEW_PLAYERS:
			message.setStoryTelling("Hey guys! We need more people!");
			break;
		default:
			message.setStoryTelling("Laule. I don't even have an error code for that.");
			break;
		}

		sendMessageToRegisteredAgents(message);
	}


	////////////////////////////////////////////////////////////////////////////////
	/////////////// 	 SEND MESSAGES      
	////////////////////////////////////////////////////////////////////////////////
	/**
	 * Initialize a message for the Advice Agent
	 * @param message The message object to serialize and send as content to the agent
	 */
	public void sendMessageToAdviceAgent(mMessage message, int messageType){
		//TODO: mMessage -> mAdvice or whatever
		ACLMessage msg = new ACLMessage(messageType);
		msg.addReceiver(this.agentAdviceAid);
		msg.setContent(message.toJson());
		this.send(msg);		
	}
	/**
	 * Initialize a message for the Action Agent
	 * @param message The message object to serialize and send as content to the agent
	 */
	public void sendMessageToActionAgent(mMessage message, int messageType){
		ACLMessage msg = new ACLMessage(messageType);
		msg.addReceiver(this.agentActionAid);
		msg.setContent(message.toJson());
		this.send(msg);		
	}
	/**
	 * Initialize a message for the Vote Agent
	 * @param message The message object to serialize and send as content to the agent
	 */
	public void sendMessageToVoteAgent(mMessage message, int messageType){
		ACLMessage msg = new ACLMessage(messageType);
		msg.addReceiver(this.agentVoteAid);
		msg.setContent(message.toJson());
		this.send(msg);		
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
		Debugger.println(message.getStoryTelling());
		this.send(msg);
	}
	/**
	 * Initialize a message for one agent player.
	 * @param aid The AID of the agents to whom we want to send the message
	 * @param message The message object to serialize and send as content
	 */
	public void sendMessageToOneRegisteredAgent(AID aid, mMessage message, int messageType){
		ACLMessage msg = new ACLMessage(messageType);
		msg.addReceiver(aid);
		msg.setContent(message.toJson());
		this.send(msg);
	}
	/**
	 * Initialize a message for the kb agent linked to this AgtStoryteller 
	 * @param message The message object to serialize and send as content
	 */
	public void sendMessageToKbAgent(mStorytellerKb message){
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(this.kbStoryteller);
		msg.setContent(message.toJson());
		Debugger.println(message.toJson());
		this.send(msg);		
		hasSentRequestToKb = true;
	}
}

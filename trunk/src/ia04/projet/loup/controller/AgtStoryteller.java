package ia04.projet.loup.controller;

import ia04.projet.loup.Global.GamePhases;
import ia04.projet.loup.Global.Roles;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This agent is the core of our program. Each game room must have its own Storyteller.
 * The storyteller in the Werewolfs of Miller's Hollow is the player who will tell the story, 
 * who knows who is who, who will call for the actions and verify that the rules are respected.
 * In our program it is the AgtStoryteller that will regulate the game.
 * @author aurelien
 *
 */
public class AgtStoryteller extends Agent {
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
	private static final long serialVersionUID = -1537520826022941930L;
	/** The map of the players with their role. */
	private HashMap<AID,Roles> playersMap;
	private ArrayList<AID> lastVictimsRoles;
	/** The clock that regulates the game phases and game speed. */
	private PhaseClock phaseClock;
	//TODO: lovers, charmed
	
	/**
	 * The default constructor. Start an AgtStoryteller.
	 * Attach its behaviour and initialize the internal clock.
	 */
	public AgtStoryteller(){
		super();
		this.addBehaviour(new BehaviourStoryteller(this));
		this.phaseClock = new PhaseClock(this);
	}
	
	/**
	 * Method called when a new game should start
	 */
	public void startGame(){
		this.phaseClock.startTimer();
		this.playersMap = new HashMap<AID,Roles>();
		this.lastVictimsRoles = new ArrayList<AID>();
		
		// TODO: test
		int i = 0;
		this.playersMap.put(new AID("test"+(i++)), Roles.WEREWOLF);
		this.playersMap.put(new AID("test"+(i++)), Roles.WEREWOLF);
		this.playersMap.put(new AID("test"+(i++)), Roles.WEREWOLF);
		this.playersMap.put(new AID("test"+(i++)), Roles.VILLAGER);
		this.playersMap.put(new AID("test"+(i++)), Roles.VILLAGER);
		this.playersMap.put(new AID("test"+(i++)), Roles.VILLAGER);
		this.playersMap.put(new AID("test"+(i++)), Roles.WITCH);
		this.playersMap.put(new AID("test"+(i++)), Roles.CLAIRVOYANT);
	}
	
	/**
	 * Add a player to the party room. The players do not necessarily have to be "playing".
	 * They can watch the game and participate in a game if a new game starts.
	 * @param player The AID of the new player
	 */
	public void addPlayerToParty(AID player){
		this.playersMap.put(player, Roles.DEAD);
	}
	/**
	 * Give a role to a player
	 * @param role The role to give to the player
	 * @param player The player to which we want to give the role
	 */
	public void addRoleToPlayer(Roles role, AID player){
		// TODO: Send message to the player
		this.playersMap.put(player, role);
	}

	/**
	 * Game is over the there is no werewolf alive
	 * or there are only werewolves alive 
	 * or only the two lovers are alive
	 * or all the players have been charmed
	 */
	public void checkGameIsOver(){
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
		
		if((nWolf == nMax) || (nWolf == 0) || (nCharmed == nMax-1)){ // TODO: ||�two lovers only
			this.endGameWithState(GameExitErrorCodes.GAME_OVER);
		}
	}
	/**
	 * Check at least one player has the given role
	 * @param role The role to search for
	 * @return Whether one player at least has the given role
	 */
	public boolean roleStillInGame(Roles role){
		return this.playersMap.containsValue(role);
	}
	
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
		// TODO: lots of stuff to do
		System.out.print("Storyteller says: ");
		switch(phase){
		case NONE:
			break;
		case NIGHT:
			System.out.println("NEW TURN\n\nIt is now the night. The village goes to sleep.");
			break;
		case CUPID:
			System.out.println("Cupid wakes up. He can choose two people who will deeply fall in love.");
			break;
		case LOVERS:
			System.out.println("The lovers recognize each other.");
			break;
		case THIEF:
			System.out.println("The thief can choose between two roles.");
			break;
		case GUARDIAN:
			System.out.println("The guardian can protect one person for tonight.");
			break;
		case CLAIRVOYANT:
			System.out.println("The clairvoyant can detect someone's role.");
			break;
		case WEREWOLVES:
			System.out.println("The werewolves wake up and gather to select their victim for tonight.");
			break;
		case WITCH:
			System.out.println("The witch wakes up. She can use her revive pot to save the werewolves' victim " +
					"or her deathly pot to make someone die in terrible suffering.");
			break;
		case WHITE_WOLF:
			System.out.println("The white wolf wakes up and can select his wolf's victim.");
			break;
		case RAVEN:
			System.out.println("The raven wakes up and can point to the village who he thinks is suspicious.");
			break;
		case FLUTE_PLAYER:
			System.out.println("The flute player wakes up and can charm two other people in the village.");
			break;
		case CHARMED:
			System.out.println("The people charmed by the flute player wake up and gather.");
			break;
		case DAY:
			System.out.println("It is now the day. The village wakes up.");
			break;
		case VICTIMS_REVELATION:
			System.out.println("Tonight's victims are revealed.");
			break;
		case VICTIMS_EVENT:
			System.out.println("Before their last action the victims can try a desperate move.");
			break;
		case VICTIMS_RESOLUTION:
			System.out.println("The victims die.");
			break;
		case MAYOR_ELECTION:
			System.out.println("The mayor election can begin. The village needs someone to follow! " +
					"Choose wisely because the mayor has power.");
			break;
		case HUNG_VOTE:
			System.out.println("The hanged selection begins. Who will be hung on the place today ?");
			break;
		case HUNG_REVELATION:
			System.out.println("The hung role's revealed.");
			break;
		case HUNG_EVENT:
			System.out.println("Before his hunging the hung can try a desperate move.");
			break;
		case HUNG_RESOLUTION:
			System.out.println("The hung is dead.");
			break;
		default:
			break;
		}		
	}
	/**
	 * This method is called by the internal clock when the current phase stops.
	 * The clock is actually stopped until the Storyteller does something.
	 * @param phase The phase that should be ending.
	 */
	public void endOfPhase(GamePhases phase){
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
			this.checkGameIsOver();
			break;
		case DAY:
			break;
		case VICTIMS_REVELATION:
			break;
		case VICTIMS_EVENT:
			break;
		case VICTIMS_RESOLUTION:
			this.checkGameIsOver();
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
			this.checkGameIsOver();
			break;
		default:
			break;
		}	
		phaseClock.startNextPhase();
	}
	/**
	 * Method called when the current game should stop. 
	 * Can be cause by an internal error or because the game is over (there is a winner or everyone is dead).
	 */
	public void endGameWithState(GameExitErrorCodes errorCode){
		this.phaseClock.stopTimer();
		switch(errorCode){
		case UNKNOWN_REASON:
			System.out.println("I don't know why the game stopped.");
			break;
		case BUG:
			System.out.println("Nyeeeee... There is a problem here. Sorry for the inconvenience =(");
			break;
		case NO_POSSIBLE_PHASE:
			System.out.println("There is no possible phase. Weird.");
			break;
		case TOO_FEW_PLAYERS:
			System.out.println("Hey guys! We need more people!");
			break;
		case GAME_OVER:{
			//TODO: all charmed
			//TODO: lovers
			if(this.playersMap.containsValue(Roles.WEREWOLF)){
				System.out.println("The werewolves win the game!");
			}
			else if(this.playersMap.containsValue(Roles.WHITE_WOLF)){
				System.out.println("The white wolf wins the game!");
			}
			else {
				System.out.println("The villagers win the game!");
			}
		}break;
		default:
			System.out.println("Laule. I don't even have an error code for that.");
			break;
		}
	}
	
	/**
	 * Prepare a message for a Communication Agent
	 * @param messageType The type of message to send (is it a vote, an advice, an action)
	 * @return
	 */
	public void sendMessageForCommunicationAgent(String messageType){
		// TODO: everything
		// A - Message for Vote: ask for a vote
		// B - Message for Advice: ask for an advice turn
		// C - Message for Action: ask for the realization of a phase/action, notify a player's death, the beginning of a game
	}
	
	/**
	 * Prepare and send a message to a registered agent
	 * @param messageType
	 * @param aid AID of targeted agent.
	 */
	public void sendMessageForRegisteredAgents(String messageType, AID aid, String story){
		// TODO: everything
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(aid);
		
		String Jmsg = "{type : " + messageType;
		
		// A - Start a game
			//nothing else.
		// B - Roles attribution
		if(messageType.equals("ATTRIBUTE_ROLE")){
			Jmsg += ", role : " + playersMap.get(aid);
			}
		// C - 
		if(messageType.equals("STORYTELLING")){
			Jmsg += ", storytelling : " + story;
		}
		// D - End of the current game
			//nothing else
		
		Jmsg +=" }";
		this.send(msg);
	}
}

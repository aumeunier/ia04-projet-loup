package ia04.projet.loup.roles;

import ia04.projet.loup.Debugger;
import ia04.projet.loup.Global;
import ia04.projet.loup.Global.Roles;
import ia04.projet.loup.Global.Strategies;
import ia04.projet.loup.messages.mActionRegister;
import ia04.projet.loup.messages.mMessage;
import ia04.projet.loup.messages.mPlayerDied;
import ia04.projet.loup.messages.mStorytellerPlayer;
import ia04.projet.loup.messages.mToGui;
import ia04.projet.loup.messages.mVote;
import ia04.projet.loup.messages.mVoteRegister;
import ia04.projet.loup.messages.mStorytellerPlayer.mType;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * This agent is the core of a role. Each role agent has to be a subclass of AgtRole.
 * It implements the main actions of a role: begining/end of a game, death of the player, etc...
 * @author claquette
 *
 */

public class AgtRole extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1226925844951644365L;
	/** role of the agent */
	protected Global.Roles role;
	/** The strategy in use */
	protected Global.Strategies currentStrategy;
	/** Array of the players */
	protected java.util.ArrayList<String> players;
	/** Map of the players with the corresponding confidence level */
	protected HashMap<String, ConfidenceLevel> confidenceLevel = new HashMap<String, ConfidenceLevel>();
	/** Map containing the last vote results */
	protected HashMap<String, mVote> lastVote;
	/** number of voices during a vote for the victim of the day */
	protected int voices=1;
	/** list of all the behaviours of the agent */
	protected ArrayList<RoleBehaviour> behaviours = new ArrayList<RoleBehaviour>();
	/** generate random int for the rabbit strategy */
	protected Random random = new Random(Global.random.nextLong());
	/** ID of his associated GUI */
	protected AID myGuiID;
	/** local name of the role the agent is in love with */
	protected String lover;
	
	public int getVoices() {
		return voices;
	}
	public void setVoices(int voices) {
		this.voices = voices;
	}
	/**
	 * The default constructor. Starts the agent and attach its behaviors (core + villager).
	 * Gets an access to his player's GUI
	 */
	public AgtRole(AID guiID) {
		super();
		initializeRole();
		initializeStrategy();
		//addBehaviour(new BehaviourRegister()); //TODO: useless for now
		addAndSaveBehaviour(new BehaviourRole());
		addAndSaveBehaviour(new BehaviourVillager());
		initializeConfidenceLevel();
		myGuiID=guiID;
	}
	/**
	 * Here the agent registers to the Communication agents such that he can receive 
	 * all the messages regarding the game being played
	 */
	public void registerToCommunicationAgents(){
		Debugger.println(this.getLocalName()+" is a "+this.role+"has the "+currentStrategy+"strategy.");
		// Register to AgtVote
		//AID voteAid = DFInterface.getService(this, "AgtVote");
		AID voteAid = new AID(Global.LOCALNAME_VOTE,AID.ISLOCALNAME);
		mVoteRegister mVote = new mVoteRegister(getRole());
		this.initializeMessageToCommunicationAgent(mVote, voteAid);	

		// Register to AgtAction
		//AID actionAid = DFInterface.getService(this, "AgtAction");
		AID actionAid = new AID(Global.LOCALNAME_ACTION,AID.ISLOCALNAME);
		mActionRegister mAction = new mActionRegister(getRole());
		this.initializeMessageToCommunicationAgent(mAction, actionAid);	

		// TODO: Register to AgtAdvice
	}
	/**
	 * Send a message to a communication agent in order to suscribe to its messages
	 * @param message
	 * @param agentAid
	 */
	private void initializeMessageToCommunicationAgent(mMessage message, AID agentAid){
		ACLMessage msg = new ACLMessage(ACLMessage.SUBSCRIBE);
		msg.addReceiver(agentAid);
		msg.setContent(message.toJson());
		this.send(msg);			
	}
	/** initialize the role of the agent */
	protected void initializeRole(){
		role = Global.Roles.VILLAGER;
	}
	/** initialize the strategy of thearg0 agent */
	protected void initializeStrategy(){
		currentStrategy=Global.STATEGY_VALUES.get(random.nextInt(Global.STATEGY_VALUES.size()));
	}
	/** add a behaviour and save it in Behaviours */
	protected void addAndSaveBehaviour(RoleBehaviour aBehaviour){
		this.addBehaviour(aBehaviour);
		behaviours.add(aBehaviour);
	}
	public void dispatchMessageToBehaviours(ACLMessage message){
		for(int i = 0 ; i < behaviours.size() ; ++i){
			RoleBehaviour b = behaviours.get(i);
			if((b instanceof BehaviourDead) || (!b.isDone())){
				b.roleAction(message);
			}
		}
	}
	/**
	 * Initializes the different level of confidence at the beginning of a new game.
	 * alpha - no confidence level
	 * TODO beta2 - first implementation
	 * TODO beta4 - initial state depending on previous games
	 * 
	 */
	protected void initializeConfidenceLevel(){
		//alpha: do nothing
	}
	
	/**
	 * during the day every role has the same action to do :
	 * vote to kill somebody in the village 
	 */
	protected String vote(ArrayList<String> candidates){
		if(lover!=null){ //Do not kill your lover !
			candidates.remove(lover);
		}
		switch (currentStrategy){
		case RABBIT:
			//Debugger.println("AgtRole: vote-RABBIT");
			return candidates.get(random.nextInt(candidates.size()));
		case BASIC:
			//Debugger.println(this.getLocalName()+": vote-BASIC: "+getLowestConfidence(candidates));
			return getLowestConfidence(candidates);
		case DUMMIE:
			//Debugger.println(this.getLocalName()+": vote-BASIC: "+getLowestConfidence(candidates));
			return getHighestConfidence(candidates);
		case SHEEP:
			if(lastVote==null){
				//TODO something else
					return candidates.get(random.nextInt(candidates.size()));
			}
			else{
				//Debugger.println(this.getLocalName()+": vote-SHEEP: "+getLastMostVoted(candidates,lastVote));
				return getLastMostVoted(candidates, lastVote);
			}
		default: return null;	
		}
	}
	/** Vote for the election of the first mayor */
	protected String electMayor(ArrayList<String> candidates){
		switch (currentStrategy){
		case RABBIT:
		case SHEEP: //TODO sheep elect mayor comportment
			//Debugger.println("AgtRole: electMayor-RABBIT");
			return candidates.get(random.nextInt(candidates.size()));
		case BASIC:
			//Debugger.println(this.getLocalName()+": electMayor-BASIC: "+getHighestConfidence(candidates));
			return getHighestConfidence(candidates);
		case DUMMIE:
			//Debugger.println(this.getLocalName()+": electMayor-BASIC: "+getHighestConfidence(candidates));
			return getLowestConfidence(candidates);
		default: return null;
		}
	}
	/** When he dies the mayor has to choose his successor */
	protected String nameSuccessor(ArrayList<String> candidates){
		switch (currentStrategy){
		case RABBIT:
			//Debugger.println("AgtRole: nameSuccessor-RABBIT");
			return candidates.get(random.nextInt(candidates.size()));
		case BASIC: 
			//Debugger.println("AgtRole: nameSuccessor-BASIC");			
			return getHighestConfidence(candidates);
		case DUMMIE: 
			//Debugger.println("AgtRole: nameSuccessor-BASIC");
			return getLowestConfidence(candidates);
		case SHEEP:
			if(lastVote==null){
				//TODO something else
				return candidates.get(random.nextInt(candidates.size()));
		}
		else{
			//Debugger.println(this.getLocalName()+": vote-SHEEP: "+getLastLeastVoted(candidates,lastVote));
			return getLastLeastVoted(candidates, lastVote);
		}
		default: return null;
		}
	}
	/** The mayor has to choose the victim in case of equality */
	protected String resolveEquality(ArrayList<String> candidates){
		if(lover!=null){ //Do not kill your lover !
			candidates.remove(lover);
		}
		candidates.remove(this.getLocalName());
		switch (currentStrategy){
		case RABBIT:
			//Debugger.println("AgtRole: resolveEquality-RABBIT");
			return candidates.get(random.nextInt(candidates.size()));
		case BASIC:
			//Debugger.println("AgtRole: resolveEquality-BASIC");
			return getLowestConfidence(candidates);
		case DUMMIE:
			//Debugger.println("AgtRole: resolveEquality-BASIC");
			return getHighestConfidence(candidates);
		case SHEEP:
			if(lastVote==null){
				//TODO something else
					return candidates.get(random.nextInt(candidates.size()));
			}
			else{
				//Debugger.println(this.getLocalName()+": vote-SHEEP: "+getLastMostVoted(candidates,lastVote));
				return getLastMostVoted(candidates, lastVote);
			}
		default: return null;
		}
	}
	/** Update confidence levels after a vote for the victim of the day */
	protected void updateConfidenceVotePaysan(String victimName, Global.Roles victimRole){
		for (String elector : this.lastVote.keySet()) {
			if (lastVote.get(elector).getChoice().equals(this.getLocalName())){
				/** this elector voted against me */
				confidenceLevel.get(elector).update(ConfidenceLevel.VOTEFORME);
			}
			else if(Global.BASICROLECORRESONDANCE.get(role)==Global.BASICROLECORRESONDANCE.get(victimRole))
				/** this elector voted for a player in my side */
				confidenceLevel.get(elector).update(ConfidenceLevel.VOTEFORMYROLE);
			else 
				/** this elector voted for a player in the opposite side */
			confidenceLevel.get(elector).update(ConfidenceLevel.VOTEFOROPPONENT);
		}
	}
	/** get the name of the player with the highest confidence level in a list of players */
	protected String getHighestConfidence(ArrayList<String> players){
		int max=0;
		String playerMax=null;
		for(String player : players){
			if(confidenceLevel.get(player).getLevel()>max){
				playerMax=player;
				max=confidenceLevel.get(player).getLevel();
			}
		}
		return playerMax;
	}
	/** get the name of the player with the lowest confidence level in a list of players */
	protected String getLowestConfidence(ArrayList<String> players){
		int min=100;
		String playerMin=null;
		for(String player : players){
			if(confidenceLevel.get(player).getLevel()<min)
				playerMin=player;
				min=confidenceLevel.get(player).getLevel();
		}
		return playerMin;
	}

	public void setLastVote(HashMap<String, mVote> lastVote) {
		this.lastVote = lastVote;
	}
	public Global.Roles getRole() {
		return role;
	}
	public void setRole(Global.Roles role) {
		this.role = role;
	}
	
	/** Send a message to AgtVote saying that the role is dead */
	public void iAmDead(ACLMessage query){
		// Remove the behaviours
		for(RoleBehaviour aBehaviour: behaviours){
			aBehaviour.setIsDone();
			//removeBehaviour(aBehaviour);
		}
		addBehaviour(new BehaviourDead());
		
		// Send message to Vote
		mPlayerDied message = new mPlayerDied();
		message.setDeadName(this.getLocalName());		
		AID voteAid = new AID(Global.LOCALNAME_VOTE,AID.ISLOCALNAME);
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(voteAid);
		msg.setContent(message.toJson());
		this.send(msg);
		
		// Send message to player to say its over
		mPlayerDied msgcontent = new mPlayerDied();
		msgcontent.setIsOver(true);
		msgcontent.setDeadName(getLocalName());
		ACLMessage messageForPlayer = query.createReply();
		messageForPlayer.setPerformative(ACLMessage.INFORM);
		messageForPlayer.setContent(msgcontent.toJson());
		this.send(messageForPlayer);
	}
	
	/**
	 * A new mayor has been elected !
	 */
	public void newMayorElected(String mayorLocalName){
		if(mayorLocalName.equals(this.getLocalName())){
			this.addAndSaveBehaviour(new BehaviourMayor());
		}
	}

	protected String getLastMostVoted(ArrayList<String> players, HashMap<String, mVote> aVote){
		String maxPlayer=null; int max=0;
		for(String player : players){
			int tmp=0;
			for(String elector : aVote.keySet()){
				if(aVote.get(elector).getChoice().equals(player))
					tmp++;
			}
			if(tmp > max){
				max=tmp;
				maxPlayer=player;
			}	
		}
		if(max==0){ //no vote for survivors
			return players.get(random.nextInt(players.size()));	
		}
		return maxPlayer; 
	}
	
	protected String getLastLeastVoted(ArrayList<String> players, HashMap<String, mVote> aVote){
		//TODO if equality use confidenceLevels to tie
		String minPlayer=null; int min=100;
		for(String player : players){
			int tmp=0;
			for(String elector : aVote.keySet()){
				if(aVote.get(elector).getChoice().equals(player))
					tmp++;
			}
			if(tmp < min){
				min=tmp;
				minPlayer=player;
			}	
		}
		if(min==100){ //no vote for survivors
			return players.get(random.nextInt(players.size()));	
		}
		return minPlayer; 
	}
	
	protected void setLover(String myLover, Roles hisRole){
		lover = myLover;
		confidenceLevel.get(lover).update(ConfidenceLevel.ILOVEHIM);
	}
}

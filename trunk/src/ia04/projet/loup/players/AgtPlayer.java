package ia04.projet.loup.players;

import ia04.projet.loup.Global;
import ia04.projet.loup.messages.mStorytellerPlayer;
import ia04.projet.loup.roles.AgtRole;
import ia04.projet.loup.roles.AgtWerewolf;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;


public class AgtPlayer extends Agent {

	private static final long serialVersionUID = -3215896432211766320L;

	private AID RoleID, GuiID;

	public AgtPlayer() {
		super();
		this.addBehaviour(new BehaviourPlayer());
		setRoleID(null);
	}

	/**
	 * Send identification to storyTeller
	 * @param stryTeller The AgtStoryTeller AID
	 * @author Guillaume
	 * **/
	public void Register(AID stryTeller){
		mStorytellerPlayer mObj = new mStorytellerPlayer();
		mObj.setType(mStorytellerPlayer.mType.REGISTER);
		String Jmsg = mObj.toJson();
		ACLMessage msg = new ACLMessage(ACLMessage.SUBSCRIBE);
		msg.setContent(Jmsg);
		msg.addReceiver(stryTeller);
		this.send(msg);
	}

	/**
	 * Answer to storyTeller request to join a game
	 * @param msg Request from the StoryTeller
	 * @param value This player answer value.
	 * @author Guillaume
	 * **/
	public void JoinGame(ACLMessage msg, mStorytellerPlayer mObj, boolean value){
		//Accept to join the beginning game
		ACLMessage response = msg.createReply();
		mObj.setParticipateInGame(value);
		String Jmsg = mObj.toJson();
		response.setContent(Jmsg);
		this.send(response);						
	}

	/**
	 * Instantiate associated role agent
	 * @param msg Role attribution message from storyTeller
	 * @throws StaleProxyException 
	 * @author Guillaume
	 * **/
	public void RoleInstance(Global.Roles role) throws StaleProxyException{
		// RoleAgent instantiation, behaviours are added in the roles	
		AgtRole agtR = null;
		switch(role){
		case VILLAGER:
			agtR = new AgtRole();
			break;
		case WEREWOLF:			
			agtR = new AgtWerewolf();
			break;
			/*case CUPID:
				agtR = new AgtRole();
			case THIEF:
				agtR = new AgtRole();
			case GUARDIAN:
				agtR = new AgtRole();
			case CLAIRVOYANT:
				agtR = new AgtRole();
			case WITCH:
				agtR = new AgtRole();
			case WHITEWOLF:
				agtR = new AgtRole();
			case RAVEN:
				agtR = new AgtRole();
			case FLUTEPLAYER:
				agtR = new AgtRole();
			case HUNTER:
				agtR = new AgtRole();
			case SCAPEGOAT:
				agtR = new AgtRole();
			case VILLAGEIDIOT:
				agtR = new AgtRole();
			case VILLAGESAGE:
				agtR = new AgtRole(); 
				*/
		default:
			agtR = new AgtRole();
			break;
		}
		
		// Create the role on the platform and register it
		AgentContainer mc = this.getContainerController();
		this.getContainerController().acceptNewAgent(this.getLocalName()+"Role", agtR).start();
		agtR.registerToCommunicationAgents();
		setRoleID(agtR.getAID());
	}

	/**
	 * Transfer to AgtGui the current game phase
	 * @param msg Informative message from storyTeller
	 * @author Guillaume
	 * **/
	public void StoryTransfertToGui(ACLMessage msg){
		ACLMessage toGui = new ACLMessage(ACLMessage.INFORM);
		toGui.setContent(msg.getContent());
		this.send(toGui);
	}

	/**Setters and getters*/
	public void setRoleID(AID roleID) {
		RoleID = roleID;
	}

	public AID getRoleID() {
		return RoleID;
	}

	public AID getGuiID() {
		return GuiID;
	}

	public void setGuiID(AID guiID) {
		GuiID = guiID;
	}
}
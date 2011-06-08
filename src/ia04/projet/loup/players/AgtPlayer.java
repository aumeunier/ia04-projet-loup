package ia04.projet.loup.players;

import java.util.ArrayList;

import ia04.projet.loup.Global;
import ia04.projet.loup.gui.AgtPlayerGui;
import ia04.projet.loup.messages.mStorytellerPlayer;
import ia04.projet.loup.messages.mToGui;
import ia04.projet.loup.roles.AgtRole;
import ia04.projet.loup.roles.AgtWerewolf;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;


public class AgtPlayer extends Agent {

	private static final long serialVersionUID = -3215896432211766320L;

	private AID RoleID, GuiID;

	public AgtPlayer() throws StaleProxyException {
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

	public void GuiCreation() throws StaleProxyException{
		AgtPlayerGui guiAgt = new AgtPlayerGui(this.getAID());
		AgentContainer mc = this.getContainerController();
		mc.acceptNewAgent(this.getLocalName()+Global.LOCALNAME_SUFFIX_GUI, guiAgt).start();
		this.setGuiID(guiAgt.getAID());
	}
	/**
	 * Answer to storyTeller request to join a game
	 * @param msg Request from the StoryTeller
	 * @param value This player answer value.
	 * @author Guillaume
	 * **/
	public void JoinGame(ACLMessage msg, mStorytellerPlayer mObj){
		//Accept to join the beginning game
		ACLMessage response = msg.createReply();
		mObj.setParticipateInGame(true);
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
			agtR = new AgtRole(GuiID);
			break;
		case WEREWOLF:			
			agtR = new AgtWerewolf(GuiID);
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
			agtR = new AgtRole(GuiID);
			break;
		}
		
		// Create the role on the platform and register it
		AgentContainer mc = this.getContainerController();
		mc.acceptNewAgent(this.getLocalName()+Global.LOCALNAME_SUFFIX_ROLE, agtR).start();
		agtR.registerToCommunicationAgents();
		setRoleID(agtR.getAID());
	}

	/**
	 * Transfer to AgtGui the current game phase
	 * @param type message type to inform gui
	 * @param val value to transfer
	 * @author Guillaume
	 * **/
	public void TransfertToGui(mToGui.mType type, String val){
		ACLMessage toGui = new ACLMessage(ACLMessage.INFORM);
		
		mToGui msg = new mToGui();
		msg.setType(type);
		msg.setValue(val);
		
		toGui.setContent(msg.toJson());
		toGui.addReceiver(GuiID);
		
		this.send(toGui);
		
	}
	
	/**
	 * Transfer to AgtGui the current game phase
	 * @param type message type to inform gui
	 * @param list player list to gui
	 * @author Guillaume
	 * **/
	public void TransfertToGui(mToGui.mType type, ArrayList<String> list){
		ACLMessage toGui = new ACLMessage(ACLMessage.INFORM);
		
		mToGui msg = new mToGui();
		msg.setType(type);
		msg.setPlayers(list);
		
		toGui.setContent(msg.toJson());
		toGui.addReceiver(GuiID);
		
		this.send(toGui);
		
	}
	
	/**
	 * Transfer to AgtGui the current game phase
	 * @param type message type to inform gui
	 * @param role role given by the storyTeller
	 * @author Guillaume
	 * **/
	public void TransfertToGui(mToGui.mType type, Global.Roles role){
		ACLMessage toGui = new ACLMessage(ACLMessage.INFORM);
		
		mToGui msg = new mToGui();
		msg.setType(type);
		msg.setRole(role);
		
		toGui.setContent(msg.toJson());
		toGui.addReceiver(GuiID);
		
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
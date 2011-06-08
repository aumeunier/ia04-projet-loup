package ia04.projet.loup.players;

import ia04.projet.loup.Global;
import ia04.projet.loup.Global.Roles;
import ia04.projet.loup.gui.GuiBot;
import ia04.projet.loup.messages.mStorytellerPlayer;
import ia04.projet.loup.roles.AgtCupid;
import ia04.projet.loup.roles.AgtGuardian;
import ia04.projet.loup.roles.AgtRole;
import ia04.projet.loup.roles.AgtWerewolf;
import jade.core.AID;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;

import java.util.HashMap;


public class AgtPlayer extends GuiAgent {

	private static final long serialVersionUID = -3215896432211766320L;
	public static final int CHOOSE_TYPE = 0;
	
	private GuiBot myGui;
	private AID RoleID;
	private HashMap<String,Integer> Confidences;

	/**
	 * Constructor
	 * @throws StaleProxyException
	 */
	public AgtPlayer() throws StaleProxyException {
		super();
		this.addBehaviour(new BehaviourPlayer());
		setRoleID(null);
	}
	
	/**
	 * Handles event on the GUI
	 * @param GuiEvent guiEvent
	 */
	protected void onGuiEvent(GuiEvent guiEvent) {
		switch(guiEvent.getType()){
			case AgtPlayer.CHOOSE_TYPE: /** TO COMPLTE **/ break;
			default: break;
		}
	}
	
	/**
	 * Add an announce to the StoryTeller panel
	 * @param storyTelling
	 */
	public void setStoryView(String storyTelling) {
		myGui.setStoryView(storyTelling);
		myGui.repaint();
	}
	
	/**
	 * Set the Status of the player in GUI (JTextBox)
	 * @param string
	 */
	public void setStat(String string) {
		myGui.setStat(string);
		myGui.repaint();
	}
	
	/**
	 * Set the Role of the player in GUI (JTextBox)
	 * @param role
	 */
	public void setRole(Roles role) {
		myGui.setRole(role.toString());
		myGui.repaint();
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
	 * Create the GUI
	 * @throws StaleProxyException
	 */
	public void GuiCreation() throws StaleProxyException{
		if(Global.IS_GUI_ACTIVATED){
			myGui = new GuiBot(this.getLocalName(), this);
			myGui.setVisible(true);
		}
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
			agtR = new AgtRole(this.getAID());
			break;
		case WEREWOLF:			
			agtR = new AgtWerewolf(this.getAID());
			break;
		case CUPID:
			agtR = new AgtCupid(this.getAID());
			break;
		case GUARDIAN:
			agtR = new AgtGuardian(this.getAID());
			break;
			/*
			case THIEF:
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
			agtR = new AgtRole(this.getAID());
			break;
		}
		
		// Create the role on the platform and register it
		AgentContainer mc = this.getContainerController();
		mc.acceptNewAgent(this.getLocalName() + Global.LOCALNAME_SUFFIX_ROLE, agtR).start();
		agtR.registerToCommunicationAgents();
		setRoleID(agtR.getAID());
	}

	
	/**Setters and getters*/
	public void setRoleID(AID roleID) {
		RoleID = roleID;
	}

	public AID getRoleID() {
		return RoleID;
	}
}
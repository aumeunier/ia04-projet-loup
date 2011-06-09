package ia04.projet.loup.players;

import ia04.projet.loup.Global;
import ia04.projet.loup.Global.Roles;
import ia04.projet.loup.gui.GuiBot;
import ia04.projet.loup.gui.GuiPlayer;
import ia04.projet.loup.messages.mGuiAction;
import ia04.projet.loup.messages.mStorytellerPlayer;
import ia04.projet.loup.roles.AgtClairvoyant;
import ia04.projet.loup.roles.AgtCupid;
import ia04.projet.loup.roles.AgtGuardian;
import ia04.projet.loup.roles.AgtHunter;
import ia04.projet.loup.roles.AgtRole;
import ia04.projet.loup.roles.AgtWerewolf;
import ia04.projet.loup.roles.AgtWitch;
import ia04.projet.loup.roles.ConfidenceLevel;
import jade.core.AID;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;


public class AgtPlayer extends GuiAgent {

	private static final long serialVersionUID = -3215896432211766320L;
	public static final int CHOOSE_TYPE = 0;
	
	private GuiBot myGui;
	private AID RoleID, myKB;
	private HashMap<String,ConfidenceLevel> Confidences;
	private boolean human = false;
	
	/**
	 * Constructor
	 * @throws StaleProxyException
	 */
	public AgtPlayer(boolean human) throws StaleProxyException {
		super();
		this.addBehaviour(new BehaviourPlayer());
		setRoleID(null);
		this.human = human;
	}
	
	/**
	 * Handles event on the GUI
	 * @param GuiEvent guiEvent
	 */
	protected void onGuiEvent(GuiEvent guiEvent) {
		switch(guiEvent.getType()){
			case AgtPlayer.CHOOSE_TYPE: 
				ACLMessage message = new ACLMessage(ACLMessage.INFORM);
				message.addReceiver(RoleID);
				String choice = (String)guiEvent.getParameter(0);
				mGuiAction aGuiAction = new mGuiAction();
				aGuiAction.setChoice(choice.substring(0, choice.indexOf('-')-1));
				message.setContent(aGuiAction.toJson());
				this.send(message);
				break;
			default: break;
		}
	}
	
	/**
	 * Add an announce to the StoryTeller panel
	 * @param storyTelling
	 */
	public void setStoryView(String storyTelling) {
		if(Global.IS_GUI_ACTIVATED){
			myGui.setStoryView(storyTelling);
			myGui.repaint();
		}
	}
	
	/**
	 * Set the Status of the player in GUI (JTextBox)
	 * @param string
	 */
	public void setStat(String string) {
		if(Global.IS_GUI_ACTIVATED){
			myGui.setStat(string);
			myGui.repaint();
		}
	}
	
	/**
	 * Set the Role of the player in GUI (JTextBox)
	 * @param role
	 */
	public void setRole(Roles role) {
		if(Global.IS_GUI_ACTIVATED){
			myGui.setRole(role.toString());
			myGui.repaint();
		}
	}
	
	public void setConfidenceLevel(HashMap<String, ConfidenceLevel> hashMap){
		if(Global.IS_GUI_ACTIVATED){
			myGui.clearTheList();
			for(Entry<String, ConfidenceLevel> entry : hashMap.entrySet()){
				myGui.addPlayerToTheList(entry.getKey() + " - " + entry.getValue().getLevel());
			}
			myGui.repaint();
		}
	}
	
	public void waitForAction() {
		if(Global.IS_GUI_ACTIVATED){
			((GuiPlayer)myGui).enableVote();
		}
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
			if(human){
				myGui = new GuiPlayer(this.getLocalName(), this);
			}else{
				myGui = new GuiBot(this.getLocalName(), this);
			}
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
		//TODO: do not forget to put them
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
		case HUNTER:
			agtR = new AgtHunter(this.getAID());
			break;
		case CLAIRVOYANT:
			agtR = new AgtClairvoyant(this.getAID());
			break;
		case WITCH:
			agtR = new AgtWitch(this.getAID());
			break;
			/*
			case THIEF:
				agtR = new AgtRole();
				break;
			case WHITEWOLF:
				agtR = new AgtRole();
				break;
			case RAVEN:
				agtR = new AgtRole();
				break;
			case FLUTEPLAYER:
				agtR = new AgtRole();
				break;
			case SCAPEGOAT:
				agtR = new AgtRole();
				break;
			case VILLAGEIDIOT:
				agtR = new AgtRole();
				break;
			case VILLAGESAGE:
				agtR = new AgtRole(); 
				break;
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
	
	/** generate random int for the rabbit strategy */
	protected Random random = new Random(Global.random.nextLong());
	
	/**Setters and getters*/
	public void setRoleID(AID roleID) {
		RoleID = roleID;
	}

	public AID getRoleID() {
		return RoleID;
	}

	public GuiBot getMyGui() {
		return myGui;
	}

	public void setMyGui(GuiBot myGui) {
		this.myGui = myGui;
	}

	public AID getMyKB() {
		return myKB;
	}

	public void setMyKB(AID myKB) {
		this.myKB = myKB;
	}

	public HashMap<String, ConfidenceLevel> getConfidences() {
		return Confidences;
	}

	public void setConfidences(HashMap<String, ConfidenceLevel> confidences) {
		Confidences = confidences;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static int getChooseType() {
		return CHOOSE_TYPE;
	}
	
}
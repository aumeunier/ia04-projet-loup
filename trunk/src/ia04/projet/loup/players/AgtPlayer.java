package ia04.projet.loup.players;

import ia04.projet.loup.Global;
import ia04.projet.loup.roles.AgtRole;
import ia04.projet.loup.roles.BehaviourRole;
import ia04.projet.loup.roles.BehaviourVillager;
import ia04.projet.loup.roles.BehaviourWerewolf;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.StaleProxyException;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


public class AgtPlayer extends Agent {
	
	private static final long serialVersionUID = -3215896432211766320L;

	private AID RoleID, GuiID;

	public AgtPlayer(){
		super();
		this.addBehaviour(new BehaviourPlayer());
		setRoleID(null);
	}
	
	/**
	 * Send identification to storyTeller
	 * @param stryTeller The AgtStoryTeller AID
	 * **/
	public void Register(AID stryTeller){
		
		String Jmsg ="{type : REGISTER}";
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setContent(Jmsg);
		msg.addReceiver(stryTeller);
		this.send(msg);
	}
	
	/**
	 * Answer to storyTeller request to join a game
	 * @param msg Request from the StoryTeller
	 * @param value This player answer value.
	 * **/
	public void JoinGame(ACLMessage msg, String value){
		//Accept to join the beginning game
		ACLMessage response = msg.createReply();
		String Jmsg = "{type : REGISTER , value :" + value +"}";
		response.setContent(Jmsg);
		this.send(response);
	}
	
	/**
	 * Instantiate associated role agent
	 * @param msg Role attribution message from storyTeller
	 * **/
	public void RoleInstance(ACLMessage msg){
		//RoleAgent instantiation
		ObjectMapper mapper = new ObjectMapper();
		try{
		JsonNode jRoot = mapper.readValue(msg.getContent(),JsonNode.class);
		String role = jRoot.path("role").getTextValue();
		
		AgtRole agtR = null;
		switch(Global.Roles.valueOf(role)){
			case VILLAGER:
				agtR = new AgtRole();	
				agtR.addBehaviour(new BehaviourVillager());
			case WEREWOLF:
				agtR = new AgtRole();
				agtR.addBehaviour(new BehaviourWerewolf());
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
			case WHITE_WOLF:
				agtR = new AgtRole();
			case RAVEN:
				agtR = new AgtRole();
			case FLUTE_PLAYER:
				agtR = new AgtRole();
			case HUNTER:
				agtR = new AgtRole();
			case SCAPEGOAT:
				agtR = new AgtRole();
			case VILLAGE_IDIOT:
				agtR = new AgtRole();
			case VILLAGE_SAGE:
				agtR = new AgtRole();*/
		}
		
		agtR.addBehaviour(new BehaviourRole());
		this.getContainerController().acceptNewAgent(this.getAID().toString()+"Role", agtR);
		setRoleID(agtR.getAID());
		
		} catch (StaleProxyException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Transfer to AgtGui the current game phase
	 * @param msg Informative message from storyTeller
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
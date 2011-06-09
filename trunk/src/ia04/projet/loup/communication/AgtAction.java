package ia04.projet.loup.communication;

import ia04.projet.loup.Debugger;
import ia04.projet.loup.Global;
import ia04.projet.loup.Global.Roles;
import ia04.projet.loup.messages.mAction;
import ia04.projet.loup.messages.mActionClairvoyant;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Agent who handles the action of the special characters
 * 
 * @author paul
 */
public class AgtAction extends Agent {

	private static final long serialVersionUID = 1L;
	/** Map of the registered players */
	private HashMap<AID, Roles> playersMap = new HashMap<AID, Roles>();
	/** AID of the AgtStoryteller */
	private AID agtStoryteller;
	/** Number of actions in progress */
	private int nbActionsInProgress;

	/**
	 * Constructor
	 */
	public AgtAction() {
		super();
		this.addBehaviour(new BehaviourAction(this));
	}
	
	/**
	 * Registers its service into the DF
	 */
	public void registerServiceToDf(){
		/*
		 * FIXME: DFInterface problems
		ServiceDescription sd = new ServiceDescription();
		sd.setType("AgtAction");
		sd.setName(this.getLocalName());
		DFInterface.registerService(this, sd);
		*/
	}

	/**
	 * Add a player to the players map
	 * 
	 * @param sender
	 * @param role
	 */
	public void addPlayer(AID sender, Roles role) {
		playersMap.put(sender, role);
	}

	/**
	 * Ask the concerned Role to perform their action
	 * 
	 * @param anActionRequest
	 */
	public void performAction(mAction anActionRequest) {
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		if(anActionRequest.getRole().equals(Roles.WITCH)){
			anActionRequest.setTargetSaved(anActionRequest.getTargetSaved().concat(Global.LOCALNAME_SUFFIX_ROLE));
		}
		message.setContent(anActionRequest.toJson());
		
		nbActionsInProgress = 0;

		for (Entry<AID, Roles> entry : playersMap.entrySet()) {
			if (entry.getValue().equals(anActionRequest.getRole())) {
				message.addReceiver(entry.getKey());
				nbActionsInProgress++;
			}
		}
		this.send(message);
	}

	/**
	 * When the role agent has finished its action
	 * @param anAction The action that was performed
	 */
	public void addAction(mAction anAction, AID performer) {
		nbActionsInProgress--;
		if (nbActionsInProgress < 0){
			Debugger.println("Should Never Happened: More Actions than expected.");			
		}
		else {
			ACLMessage message = new ACLMessage(ACLMessage.INFORM);
			
			// Prepare the answer to storyteller
			if(anAction.getTargetKilled() != null && (!anAction.getTargetKilled().equals(anAction.getPerformer()))){
				anAction.setTargetKilled(anAction.getTargetKilled().replace(Global.LOCALNAME_SUFFIX_ROLE, ""));				
			}
			else {
				anAction.setTargetKilled(null);
			}
			if(anAction.getTargetSaved() != null){
				anAction.setTargetSaved(anAction.getTargetSaved().replace(Global.LOCALNAME_SUFFIX_ROLE, ""));				
			}
			anAction.setPerformer(performer.getLocalName().replace(Global.LOCALNAME_SUFFIX_ROLE, ""));
						
			// Send the message to storyteller
			message.setContent(anAction.toJson());
			message.addReceiver(agtStoryteller);
			this.send(message);
		}
	}
	
	/**
	 * If the agent is the clairvoyant we need to send him back the role of his target
	 * @param anAction
	 * @param clairvoyant
	 */
	public void addClairvoyantAction(mActionClairvoyant anAction, AID clairvoyant){
		nbActionsInProgress--;
		if (nbActionsInProgress < 0){
			Debugger.println("Should Never Happened: More Actions than expected.");			
		}
		else {
			
			// Send the role of the target to the clairvoyant
			ACLMessage message = new ACLMessage(ACLMessage.INFORM);		
			AID playerAid = new AID(anAction.getChosenPlayer(),AID.ISLOCALNAME);
			anAction.setRole(this.playersMap.get(playerAid));
			message.setContent(anAction.toJson());
			message.addReceiver(clairvoyant);
			this.send(message);

			// Notify the storyteller: the action is over
			ACLMessage answerToStory = new ACLMessage(ACLMessage.INFORM);
			mAction answer = new mAction(Roles.CLAIRVOYANT);	
			answer.setPerformer(clairvoyant.getLocalName().replace(Global.LOCALNAME_SUFFIX_ROLE, ""));
			answer.setTargetSaved(playerAid.getLocalName().replace(Global.LOCALNAME_SUFFIX_ROLE, ""));
			answerToStory.setContent(answer.toJson());
			answerToStory.addReceiver(agtStoryteller);
			this.send(answerToStory);
		}
	}

	/**
	 * @param agtStoryteller
	 *            the agtStoryteller to set
	 */
	public void setAgtStoryteller(AID agtStoryteller) {
		this.agtStoryteller = agtStoryteller;
	}

	/**
	 * @return the agtStoryteller
	 */
	public AID getAgtStoryteller() {
		return agtStoryteller;
	}

}

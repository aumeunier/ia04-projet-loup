package ia04.projet.loup.communication;

import ia04.projet.loup.DFInterface;
import ia04.projet.loup.Debugger;
import ia04.projet.loup.Global.Roles;
import ia04.projet.loup.messages.mAction;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.ServiceDescription;
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
		mAction anAction = new mAction();
		message.setContent(anAction.toJson());

		nbActionsInProgress = 0;

		for (Entry<AID, Roles> entry : playersMap.entrySet()) {
			if (entry.getValue() == anActionRequest.getRole()) {
				message.addReceiver(entry.getKey());
				nbActionsInProgress++;
			}
		}

		this.send(message);
	}

	/**
	 * 
	 * @param anAction
	 */
	public void addAction(mAction anAction, AID performer) {
		nbActionsInProgress--;
		if (nbActionsInProgress < 0)
			Debugger.println("Should Never Happened: More Actions than expected.");
		else {
			ACLMessage message = new ACLMessage(ACLMessage.INFORM);
			anAction.setPerformer(performer.getLocalName());
			message.setContent(anAction.toJson());
			message.addReceiver(agtStoryteller);
			this.send(message);
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

package ia04.projet.loup.roles;

import ia04.projet.loup.messages.mToGui;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.HashMap;
public class ConfidenceLevelManager {

	/** Map of the players with the corresponding confidence level */
	protected HashMap<String, ConfidenceLevel> confidenceLevelMap = new HashMap<String, ConfidenceLevel>();
	/** Agents who listen to this ConfidenceLevels **/
	protected ArrayList<AID> listeners = new ArrayList<AID>();
	/** My Agent **/
	private Agent myAgent;
	
	public ConfidenceLevelManager(Agent agt){
		this.myAgent = agt;
	}
	
	public void put(String name, ConfidenceLevel confidenceLevel){
		this.confidenceLevelMap.put(name, confidenceLevel);
		informListeners();
	}
	
	public void remove(String name){
		this.confidenceLevelMap.remove(name);
		informListeners();
	}
	
	public void update(String name, int value){
		this.confidenceLevelMap.get(name).update(value);
		informListeners();
	}
	
	public int getLevel(String name) {
		return this.confidenceLevelMap.get(name).getLevel();
	}
	
	public void addListener(AID aid){
		this.listeners.add(aid);
	}
	
	public void removeListerner(AID aid){
		this.listeners.remove(aid);
	}
	
	private void informListeners(){
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		for(AID aid : listeners){
			message.addReceiver(aid);
		}
		
		mToGui m = new mToGui();
		m.setConfidenceLevelMap(confidenceLevelMap);
		message.setContent(m.toJson());
		myAgent.send(message);
	}
}

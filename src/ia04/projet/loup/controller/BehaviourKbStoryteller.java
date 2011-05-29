package ia04.projet.loup.controller;

import ia04.projet.loup.Global;
import ia04.projet.loup.messages.mMessage;
import ia04.projet.loup.messages.mStorytellerKb;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class BehaviourKbStoryteller extends Behaviour {
	private static final String FAILURE_ANSWER = "FAILURE";

	static final String prefixWerewolves = "prefix werewolves: <http://www.utc.fr/werewolves#> ";
	static final String prefixSkos = "prefix skos: <http://www.w3.org/2004/02/skos/core#> ";
	static final String prefixRdfs = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> ";
	static final String prefixOwl = "prefix owl: <http://www.w3.org/2002/07/owl#> ";
	static final String prefixRdf = "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
	static final String prefixDct = "prefix dct: <http://purl.org/dc/terms/> ";
	static final String prefixDc = "prefix dc: <http://purl.org/dc/elements/1.1/> ";
	static final String prefixWot = "prefix wot: <http://xmlns.com/wot/0.1/> ";
	static final String prefixVs = "prefix vs: <http://www.w3.org/2003/06/sw-vocab-status/ns#> ";
	static final String prefixFoaf = "prefix foaf: <http://xmlns.com/foaf/0.1/> ";
	static final String allPrefixes = prefixWerewolves+prefixSkos+prefixRdfs+prefixOwl+prefixRdf+prefixDct+prefixDc+prefixWot+prefixVs+prefixFoaf;

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if (msg != null){
			// Get the message
			ACLMessage response = msg.createReply();
			String msgString = msg.getContent();			
			mMessage generalMessage = mMessage.parseJson(msgString, mStorytellerKb.class);

			// Analyse the message
			String responseStr = FAILURE_ANSWER;
			if(generalMessage instanceof mStorytellerKb){
				responseStr = queryAnalysis((mStorytellerKb)generalMessage);
			}

			// Answer
			response.setContent(responseStr);
			if(responseStr.equals(FAILURE_ANSWER)){
				response.setPerformative(ACLMessage.FAILURE);
			}
			else {
				response.setPerformative(ACLMessage.INFORM);
			}
			myAgent.send(response);
		}
	}

	@Override
	public boolean done() {
		return false;
	}

	/**
	 * Analyze the query and perform it if it is valid
	 * @param json The JSON string containing both the type of the query and the query itself
	 * @return The result in a JSON from
	 */
	public String queryAnalysis(mStorytellerKb message){
		String result = FAILURE_ANSWER;
		mStorytellerKb response = new mStorytellerKb();
		response.setType(message.getType());
		response.setNbPlayers(message.getNbPlayers());
		String queryString = allPrefixes;

		// Query treatment depends on the message's type
		try {
			switch(message.getType()){
			case GET_ROLE:{
				// Start the query
				queryString += "\nSELECT ?x WHERE { ?x a werewolves:Role. }";
				result = ((AgtKbStoryteller)myAgent).runExecQuery(queryString);

				// Get only the results, without the prefixes
				ObjectMapper m = new ObjectMapper();
				JsonNode rootNode;
				rootNode = m.readValue(result, JsonNode.class);
				JsonNode node = rootNode.path("results").path("bindings");
				Iterator<JsonNode> itr = node.getElements();
				while (itr.hasNext()) {
					JsonNode element = itr.next();
					JsonNode nameNode = element.get("x");
					String role = nameNode.path("value").getValueAsText();					
					response.addPossibleRole(Global.Roles.valueOf(role.replace("http://www.utc.fr/werewolves#", "").toUpperCase()));
				}
			}	break;
			
			case GET_GAME_COMPOSITION: {
				// Get the number of players we want
				int nbPlayers = response.getNbPlayers();
				
				// Create the query
				queryString += "\nSELECT ?c ?x ?nV ?nW WHERE { " +
						"{" +
						"	?c a werewolves:GameComposition ;" +
						"		owl:cardinality "+nbPlayers+";" +
						"		foaf:knows ?x;" +
						"		rdfs:subClassOf [ a werewolves:NbVillagers;" +
						"			owl:cardinality ?nV ];" +
						"		rdfs:subClassOf [ a werewolves:NbWerewolves;" +
						"			owl:cardinality ?nW ]." +
						"}" +
						"}";
				result = ((AgtKbStoryteller)myAgent).runExecQuery(queryString);

				// Get the results in a form we are interested in
				ObjectMapper m = new ObjectMapper();
				JsonNode rootNode;
				rootNode = m.readValue(result, JsonNode.class);
				JsonNode node = rootNode.path("results").path("bindings");
				Iterator<JsonNode> itr = node.getElements();
				HashMap<String,ArrayList<Global.Roles>> compositions = new HashMap<String,ArrayList<Global.Roles>>();
				// Get the composition names
				while (itr.hasNext()) {
					JsonNode element = itr.next();
					String compositionName = element.get("c").get("value").getTextValue().replace("http://www.utc.fr/werewolves#", "");
					compositions.put(compositionName, new ArrayList<Global.Roles>());
				}
				itr = node.getElements();
				// Add the roles to their composition
				while (itr.hasNext()) {
					JsonNode element = itr.next();
					String compositionName = element.get("c").get("value").getTextValue().replace("http://www.utc.fr/werewolves#", "");
					String compositionRole = element.get("x").get("value").getTextValue().
						replace("http://www.utc.fr/werewolves#", "").toUpperCase();	
					int nbRoles = 1;
					Global.Roles roleToAdd = Global.Roles.valueOf(compositionRole);
					if(roleToAdd.equals(Global.Roles.VILLAGER)){
						nbRoles = element.get("nV").get("value").getValueAsInt();
					}
					else if(roleToAdd.equals(Global.Roles.WEREWOLF)){
						nbRoles = element.get("nW").get("value").getValueAsInt();
					}
					for(int i = 0; i < nbRoles ; ++i){
						compositions.get(compositionName).add(roleToAdd);
					}
				}
				
				// Set the possible compositions in the message that will be sent back to the Storyteller agent
				response.setCompositions(compositions);
			}	break;
			
			case GET_FILTER_COMPOSITION:
				// TODO:
				break;
			}		
			
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response.toJson();
	}
}

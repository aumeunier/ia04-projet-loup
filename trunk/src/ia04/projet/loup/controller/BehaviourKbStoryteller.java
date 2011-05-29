package ia04.projet.loup.controller;

import java.io.IOException;
import java.util.Iterator;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import ia04.projet.loup.Global;
import ia04.projet.loup.messages.mMessage;
import ia04.projet.loup.messages.mStorytellerKb;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

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

		// Query treatment depends on the message's type
		try {
			switch(message.getType()){
			case GET_ROLE:
				// Start the query
				String queryString = allPrefixes+"\nSELECT ?x WHERE { ?x a werewolves:Role. }";
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
				break;
			case GET_GAME_COMPOSITION:
				// TODO:
				break;
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

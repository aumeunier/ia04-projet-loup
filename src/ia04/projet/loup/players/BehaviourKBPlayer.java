package ia04.projet.loup.players;

import ia04.projet.loup.controller.AgtKbStoryteller;
import ia04.projet.loup.messages.mMessage;
import ia04.projet.loup.messages.mPlayerKb;
import ia04.projet.loup.messages.mPlayerKb.mType;
import ia04.projet.loup.messages.mStorytellerKb;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class BehaviourKBPlayer extends Behaviour {
	private static final String FAILURE_ANSWER = "FAILURE";

	static final String prefixKbPlayer = "prefix werewolves: <http://www.utc.fr/kbfplyer#> ";
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
	static final String allPrefixes = prefixWerewolves+prefixSkos+prefixRdfs+prefixOwl+prefixRdf+prefixDct+prefixDc+prefixWot+prefixVs+prefixFoaf+prefixKbPlayer;

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
			if(generalMessage instanceof mPlayerKb){
				responseStr = queryAnalysis((mPlayerKb)generalMessage);
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
	public String queryAnalysis(mPlayerKb message){
		String result = FAILURE_ANSWER;
		mPlayerKb response = new mPlayerKb();
		response.setType(message.getType());
		String queryString = allPrefixes;

		// Query treatment depends on the message's type
		try {
			switch(message.getType()){
				case GET_CONFIDENCE:{
					ArrayList<String> list = message.getPlayers();
					HashMap<String,String> resultab= message.getConfidences();
					String status;
					((AgtKBPlayer) myAgent).verifList(list,allPrefixes);
					for(int i=0; i<list.size();i++){	
						queryString += "\nSELECT ?x WHERE {\n "+list.get(i) +" a kbfplyer:Player;\n kbfplyer:RelationStatus ?x. }";
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
							status = nameNode.path("value").getValueAsText();
							resultab.put(list.get(i), status);
						}	
					}
					response.setConfidences(resultab);
					response.setType(mType.GET_CONFIDENCE);
					return response.toJson();
				}
				
				case PUT_CONFIDENCE: {
					HashMap<String,String> confidences = message.getConfidences();
					// Create the new file
					((AgtKBPlayer)myAgent).createPlayerKb(confidences);
					return "KB_UPDATED";
				}
				default: return result;
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

package ia04.projet.loup.players;

import jade.core.Agent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryException;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class AgtKBPlayer extends Agent {
	private Model modeln3 = null;
	private String agtName;
	private File kb;
	private String str = "@prefix kbfplyer:     <http://www.utc.fr/kbfplyer#> .\n" +
			"@prefix werewolves:     <http://www.utc.fr/werewolves#> .\n" +
			"@prefix skos:    <http://www.w3.org/2004/02/skos/core#> .\n" +
			"@prefix foaf:    <http://xmlns.com/foaf/0.1/> ." +
			"\n@prefix owl:     <http://www.w3.org/2002/07/owl#> .\n" +
			"@prefix rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
			"@prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#> .\n\n\n";
	
	
	public AgtKBPlayer(String playerName) throws IOException {
		int i = 0;
		agtName = playerName;
		String fileName = "./resources/kb/players/"+playerName+".n3";
		kb =new File(fileName);
		try{
		if(!kb.exists()){
			kb.createNewFile();
			FileWriter fw = new FileWriter(kb);
			fw.write(str);
		}
		}catch(IOException e){e.printStackTrace();}
		
		
		try{
			// Initialize the model to use for this kb agent
			modeln3 = ModelFactory.createDefaultModel();
			
			// Add both the skos and the foaf kb databases
			modeln3.read(new FileInputStream("./resources/kb/kbfplyer.n3"),"http://utc/","N3"); ++i;
			modeln3.read(new FileInputStream("./resources/kb/players/"+playerName+".n3"),"http://utc/","N3"); ++i;
			
			//this.ReadBDC();
		}
		catch(Exception ex){
			switch(i){
			case 0:
				System.out.println("Could not open skos.n3 file");
				break;
			case 1:
				System.out.println("Could not open foaf.n3 file");
				break;
			case 2:
				System.out.println("Could not open werewolves.n3 file");
				break;
			case 3:
				System.out.println("Could not open kbfplyer.n3 file");
			}
			System.out.println(ex.getMessage());
			this.addBehaviour(new BehaviourKBPlayer());
		}
	}

	@Override
	protected void setup() {
		super.setup();
		this.addBehaviour(new BehaviourKBPlayer());
	}
	
	void verifList(ArrayList<String> players, String prefixes){
		String queryString = prefixes;
		for(int i=0; i < players.size();i++){
			queryString += "\nASK WHERE { kbfplyer:"+players.get(i) +" a kbfplyer:Player. }";
			String result = this.runAskQuery(queryString);
			if (result.equals("false")){
				modeln3.createResource("kbfplyer:"+players.get(i)+" a kbfplyer:Player;\n kbfplyer:RelationStatus kbfplyer:Newcomer. ");
			}
		}
	}
	
	/**
	 * Read the Model and display all the statements in the console
	 */
	void ReadBDC(){
		// list the statements in the Model
		StmtIterator iter = modeln3.listStatements();

		// print out the predicate, subject and object of each statement
		while (iter.hasNext()) {
			Statement stmt      = iter.nextStatement();  // get next statement
			Resource  subject   = stmt.getSubject();     // get the subject
			Property  predicate = stmt.getPredicate();   // get the predicate
			RDFNode   object    = stmt.getObject();      // get the object

			System.out.print(subject.toString());
			System.out.print(" " + predicate.toString() + " ");
			if (object instanceof Resource) {
				System.out.print(object.toString());
			} else {
				// object is a literal
				System.out.print(" \"" + object.toString() + "\"");
			}

			System.out.println(" .");
		} 
	}

	/**
	 * Get a ask query on the model and return the result in a JSON string form
	 * @param qString The request to perform on the model
	 * @return A JSON string containing the result (true / false)
	 */
	public String runAskQuery(String qString) {
		try {
			Query query = QueryFactory.create(qString) ;
			QueryExecution queryExecution =	QueryExecutionFactory.create(query, this.modeln3);
			boolean b = queryExecution.execAsk();
			queryExecution.close();
			return  ""+b;
		}
		catch(QueryException e){
			return "{ \"error\":\"does not understand request\" }";
		}
	}

	/**
	 * Get a SELECT query to perform on a model and return the result in a JSON form
	 * @param qString The SELECT query to perform on the model
	 * @return A JSON string containing the result (standard output)
	 */
	public String runExecQuery(String qString) {
		Query query = QueryFactory.create(qString) ;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		QueryExecution queryExecution =	QueryExecutionFactory.create(query, this.modeln3);
		ResultSet r = queryExecution.execSelect();
		ResultSetFormatter.outputAsJSON(baos,r);
		queryExecution.close();
		return baos.toString();
	}

	public String getAgtName() {
		return agtName;
	}

	public void setAgtName(String agtName) {
		this.agtName = agtName;
	}

	public void createPlayerKb(HashMap<String, String> confidences) throws IOException {
		String OntoFile = this.str;
		for(Map.Entry<String, String> e : confidences.entrySet()){
			OntoFile += "kbfplyer:"+e.getKey()+"\n \t a kbfplayer:Player;\n";
			OntoFile += "\t kbfplyer:RelationStatus kbfplyer:"+e.getValue()+" .\n\n";
		}
		FileWriter fw = new FileWriter(this.kb);
		fw.write(OntoFile);		
	}
	
	
}

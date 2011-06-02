package ia04.projet.loup.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

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

import jade.core.Agent;

public class AgtKbStoryteller extends Agent {
	private Model modeln3 = null;

	public AgtKbStoryteller() {
		int i = 0;
		try{
			// Initialize the model to use for this kb agent
			modeln3 = ModelFactory.createDefaultModel();
			
			// Add both the skos and the foaf kb databases
			modeln3.read(new FileInputStream("./resources/kb/skos.n3"),"http://utc/","N3"); ++i;
			modeln3.read(new FileInputStream("./resources/kb/foaf.n3"),"http://utc/","N3"); ++i;
			modeln3.read(new FileInputStream("./resources/kb/werewolves.n3"),"http://utc/","N3"); ++i;
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
			}
			System.out.println(ex.getMessage());
		}
	}

	@Override
	protected void setup() {
		super.setup();
		this.addBehaviour(new BehaviourKbStoryteller());
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
			return "{ \"answer\" : " + b + " } ";
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
}

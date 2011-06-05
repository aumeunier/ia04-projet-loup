package ia04.projet.loup.rest;
import jade.core.Agent;

/**
 * 
 * @author paul
 */
public class AgtRest extends Agent{

	private static final long serialVersionUID = 1L;

	public AgtRest(){
		RulesMainServer.start();
	}
}

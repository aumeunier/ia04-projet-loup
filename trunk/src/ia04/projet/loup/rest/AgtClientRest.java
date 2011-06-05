package ia04.projet.loup.rest;

import jade.core.Agent;

public class AgtClientRest extends Agent{

	private static final long serialVersionUID = 1L;
	
	public AgtClientRest(){
		addBehaviour(new BehaviorClientRest());
	}
	
}

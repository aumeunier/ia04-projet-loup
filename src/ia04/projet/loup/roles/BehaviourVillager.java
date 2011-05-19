package ia04.projet.loup.roles;

import jade.core.behaviours.Behaviour;

public class BehaviourVillager extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2060328251311657769L;

	/**
	 * Waits for the day and do the corresponding actions
	 *  
	 * @author claquette
	 */
	@Override
	public void action() {
		/** Waits the sunrise */
		
			/** TODO beta1 mayor election */
		
			/** Vote for the day's victim */
			((AgtRole) myAgent).vote();

	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}

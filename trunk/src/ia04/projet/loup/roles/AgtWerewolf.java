package ia04.projet.loup.roles;

public class AgtWerewolf extends AgtRole {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6633110253810476936L;
	
	public AgtWerewolf () {
		super();
		addBehaviour(new BehaviourWerewolf());
	}
	
	public void eatSomebody(){
		switch (currentStrategy){
		case RABBIT:
			//TODO random choice
		}
	}

}

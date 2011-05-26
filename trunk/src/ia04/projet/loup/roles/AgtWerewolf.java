package ia04.projet.loup.roles;

import java.util.ArrayList;
import java.util.Random;

public class AgtWerewolf extends AgtRole {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6633110253810476936L;
	
	public AgtWerewolf () {
		super();
		addBehaviour(new BehaviourWerewolf());
	}
	
	public String eatSomebody(ArrayList<String> candidates){
		switch (currentStrategy){
		case RABBIT:
			Random random = new Random();
			return candidates.get(random.nextInt(candidates.size()));
		default: return null;
		}
	}

}

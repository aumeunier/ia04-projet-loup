package ia04.projet.loup.roles;

import ia04.projet.loup.messages.mAction;

import java.util.ArrayList;
import java.util.Set;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class AgtCupid extends AgtRole {

	public AgtCupid(AID guiID) {
		super(guiID);
		addAndSaveBehaviour(new BehaviourCupid());
	}
	
	public mAction selectLovers(mAction msgContent){
		switch (currentStrategy){
		case RABBIT:
		case BASIC:
		case SHEEP:
			msgContent.setTargetKilled(players.get(random.nextInt(players.size())));
			String tmp = players.get(random.nextInt(players.size()));
			while (tmp == msgContent.getTargetKilled())
				tmp = players.get(random.nextInt(players.size()));
			msgContent.setTargetSaved(tmp);
		}
		return msgContent;
	}
}

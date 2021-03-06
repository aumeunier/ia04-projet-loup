package ia04.projet.loup.roles;

import ia04.projet.loup.Global.Roles;
import ia04.projet.loup.messages.mAction;

import java.util.ArrayList;
import java.util.Set;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class AgtCupid extends AgtRole {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6639332510302096704L;

	public AgtCupid(AID guiID) {
		super(guiID);
		addAndSaveBehaviour(new BehaviourCupid());
	}
	/** initialize the role of the agent */
	protected void initializeRole(){
		role=Roles.CUPID;
	}
	public mAction selectLovers(mAction msgContent){
		if(human)
			return selectLoversHuman(msgContent);
		else 
			return selectLoversBot(msgContent);
	}

	public mAction selectLoversHuman(mAction msgContent){//TODO human method
		String choice = this.askGUI(new ArrayList<String>());
		msgContent.setTargetSaved(choice);
		choice = this.askGUI(new ArrayList<String>());
		msgContent.setTargetKilled(choice);
		return msgContent;
	}

	public mAction selectLoversBot(mAction msgContent){
		switch (currentStrategy){
		case RABBIT:
		case BASIC:
		case SHEEP:
		case DUMMIE:
			msgContent.setTargetKilled(players.get(random.nextInt(players.size())));
			String tmp = players.get(random.nextInt(players.size()));
			while (tmp == msgContent.getTargetKilled())
				tmp = players.get(random.nextInt(players.size()));
			msgContent.setTargetSaved(tmp);
		}
		return msgContent;
	}
}

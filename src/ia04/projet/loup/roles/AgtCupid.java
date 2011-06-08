package ia04.projet.loup.roles;

import ia04.projet.loup.messages.mAction;

import java.util.ArrayList;
import java.util.Set;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class AgtCupid extends AgtRole {

	public AgtCupid(AID guiID) {
		super(guiID);
	}
	
	public mAction selectLovers(mAction msgContent){
		switch (currentStrategy){
		case RABBIT:
		case BASIC:
		case SHEEP:
			msgContent.setTargetKilled((String)confidenceLevel.keySet().toArray()[random.nextInt(confidenceLevel.size())]);
			String tmp = (String)confidenceLevel.keySet().toArray()[random.nextInt(confidenceLevel.size())];
			while (tmp == msgContent.getTargetKilled())
				tmp = (String)confidenceLevel.keySet().toArray()[random.nextInt(confidenceLevel.size())];
			msgContent.setTargetSaved(tmp);
		}
		return msgContent;
	}
}

package ia04.projet.loup.GUI;

import ia04.projet.loup.Debugger;
import ia04.projet.loup.messages.mMessage;
import ia04.projet.loup.messages.mToGui;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class BehavGuiAgtPlyer extends Behaviour {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1958438630758473392L;

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if (msg != null){
			String msgString = msg.getContent();
			Debugger.println(myAgent.getAID().getLocalName().toString()+" reçoit "+ msgString);
			mToGui msgObj = (mToGui) mMessage.parseJson(msgString, mToGui.class);
			((GuiAgtPlayer) myAgent).guiMaj(msgObj);
		}
		
	}

	@Override
	public boolean done() {
		return false;
	}

}

package ia04.projet.loup.GUI;

import jade.core.AID;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

public class GuiAgtPlayer extends GuiAgent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	BotPlayerGui myGui;
	AID myAgt;

	public GuiAgtPlayer(AID id) {
		super();
		myAgt = id;
		myGui = new BotPlayerGui(id.getLocalName(),this);
		myGui.setVisible(true);
		
		this.addBehaviour(new BehavGuiAgtPlyer());
	}

	@Override
	protected void onGuiEvent(GuiEvent arg0) {
		// TODO Auto-generated method stub

	}

}

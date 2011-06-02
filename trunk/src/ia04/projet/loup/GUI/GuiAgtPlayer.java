package ia04.projet.loup.gui;

import ia04.projet.loup.messages.mToGui;
import jade.core.AID;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

public class GuiAgtPlayer extends GuiAgent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	BotPlayerGui myGui;
	AID myAgt, roleAgt;
	

	public GuiAgtPlayer(AID id, AID role) {
		super();
		myAgt = id;
		roleAgt = role;
		myGui = new BotPlayerGui(id.getLocalName(),this);
		myGui.setVisible(true);
		
		this.addBehaviour(new BehavGuiAgtPlyer());
	}

	@Override
	protected void onGuiEvent(GuiEvent arg0) {
	}
	
	public void guiMaj(mToGui msgObj) {
		switch(msgObj.getType()){
		case STATUS:
			myGui.setStat(msgObj.getValue());
			break;
		case PLAYERS_LIST:
			myGui.setPlayerList(msgObj.getValue());
			break;
		case ROLE:
			myGui.setRole(msgObj.getRole().toString());
			break;
		case PHASE_ACTION :
			myGui.setStoryView(msgObj.getStoryTelling());
			break;
		default: break;
		
		}
		
	}
	//getters and setters
	public BotPlayerGui getMyGui() {
		return myGui;
	}

	public void setMyGui(BotPlayerGui myGui) {
		this.myGui = myGui;
	}

	public AID getMyAgt() {
		return myAgt;
	}

	public void setMyAgt(AID myAgt) {
		this.myAgt = myAgt;
	}

	public AID getRoleAgt() {
		return roleAgt;
	}

	public void setRoleAgt(AID roleAgt) {
		this.roleAgt = roleAgt;
	}

}

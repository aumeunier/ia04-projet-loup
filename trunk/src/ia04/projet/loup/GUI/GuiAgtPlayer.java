package ia04.projet.loup.gui;

import ia04.projet.loup.Global;
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
	

	public GuiAgtPlayer(AID id) {
		super();
		myAgt = id;
		myGui = new BotPlayerGui(id.getLocalName(),id);
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
			roleAgt = new AID(myAgt.getLocalName()+Global.LOCALNAME_SUFFIX_ROLE,AID.ISLOCALNAME);
			break;
		case STORYTELLING :
			myGui.setStoryView(msgObj.getValue());
			break;
		default: break;
		
		}
		myGui.repaint();
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

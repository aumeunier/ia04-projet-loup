package ia04.projet.loup.gui;

import ia04.projet.loup.Global;
import ia04.projet.loup.messages.mToGui;
import jade.core.AID;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

import java.util.ArrayList;

public class AgtPlayerGui extends GuiAgent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GuiBot myGui;
	AID myAgt, roleAgt;
	

	public AgtPlayerGui(AID id) {
		super();
		myAgt = id;
		myGui = new GuiBot(id.getLocalName(),id);
		myGui.setVisible(true);
		
		this.addBehaviour(new BehaviourAgtPlayerGui());
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
			ArrayList<String> list = msgObj.getPlayers();
			for(int i=0; i < list.size(); i++){
				myGui.addPlayerToTheList(list.get(i));
			}
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
	public GuiBot getMyGui() {
		return myGui;
	}

	public void setMyGui(GuiBot myGui) {
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

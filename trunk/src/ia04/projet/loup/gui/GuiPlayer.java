package ia04.projet.loup.gui;

import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * GUI for the human player
 * @author paul
 */
public class GuiPlayer extends GuiBot{

	private static final long serialVersionUID = 1L;
	
	private JButton jButtonChoose;
	
	/**
	 * Constructor
	 * @param String playerName
	 * @param GuiAgent agt
	 * @throws HeadlessException
	 */
	public GuiPlayer(String arg0, GuiAgent agt) throws HeadlessException {
		super(arg0, agt);
		this.initialize();
	}

	private void initialize() {
		jButtonChoose = new JButton("Vote");
		jButtonChoose.setEnabled(false);
		buttonPanel.add(jButtonChoose);
		
		jButtonChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GuiEvent guiEvent = new GuiEvent(e, AgtPlayerGui.CHOOSE_TYPE);
				guiEvent.addParameter(jListPlayerList.getSelectedValue());
				myPlayerAgent.postGuiEvent(guiEvent);
			}
		});
	}
	
	public void enableVote(){
		this.jButtonChoose.setEnabled(true);
	}
	
	public void disableVote(){
		this.jButtonChoose.setEnabled(false);
	}
}

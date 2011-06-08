package ia04.projet.loup.gui;

import jade.core.AID;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.HeadlessException;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * GUI for the human player
 * @author paul
 */
public class GuiPlayer extends GuiBot{

	private static final long serialVersionUID = 1L;
	
	private JButton jButtonChoose;
	
	public GuiPlayer(String arg0, AID agt) throws HeadlessException {
		super(arg0, agt);
		this.initialize();
	}

	private void initialize() {
		jButtonChoose = new JButton("Vote");
		jButtonChoose.setEnabled(false);
		
		buttonPanel.add(jButtonChoose);
	}
	
	public void enableVote(){
		this.jButtonChoose.setEnabled(true);
	}
	
	public void disableVote(){
		this.jButtonChoose.setEnabled(false);
	}
}

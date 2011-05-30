package ia04.projet.loup.GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class BotPlayerGui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JTextArea name;
	JTextArea stat;
	JTextArea role;
	JTextArea StoryView;
	JTextArea PlayerList;
	
	public BotPlayerGui(String arg0) throws HeadlessException {
		super(arg0);
		initialize(arg0);
		output();
	}
	
	private void output() {
		setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		setSize(640, 480);
		setVisible(true);
	}
	
	private void initialize(String arg0) {
		
		//Components initialization
		JLabel nameLab = new JLabel("NAME :");
		name = new JTextArea(arg0);
		name.setEditable(false);
		
		JLabel statLab = new JLabel("STATUS :");
		stat = new JTextArea();
		stat.setEditable(false);
		
		JLabel roleLab = new JLabel("ROLE :");
		role = new JTextArea();
		role.setEditable(false);
		
		StoryView = new JTextArea();
		StoryView.setText("Player initialization...");
		StoryView.setEditable(false);
		
		
		PlayerList = new JTextArea();
		PlayerList.setEditable(false);
		
		//Layout creation
		GridBagConstraints cNameLab = new GridBagConstraints();	
		cNameLab.gridx = 0;
		cNameLab.gridy = 0;
		
		GridBagConstraints cName = new GridBagConstraints();	
		cName.gridx = 1;
		cName.gridy = 0;
		
		GridBagConstraints cStatLab = new GridBagConstraints();	
		cStatLab.gridx = 0;
		cStatLab.gridy = 1;
		
		GridBagConstraints cStat = new GridBagConstraints();	
		cStat.gridx = 1;
		cStat.gridy = 1;
		
		GridBagConstraints cRoleLab = new GridBagConstraints();	
		cRoleLab.gridx = 0;
		cRoleLab.gridy = 2;
		
		GridBagConstraints cRole = new GridBagConstraints();	
		cRole.gridx = 1;
		cRole.gridy = 2;
		
		GridBagConstraints cStoryView = new GridBagConstraints();	
		cStoryView.gridx = 3;
		cStoryView.gridy = 0;
		cStoryView.gridheight = 6;
		
		GridBagConstraints cPlayerList = new GridBagConstraints();	
		cPlayerList.gridx = 0;
		cPlayerList.gridy = 3;
		cPlayerList.gridheight = 3;
		
		//Panel construction
		JPanel mainPanel = new JPanel(new GridBagLayout());
		mainPanel.add(nameLab,cNameLab);
		mainPanel.add(name,cName);
		mainPanel.add(statLab,cStatLab);
		mainPanel.add(stat,cStat);
		mainPanel.add(roleLab,cRoleLab);
		mainPanel.add(role,cRole);
		mainPanel.add(StoryView,cStoryView);
		mainPanel.add(PlayerList,cPlayerList);
	}
}

package ia04.projet.loup.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class BotPlayerGui extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JTextArea name;
	JTextArea stat;
	JTextArea role;
	JTextArea StoryView;
	JTextArea PlayerList;
	GuiAgtPlayer MyPlayerAgent;
	
	
	public BotPlayerGui(String arg0, GuiAgtPlayer agt) throws HeadlessException {
		super(arg0);
		this.MyPlayerAgent = agt;
		initialize(arg0);
		output();
	}
	
	private void output() {
		setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		setSize(640, 480);
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO
		/*if(e.getSource()== x){
			GuiEvent ge = new GuiEvent(this, GuiAgtPlayer.EVENT);
			MyAgent.postGuiEvent(ge);
		}*/
	}
	
	public String getName() {
		return name.getText();
	}

	public void setName(String nam) {
		this.name.setText(nam);
	}

	public String getStat() {
		return stat.getText();
	}
	
	public void setStat(String state) {
			this.stat.setText(state);
		}
	
	public String getRole() {
		return role.getText();
	}
	
	public void setRole(String rol) {
		this.role.setText(rol);
	}
		
	public String getStoryView() {
		return StoryView.getText();
	}

	public void setStoryView(String str) {
		StoryView.append("\n"+str);
	}

	public String getPlayerList() {
		return PlayerList.getText();
	}

	public void setPlayerList(String str) {
		PlayerList.append("\n"+str);
	}

	
}

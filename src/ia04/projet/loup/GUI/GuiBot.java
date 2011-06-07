package ia04.projet.loup.gui;

import jade.core.AID;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextArea;

public class GuiBot extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JTextArea name;
	JTextArea stat;
	JTextArea role;
	//JScrollPane storyPane;
	JTextArea StoryView;
	JTextArea PlayerList;
	AID MyPlayerAgent;
	
	
	public GuiBot(String arg0, AID agt) throws HeadlessException {
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
		
		
		StoryView = new JTextArea("",30,30);
		StoryView.setWrapStyleWord(true);
		StoryView.setText("Player initialization...");
		StoryView.setEditable(false);
		
		
		
		PlayerList = new JTextArea();
		PlayerList.setEditable(false);
		
		//Layout creation
		GridBagConstraints cNameLab = new GridBagConstraints(0,0,1,1,1.0,1.0,GridBagConstraints.EAST,
				GridBagConstraints.BOTH,new Insets(10,10,10,10),0,0);
		
		GridBagConstraints cName = new GridBagConstraints(1,0,1,1,1.0,1.0,GridBagConstraints.EAST,
				GridBagConstraints.BOTH,new Insets(10,10,10,10),0,0);
		
		GridBagConstraints cStatLab = new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.EAST,
				GridBagConstraints.BOTH,new Insets(10,10,10,10),0,0);
		
		GridBagConstraints cStat = new GridBagConstraints(1,1,1,1,1.0,1.0,GridBagConstraints.EAST,
				GridBagConstraints.BOTH,new Insets(10,10,10,10),0,0);
		
		GridBagConstraints cRoleLab = new GridBagConstraints(0,2,1,1,1.0,1.0,GridBagConstraints.EAST,
				GridBagConstraints.BOTH,new Insets(10,10,10,10),0,0);
		
		GridBagConstraints cRole = new GridBagConstraints(1,2,1,1,1.0,1.0,GridBagConstraints.EAST,
				GridBagConstraints.BOTH,new Insets(10,10,10,10),0,0);
		
		GridBagConstraints cStoryView = new GridBagConstraints(0,0,1,1,1.0,1.0,GridBagConstraints.EAST,
				GridBagConstraints.BOTH,new Insets(10,10,10,10),0,0);
		
		GridBagConstraints cPlayerList = new GridBagConstraints(0,3,1,1,1.0,1.0,GridBagConstraints.EAST,
				GridBagConstraints.BOTH,new Insets(10,10,10,10),0,0);
		
		//Panel construction
		JPanel leftPanel = new JPanel(new GridBagLayout());
		leftPanel.add(nameLab,cNameLab);
		leftPanel.add(name,cName);
		leftPanel.add(statLab,cStatLab);
		leftPanel.add(stat,cStat);
		leftPanel.add(roleLab,cRoleLab);
		leftPanel.add(role,cRole);
		leftPanel.add(PlayerList,cPlayerList);
		
		JPanel mainPanel = new JPanel(new GridBagLayout());
		JScrollPane storyPane = new JScrollPane(StoryView,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		storyPane.setPreferredSize(new Dimension(400,400));
		
		mainPanel.add(leftPanel);
		mainPanel.add(storyPane);		
		this.add(mainPanel);
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
		StoryView.append("\n" +str);
	}

	public String getPlayerList() {
		return PlayerList.getText();
	}

	public void setPlayerList(String str) {
		PlayerList.append("\n"+str);
	}

	
}

package ia04.projet.loup.gui;

import jade.core.AID;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;

/**
 * Class which handles the GUI for a bot player
 * @author Guillaume
 */
public class GuiBot extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	protected JTextArea jTextAreaName;
	protected JTextArea jTextAreaStat;
	protected JTextArea jTextAreaRole;
	protected JTextArea jTextAreaStoryView;
	protected JList jListPlayerList;
	protected JPanel leftPanel;
	protected JPanel buttonPanel;
	protected JButton jButtonHelp;
	protected AID myPlayerAgent;
	
	/**
	 * Construction
	 * @param String playerName
	 * @param AID agt
	 * @throws HeadlessException
	 */
	public GuiBot(String playerName, AID agt) throws HeadlessException {
		super(playerName);
		this.myPlayerAgent = agt;
		this.initialize(playerName);
		this.output();
	}
	
	/**
	 * Set the default size to the frame
	 */
	protected void output() {
		setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		setSize(640, 480);
	}
	
	/**
	 * Initialize the Frame
	 * @param playerName
	 */
	protected void initialize(String playerName) {
		
		//Components initialization
		JLabel nameLab = new JLabel("NAME :");
		jTextAreaName = new JTextArea(playerName);
		jTextAreaName.setEditable(false);
		
		JLabel statLab = new JLabel("STATUS :");
		jTextAreaStat = new JTextArea();
		jTextAreaStat.setEditable(false);
		
		JLabel roleLab = new JLabel("ROLE :");
		jTextAreaRole = new JTextArea();
		jTextAreaRole.setEditable(false);
		
		jTextAreaStoryView = new JTextArea("",30,30);
		jTextAreaStoryView.setWrapStyleWord(true);
		jTextAreaStoryView.setText("Player initialization...");
		jTextAreaStoryView.setEditable(false);
		
		DefaultListModel model = new DefaultListModel();
		jListPlayerList = new JList(model);
		
		jButtonHelp = new JButton("Help");
		
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
		
		//GridBagConstraints cStoryView = new GridBagConstraints(0,0,1,1,1.0,1.0,GridBagConstraints.EAST,
		//		GridBagConstraints.BOTH,new Insets(10,10,10,10),0,0);
		
		GridBagConstraints cPlayerList = new GridBagConstraints(0,3,1,1,1.0,1.0,GridBagConstraints.EAST,
				GridBagConstraints.BOTH,new Insets(10,10,10,10),0,0);
		
		//Panel construction
		JPanel informationPanel = new JPanel(new GridBagLayout());
		informationPanel.add(nameLab,cNameLab);
		informationPanel.add(jTextAreaName,cName);
		informationPanel.add(statLab,cStatLab);
		informationPanel.add(jTextAreaStat,cStat);
		informationPanel.add(roleLab,cRoleLab);
		informationPanel.add(jTextAreaRole,cRole);
		
		buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(jButtonHelp);
		
		leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(informationPanel, BorderLayout.PAGE_START);
		leftPanel.add(jListPlayerList, BorderLayout.CENTER);
		leftPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		JScrollPane storyPane = new JScrollPane(jTextAreaStoryView,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		storyPane.setPreferredSize(new Dimension(400,400));
		
		mainPanel.add(leftPanel, BorderLayout.WEST);
		mainPanel.add(storyPane, BorderLayout.EAST);		
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
		return jTextAreaName.getText();
	}

	public void setName(String nam) {
		this.jTextAreaName.setText(nam);
	}

	public String getStat() {
		return jTextAreaStat.getText();
	}
	
	public void setStat(String state) {
			this.jTextAreaStat.setText(state);
		}
	
	public String getRole() {
		return jTextAreaRole.getText();
	}
	
	public void setRole(String rol) {
		this.jTextAreaRole.setText(rol);
	}
		
	public String getStoryView() {
		return jTextAreaStoryView.getText();
	}

	public void setStoryView(String str) {
		jTextAreaStoryView.append("\n" +str);
		jTextAreaStoryView.setCaretPosition( jTextAreaStoryView.getDocument().getLength() );
	}

	/**
	 * Return all the player names
	 * @return String[]
	 */
	public String[] getPlayerList() {
		ListModel model = jListPlayerList.getModel();
		return (String[]) ((DefaultListModel) model).toArray();
	}

	/**
	 * Add a player to the list, if the player does not already exist in the list return true, else false
	 * @param str
	 * @return
	 */
	public boolean addPlayerToTheList(String str) {
		ListModel model = jListPlayerList.getModel();
		if(((DefaultListModel) model).contains(str)){
			return false;
		}else{
			((DefaultListModel) model).add(model.getSize(), str);
			return true;
		}
	}
	
	/**
	 * Remove a player to the list, if the player does not already exist in the list return true, else false
	 * @param str
	 * @return
	 */
	public boolean removePlayerFromTheList(String str) {
		ListModel model = jListPlayerList.getModel();
		return ((DefaultListModel) model).removeElement(str);
	}
}

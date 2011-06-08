package ia04.projet.loup.gui;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class GuiHelp extends JFrame{

	private static final long serialVersionUID = 1L;
	
	public GuiHelp(){
		super("Help");
		JTextArea rules = new JTextArea();
		rules.setLineWrap(true);
		rules.setEditable(false);
		try 
		{
			URL url = new URL("http://localhost:8182/rules/");
			InputStream input = url.openStream();
			InputStreamReader streamReader = new InputStreamReader(input, "UTF-8");
			BufferedReader buffer = new BufferedReader(streamReader);
			String line = "";
			
			while (null != (line = buffer.readLine())){
				rules.append(line+"\n");
			}

			this.add(rules);
			
			setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
			setSize(640, 480);
			setVisible(true);
		}catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}

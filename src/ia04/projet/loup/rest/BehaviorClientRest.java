package ia04.projet.loup.rest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;

import jade.core.behaviours.OneShotBehaviour;

import org.restlet.engine.http.HttpResponse;

import com.sun.org.apache.xml.internal.utils.URI;

public class BehaviorClientRest extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	URL url;
	String s = null;

	@Override
	public void action() {
		try 
		{
			url = new URL("http://localhost:8182/rules/");
			InputStream input = url.openStream();
			StringWriter writer = new StringWriter();
			InputStreamReader streamReader = new InputStreamReader(input);
			//le buffer permet le readline
			BufferedReader buffer = new BufferedReader(streamReader);
			String line="";
			
			while (null != (line = buffer.readLine())){
				writer.write(line); 
			}
			// Sortie finale dans le String
			System.out.println(writer.toString());
			
		}catch (Exception e) {
			System.out.println(e.toString());
		}

	}
}

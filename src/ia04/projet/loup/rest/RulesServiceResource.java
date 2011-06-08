package ia04.projet.loup.rest;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class RulesServiceResource extends ServerResource {
	private String file = "./resources/rest/rules.txt";

	@Get
	public void retrieve() {
		System.out.println("Incoming Request");

		try {

			InputStream ips = new FileInputStream(file);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			String chaine = "";
			while ((ligne = br.readLine()) != null) {
				chaine += ligne + "\n";
			}
			br.close();

			getResponse().setEntity(
					new StringRepresentation(chaine, MediaType.TEXT_PLAIN));
			getResponse().setStatus(Status.SUCCESS_OK);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}

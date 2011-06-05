package ia04.projet.loup.rest;

import org.restlet.Component;
import org.restlet.data.Protocol;

public class RulesMainServer {

		private static Component component;
		
		public static void start() {
			try { // Create a new Component.
				component = new Component();
				// Add a new HTTP server listening on port 8182.
				component.getServers().add(Protocol.HTTP, 8182);
				component.getDefaultHost().attach(new RulesApplication());
				component.start(); // Start the component.
				System.out.println("Server launched !!!");
			} catch(Exception ex) {
				System.out.println(ex.toString());
			}
		}
		
		public static void stop() {
			try{
				component.stop();
			}catch(Exception ex){
				System.out.println(ex.toString());
			}
		}
}

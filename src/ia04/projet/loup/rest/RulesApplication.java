package ia04.projet.loup.rest;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class RulesApplication extends Application {

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attach("/rules/", RulesServiceResource.class);
		System.out.println("Application Created");
		return router;
	}
}
